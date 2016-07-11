/**
  * Created by nperez on 7/6/16.
  */

package com.nico.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.nico.actors.TransactionManagerActor.{Deposit, Extract}
import com.nico.persistence.Transaction


class TransactionPublisher extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator

  override def receive: Actor.Receive = {



    case t @ Transaction(accId, amount, stamp) => {
      log.debug(s"Transaction Received: $t")

      if (amount > 0) {
        mediator ! Publish(accId, Deposit(amount), sendOneMessageToEachGroup = true)
      }
      else {
        mediator ! Publish(accId, Extract(Math.abs(amount)), sendOneMessageToEachGroup = true)
      }
    }
  }
}

object TransactionPublisher {
  def props() = Props(new TransactionPublisher)
}
