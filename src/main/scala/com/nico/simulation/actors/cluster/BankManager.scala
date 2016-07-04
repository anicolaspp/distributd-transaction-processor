package com.nico.simulation.actors.cluster

import akka.actor.{Props, ActorLogging, Actor}

/**
  * Created by anicolaspp on 7/4/16.
  */


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
