/**
  * Created by anicolaspp on 7/4/16.
  */
package com.nico.ClusterPublisher

import akka.actor.ActorSystem
import com.nico.actors.TickerTransactionPublisher
import com.typesafe.config.ConfigFactory

object ClusterPublisherApp {
  def main(args: Array[String]) {

//    val port = args(1)

    val configuration = ConfigFactory.load()

    val system = ActorSystem(configuration.getString("clustering.cluster.name"), configuration)

    val numberOfAccounts = configuration.getInt("accounts.total")
    val accounts = (0 to numberOfAccounts).map (_.toString)

//    val numberOfTransactions = args(2).toInt


//    val publisher = system.actorOf(TransactionPublisher.props())

//    println("number of transactions: " + numberOfTransactions)

//
//    (1 to numberOfTransactions) foreach {i =>
//
//      val selectedAccount = Random.nextInt() % numberOfAccounts
//
//      val transaction = Transaction(selectedAccount.toString, Random.nextInt(100), DateTime.now())
//
//      publisher ! transaction
//
//      println(s"iteration $i transaction $transaction")
//    }


//    system.actorOf(TickerTransactionPublisher.props(accounts.toList))

    readLine()
  }
}