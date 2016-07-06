/**
  * Created by nperez on 7/6/16.
  */


package com.nico.http


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.nico.persistence.Transaction

import scala.io.StdIn

object RestApi extends App with ApiResources {
  //override val transactionPublisher: TransactionPublisher = _

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val api = transactionRoutes

  val binding = Http().bindAndHandle(api, "localhost", 9070)

  StdIn.readLine()

//  binding.flatMap(_.unbind())
}

trait ApiResources extends Directives {

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


