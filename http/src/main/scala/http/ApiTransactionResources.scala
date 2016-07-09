/**
  * Created by nperez on 7/6/16.
  */

package com.nico.http

import akka.http.javadsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import com.nico.persistence.Account
import com.typesafe.config.ConfigFactory

trait ApiTransactionResources extends Directives {

  //val transactionPublisher: TransactionPublisher

  def transactionRoutes = pathPrefix("transactions") {
    pathEnd {
      get {
        complete("This is a nice get")
      }
    }
  }
}

trait ApiReportResources extends Directives {

  def reportRoutes = pathPrefix("accounts") {
    parameters('account.as[String]) { acc =>
      get {
        complete(Account(acc, 0).toString)
      }
    }

  }
}

trait ApiConfigurationLoader {

  def getConfiguration(bindingPort: Int) = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$bindingPort")
    .withFallback(ConfigFactory.parseString("akka.cluster.roles=[api]"))
    .withFallback(ConfigFactory.load())
}