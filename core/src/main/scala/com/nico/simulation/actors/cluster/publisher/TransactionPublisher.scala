/**
  * Created by nperez on 7/6/16.
  */

package com.nico.simulation.actors.cluster.publisher

import akka.actor.{Props, ActorLogging, Actor}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.nico.actors.TransactionManagerActor.{Extract, Deposit}
import com.nico.persistence.Transaction


class TransactionPublisher extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator

  override def receive: Actor.Receive = {

    case Transaction(accId, amount, stamp) => if (amount > 0) {
      mediator ! Publish(accId, Deposit(amount), sendOneMessageToEachGroup = true)
    }
    else {
      mediator ! Publish(accId, Extract(Math.abs(amount)), sendOneMessageToEachGroup = true)
    }
  }
}

object TransactionPublisher {
  def props() = Props(new TransactionPublisher)
}
