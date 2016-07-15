/**
  * Created by anicolaspp on 7/4/16.
  */

package com.nico.DistributedSubscriber

import akka.actor.ActorSystem
import com.nico.actors.AccountSubscriber
import com.nico.persistence.TransactionManager
import com.typesafe.config.ConfigFactory
import kamon.Kamon

object DistributedSubscriberApp {
  def main(args: Array[String]) {

    //Kamon.start()

    val port = args(1).toInt

    println(port)

    val configuration = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString("akka.cluster.roles=[banker]"))
      .withFallback(ConfigFactory.load())

    val system = ActorSystem("TransactionCluster", configuration)

    val numberOfAccounts = args(0).toInt
    val accounts = (0 to numberOfAccounts).map (_.toString).toSet

    (0 to 100).map { i =>
      system.actorOf(AccountSubscriber.props(TransactionManager.onDisk((i % numberOfAccounts).toString, "/Users/nperez/accounts")))
    }

    readLine()
  }
}
