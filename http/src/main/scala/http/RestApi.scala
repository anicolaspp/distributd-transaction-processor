/**
  * Created by nperez on 7/6/16.
  */


package com.nico.http


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow

import scala.io.StdIn

object RestApi extends App
  with ApiTransactionResources
  with ApiReportResources
  with ApiConfigurationLoader {

  val configuration = getConfiguration(args(0).toInt)

  implicit val system: ActorSystem = ActorSystem("TransactionCluster", configuration)
  implicit val materializer = ActorMaterializer()

  val api = transactionRoutes ~ reportRoutes

  val binding = Http().bindAndHandle(api, "localhost", args(1).toInt)

  StdIn.readLine()

  binding.map(_.unbind()).map(_ => system.terminate())
}




