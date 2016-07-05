/**
  * Created by anicolaspp on 7/3/16.
  */

package com.nico.simulation.actors.cluster.publisher

import akka.actor._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.nico.actors.TransactionManagerActor.{Deposit, Extract, Transaction}

import scala.util.Random


class TransactionPublisher(accounts: List[String]) extends Actor with ActorLogging {
  import context.dispatcher

  import scala.concurrent.duration._

  val mediator = DistributedPubSub(context.system).mediator
  val ticks = context.system.scheduler.schedule(1 seconds, 100 milliseconds, self, "ticks")

  override def receive: Actor.Receive = {
    case "ticks"        =>

      val selectedAccount = accounts(Math.abs(Random.nextInt()) % accounts.length)

      mediator ! Publish(selectedAccount, genTransaction, true)

      log.info(selectedAccount)
  }

  def genTransaction: Transaction = {
    val transactionAmount = Random.nextInt() % 100

    if (transactionAmount > 0) {
      Deposit(transactionAmount)
    }
    else {
      Extract(Math.abs(transactionAmount))
    }
  }
}

object TransactionPublisher {
  def props(accounts: List[String]) = Props(new TransactionPublisher(accounts))
}




