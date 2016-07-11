/**
  * Created by anicolaspp on 7/4/16.
  */
package com.nico.actors

import akka.actor.{Actor, ActorLogging, Props}
import com.nico.persistence.TransactionManager

class BankManager(accounts: List[String]) extends Actor with ActorLogging {

  val subscribers = Init()

  def Init() = {
    log.debug("com.nico.actors.BankManager started")
    accounts.map(acc => context.actorOf(AccountSubscriber.props(TransactionManager.onDisk(acc, "/Users/anicolaspp/accounts"))))
  }

  override def receive: Actor.Receive = {
    case "accounts"  =>  println("number of accounts: " + accounts.length)
  }
}


object BankManager {
  def props(accounts: List[String]) = Props(new BankManager(accounts))
}
