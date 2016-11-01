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

    Kamon.start()

//    val port = args(1).toInt

//    println(port)

//    ConfigFactory

    val configuration = ConfigFactory.load()

    val system = ActorSystem(configuration.getString("clustering.cluster.name"), configuration)

//    val numberOfAccounts = args(0).toInt
//    val accounts = (0 to numberOfAccounts).map (_.toString).toSet
//
//    (0 to 10).map { i =>
//      system.actorOf(AccountSubscriber.props(TransactionManager.onDisk((i % numberOfAccounts).toString, "/Users/anicolaspp/accounts")))
//    }

    readLine()
  }
}
