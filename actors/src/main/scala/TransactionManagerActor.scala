/**
  * Created by anicolaspp on 7/3/16.
  */
package com.nico.actors

import akka.actor.{ActorLogging, Props, Actor}
import akka.actor.Actor.Receive
import akka.event.LoggingReceive
import com.nico.actors.TransactionManagerActor._
import com.nico.persistence.{Account, AccountStorage, TransactionManager}

class TransactionManagerActor(account: String) extends Actor with ActorLogging {

  val manager = (new TransactionManager(account) with AccountStorage).manager


  override def receive: Receive = {
    case AccountInfo()    =>  sender() ! AccountInfoResult(manager.accountInfo)

    case Deposit(amount)  =>  {
      log.debug("deposit: " + account)

      sender() ! AccountInfoResult(manager.deposit(amount))
    }

    case Extract(amount)  =>  {
      log.debug("extract: " + account)

      val (result, acc) = manager.extract(amount)

      sender() ! ExtractResult(result, acc)
    }
  }
}

object TransactionManagerActor {
  def props(account: String): Props = Props(new TransactionManagerActor(account))

  trait Transaction

  case class AccountInfo() extends Transaction
  case class AccountInfoResult(account: Account)

  case class Deposit(amount: Double) extends Transaction
  case class Extract(amount: Double) extends Transaction

  case class ExtractResult(result: Boolean, account: Account)
}