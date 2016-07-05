/**
  * Created by anicolaspp on 7/4/16.
  */

package com.nico.simulation.actors.cluster.sub

import akka.actor.ActorSystem
import com.nico.simulation.actors.cluster.AccountSubscriber
import com.typesafe.config.ConfigFactory

object DistributedSub {
  def main(args: Array[String]) {
    val port = args(1).toInt

    val configuration = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString("akka.cluster.roles=[banker]"))
      .withFallback(ConfigFactory.load())

    val system = ActorSystem("TransactionCluster", configuration)

    val numberOfAccounts = args(0).toInt
    val accounts = (0 to numberOfAccounts).map (_.toString).toSet

    (0 to 100).map { i =>
      system.actorOf(AccountSubscriber.props((i % numberOfAccounts).toString))
    }

    readLine()
  }
}
