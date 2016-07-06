/**
  * Created by anicolaspp on 7/4/16.
  */
package com.nico.simulation.actors.cluster.publisher

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object ClusterPublisher {
  def main(args: Array[String]) {

    val port = args(1)

    val configuration = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString("akka.cluster.roles=[publisher]"))
      .withFallback(ConfigFactory.load())

    val system = ActorSystem("TransactionCluster", configuration)

    val numberOfAccounts = args(0).toInt
    val accounts = (0 to numberOfAccounts).map (_.toString)

    system.actorOf(TickerTransactionPublisher.props(accounts.toList))

    readLine()
  }
}
