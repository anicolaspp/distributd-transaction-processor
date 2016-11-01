/**
  * Created by anicolaspp on 7/3/16.
  */

package com.nico.ClusterSingleton

import akka.actor._
import akka.cluster.routing.{ClusterRouterGroupSettings, ClusterRouterGroup}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings}
import akka.routing.{ConsistentHashingGroup, RoundRobinGroup}
import com.nico.actors.BankManager
import com.typesafe.config.ConfigFactory


object ClusterSingletonApp {
  def main(args: Array[String]) {

    val port = args(1).toInt
    val configuration = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString("akka.cluster.roles=[banker]"))
      .withFallback(ConfigFactory.load())

    val system = ActorSystem("TransactionCluster", configuration)

    val numberOfAccounts = args(0).toInt
    val accounts = (0 to numberOfAccounts).map (_.toString).toSet

    system.actorOf(ClusterSingletonManager.props(
      singletonProps = BankManager.props(accounts.toList),
      terminationMessage = PoisonPill,
      settings = ClusterSingletonManagerSettings(system)
    ))

    readLine()
  }
}









