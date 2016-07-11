/**
  * Created by anicolaspp on 7/3/16.
  */
package com.nico.actors

import akka.actor.{Actor, ActorLogging, Props}
import com.nico.actors.TransactionManagerActor._
import com.nico.persistence.{Account, TransactionManager}

class TransactionManagerActor(transactionManager: TransactionManager) extends Actor with ActorLogging {


  override def receive: Receive = {
    case AccountInfo()    =>  sender() ! AccountInfoResult(transactionManager.manager.accountInfo)

    case Deposit(amount)  =>  {
      log.debug("deposit: " + transactionManager.manager.accountInfo.id)

      sender() ! AccountInfoResult(transactionManager.manager.deposit(amount))
    }

    case Extract(amount)  =>  {
      log.debug("extract: " + transactionManager.manager.accountInfo.id)

      val (result, acc) = transactionManager.manager.extract(amount)

      sender() ! ExtractResult(result, acc)
    }
  }
}

object TransactionManagerActor {
  def props(transactionManager: TransactionManager): Props = Props(new TransactionManagerActor(transactionManager))

  trait Transaction

  case class AccountInfo() extends Transaction
  case class AccountInfoResult(account: Account)

  case class Deposit(amount: Double) extends Transaction
  case class Extract(amount: Double) extends Transaction

  case class ExtractResult(result: Boolean, account: Account)
}