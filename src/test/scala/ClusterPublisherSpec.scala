/**
  * Created by nperez on 7/6/16.
  */

package com.nico
package test

import akka.actor.ActorSystem
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Subscribe, SubscribeAck}
import akka.testkit.{ImplicitSender, TestKit}
import com.nico.actors.TransactionManagerActor.{Deposit, Extract}
import com.nico.persistence.Transaction
import com.nico.simulation.actors.cluster.publisher.TransactionPublisher
import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpecLike}

class ClusterPublisherSpec extends TestKit(ActorSystem("pubsub"))
  with FlatSpecLike
  with Matchers
  with ImplicitSender
  with BeforeAndAfterAll {

  it should "publish deposit transactions to topic" in {
    val mediator = DistributedPubSub(system).mediator

    mediator ! Subscribe("1", Some("1"), testActor)

    expectMsgType[SubscribeAck]

    val publisher = system.actorOf(TransactionPublisher.props)

    publisher ! Transaction("1", 100, DateTime.now())

    val msg = expectMsgType[Deposit]

    msg.amount should be(100)
  }

  it should "publish extract transactions to topic" in {
    val mediator = DistributedPubSub(system).mediator

    mediator ! Subscribe("10", Some("10"), testActor)

    expectMsgType[SubscribeAck]

    val publisher = system.actorOf(TransactionPublisher.props)

    publisher ! Transaction("10", -100, DateTime.now())

    val msg = expectMsgType[Extract]

    msg.amount should be(100)
  }
}
