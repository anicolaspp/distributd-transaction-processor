/**
  * Created by anicolaspp on 7/4/16.
  */

package com.nico.simulation.actors.cluster

import akka.actor.{Props, ActorLogging, Actor}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Subscribe
import akka.event.LoggingReceive
import com.nico.actors.TransactionManagerActor
import com.nico.persistence.TransactionManager


class AccountSubscriber(transactionManager: TransactionManager) extends Actor with ActorLogging {

  val account = transactionManager.manager.accountInfo.id

  val manager = context.actorOf(TransactionManagerActor.props(transactionManager), account + ".ActorManager")
  val mediator = DistributedPubSub(context.system).mediator

  mediator ! Subscribe(account, Some(account), self)

  log.debug("AccountSubscriber started")


  override def receive: Actor.Receive = LoggingReceive {

    case d @ TransactionManagerActor.Deposit(amount)  =>  manager ! d
    case e @ TransactionManagerActor.Extract(amount)  =>  manager ! e
  }
}


object AccountSubscriber {
  def props(transactionManager: TransactionManager) = Props(new AccountSubscriber(transactionManager))
}
