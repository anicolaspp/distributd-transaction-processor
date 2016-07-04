/**
  * Created by anicolaspp on 7/3/16.
  */

package com.nico.simulation.actors.cluster

import akka.actor._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, Subscribe}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings}
import akka.event.LoggingReceive
import com.nico.actors.TransactionManagerActor
import com.nico.actors.TransactionManagerActor.{Transaction, Extract, Deposit}
import com.typesafe.config.ConfigFactory

import scala.util.Random


object Cluster {
  def main(args: Array[String]) {

    val port = args(1).toInt
    val configuration = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.load())

    val system = ActorSystem("TransactionCluster", configuration)

    val numberOfAccounts = args(0).toInt
    val accounts = (0 to numberOfAccounts).map (_.toString).toSet

    system.actorOf(ClusterSingletonManager.props(
      singletonProps = BankManager.props(accounts.toList),
      terminationMessage = PoisonPill,
      settings = ClusterSingletonManagerSettings(system).withSingletonName("banker")
    ))


    readLine()
  }
}

object ClusterPublisher {
  def main(args: Array[String]) {
    val configuration = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + 9080)
      .withFallback(ConfigFactory.load())

    val system = ActorSystem("TransactionCluster", configuration)

    val numberOfAccounts = args(0).toInt
    val accounts = (0 to numberOfAccounts).map (_.toString)

    system.actorOf(TransactionPublisher.props(accounts.toList))

    readLine()
  }
}


class BankManager(accounts: List[String]) extends Actor with ActorLogging {

  val subscribers = Init()

  def Init() = {
    log.debug("BankManager started")
    accounts.map(acc => context.actorOf(AccountSubscriber.props(acc)))
  }

  override def receive: Actor.Receive = {
    case "accounts"  =>  println("number of accounts: " + accounts.length)
  }
}

object BankManager {
  def props(accounts: List[String]) = Props(new BankManager(accounts))
}

class AccountSubscriber(account: String) extends Actor with ActorLogging {
  val manager = context.actorOf(TransactionManagerActor.props(account), account + ".ActorManager")
  val mediator = DistributedPubSub(context.system).mediator

  mediator ! Subscribe(account, self)

  log.debug("AccountSubscriber started")


  override def receive: Actor.Receive = LoggingReceive {

    case d @ TransactionManagerActor.Deposit(amount)  =>  manager ! d
    case e @ TransactionManagerActor.Extract(amount)  =>  manager ! e
  }
}

object AccountSubscriber {
  def props(account: String) = Props(new AccountSubscriber(account))
}

class TransactionPublisher(accounts: List[String]) extends Actor with ActorLogging {
  import scala.concurrent.duration._
  import context.dispatcher

  val mediator = DistributedPubSub(context.system).mediator
  val ticks = context.system.scheduler.schedule(1 seconds, 500 milliseconds, self, "ticks")

  override def receive: Actor.Receive = {
    case "ticks"        =>

      val selectedAccount = accounts(Math.abs(Random.nextInt()) % accounts.length)

      mediator ! Publish(selectedAccount, genTransaction)

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

