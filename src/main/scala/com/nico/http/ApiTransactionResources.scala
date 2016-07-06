/**
  * Created by nperez on 7/6/16.
  */

package com.nico.http

import akka.http.javadsl.server.Route
import akka.http.scaladsl.server.Directives
import com.nico.persistence.Account

trait ApiTransactionResources extends Directives {

  //val transactionPublisher: TransactionPublisher

  def transactionRoutes = pathPrefix("transactions") {
    pathEnd {
//      post {
//        entity(as[Transaction]) { transaction =>
//
//          complete((StatusCodes.Accepted, "Transaction posted"))
//        }
//      } ~
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