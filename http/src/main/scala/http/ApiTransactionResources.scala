/**
  * Created by nperez on 7/6/16.
  */

package com.nico.http

import akka.http.javadsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import com.nico.persistence.Account
import com.typesafe.config.{ConfigValueFactory, ConfigFactory}

import scala.util.Try


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
