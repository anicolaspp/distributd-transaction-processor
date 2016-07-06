/**
  * Created by nperez on 7/6/16.
  */

package com.nico.http

import akka.http.scaladsl.server.Directives

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
