/**
  * Created by anicolaspp on 7/4/16.
  */

package com.nico.simulation.actors.cluster

import akka.actor.{Props, ActorLogging, Actor}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Subscribe
import akka.event.LoggingReceive
import com.nico.actors.TransactionManagerActor


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
