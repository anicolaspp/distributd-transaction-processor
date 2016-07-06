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
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

object RestApi extends App
  with ApiTransactionResources
  with ApiReportResources {

  val configuration = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=9070")
    .withFallback(ConfigFactory.parseString("akka.cluster.roles=[api]"))
    .withFallback(ConfigFactory.load())

  implicit val system = ActorSystem("TransactionCluster", configuration)
  implicit val materializer = ActorMaterializer()

  val api = transactionRoutes ~ reportRoutes

  val binding = Http().bindAndHandle(api, "localhost", 9071)

  StdIn.readLine()

//  binding.flatMap(_.unbind())
}




