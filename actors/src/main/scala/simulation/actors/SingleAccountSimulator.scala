/**
  * Created by anicolaspp on 7/3/16.
  */

package com.nico.simulation.actors

import akka.actor._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Subscribe
import akka.cluster.singleton.{ClusterSingletonManagerSettings, ClusterSingletonManager}
import com.nico.actors.TransactionManagerActor
import com.nico.actors.TransactionManagerActor._
import com.nico.persistence.TransactionManager
import com.nico.simulation.actors.Driver.Start
import com.typesafe.config.ConfigFactory
import org.joda.time.{DateTime, Period, PeriodType}

import scala.concurrent.duration._
import scala.util.Random

object SingleAccountSimulator {
  def main(args: Array[String]) {
    val actorySystem = ActorSystem("simulator")

    val numberOfTransactions = args(0).toInt
    val accId = Random.nextInt(100).toString
    val manager = actorySystem.actorOf(TransactionManagerActor.props(TransactionManager.onDisk(accId, "/Users/anicolaspp/accounts")))

    val driver = actorySystem.actorOf(Driver.props(manager, numberOfTransactions), "driver")


    driver ! Start()

    readLine()
  }
}

class Driver(manager: ActorRef, n: Int) extends Actor {

  var started = false
  var time = DateTime.now()
  var count = 0

  import context.dispatcher

  var schedule: Cancellable = _

  override def receive: Receive = {
    case Start()  =>  {
      started = true
      time = DateTime.now()

      schedule = context.system.scheduler.schedule(1 seconds, 1 milliseconds, self, "TICK")
    }

    case "TICK"   =>  {
      val transactionAmount = Random.nextInt() % 100

      if (transactionAmount > 0) {
        manager ! Deposit(transactionAmount)
      }
      else {
        manager ! Extract(Math.abs(transactionAmount))
      }
    }

    case AccountInfoResult(acc)   =>   {
      count = count + 1

      if (count == n) {
        schedule.cancel()

        val p = new Period(time, DateTime.now(), PeriodType.millis());

        println("Done in: " + p.getMillis)
      }
    }

    case ExtractResult(_,_) =>  {
      count = count + 1

      if (count == n) {
        schedule.cancel()

        val p = new Period(time, DateTime.now(), PeriodType.millis());

        println("Done in: " + p.getMillis)
      }
    }

  }
}

object Driver {
  def props(manager: ActorRef, numberOfTransactions: Int): Props = Props(new Driver(manager, numberOfTransactions))

  case class Start()
}


