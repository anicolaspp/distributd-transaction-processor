/**
  * Created by anicolaspp on 7/3/16.
  */
package com.nico.actors

import akka.actor.{Props, Actor}
import akka.actor.Actor.Receive
import com.nico.actors.TransactionManagerActor._
import com.nico.persistence.{Account, AccountStorage, TransactionManager}

class TransactionManagerActor(account: String) extends Actor {

  val manager = (new TransactionManager(account) with AccountStorage).manager


  override def receive: Receive = {
    case AccountInfo()    =>  sender() ! AccountInfoResult(manager.accountInfo)

    case Deposit(amount)  =>  sender() ! AccountInfoResult(manager.deposit(amount))

    case Extract(amount)  =>  {
      val (result, acc) = manager.extract(amount)

      sender() ! ExtractResult(result, acc)
    }
  }
}

object TransactionManagerActor {
  def props(account: String): Props = Props(new TransactionManagerActor(account))

  case class AccountInfo()
  case class AccountInfoResult(account: Account)

  case class Deposit(amount: Double)
  case class Extract(amount: Double)

  case class ExtractResult(result: Boolean, account: Account)
}