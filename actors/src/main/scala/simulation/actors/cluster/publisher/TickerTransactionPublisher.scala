/**
  * Created by anicolaspp on 7/3/16.
  */

package com.nico.simulation.actors.cluster.publisher

import akka.actor._
import com.nico.persistence.Transaction
import org.joda.time.DateTime

import scala.util.Random


class TickerTransactionPublisher(accounts: List[String]) extends Actor with ActorLogging {
  import context.dispatcher

  import scala.concurrent.duration._

  val ticks = context.system.scheduler.schedule(1 seconds, 1 milliseconds, self, "ticks")

  val publisher = context.actorOf(TransactionPublisher.props(), "actor-publisher")

  override def receive: Actor.Receive = {
    case "ticks"        =>

      val selectedAccount = accounts(Math.abs(Random.nextInt()) % accounts.length)
      val transaction = Transaction(selectedAccount, Random.nextInt(100), DateTime.now())

      publisher ! transaction

      log.info(s"Transaction published: $transaction")
  }
}

object TickerTransactionPublisher {
  def props(accounts: List[String]) = Props(new TickerTransactionPublisher(accounts))
}