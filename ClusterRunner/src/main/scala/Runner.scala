/**
  * Created by nperez on 7/15/16.
  */


package com.nico.runner

import com.nico.ClusterPublisher.ClusterPublisherApp
import com.nico.DistributedSubscriber.DistributedSubscriberApp


import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object Runner extends App {

  //val jsonConfig = args(0)

  // parse config and start nodes


  Future { DistributedSubscriberApp
    .main(List(10, 9090).map(_.toString).toArray)}.map {_ =>

    ClusterPublisherApp
      .main(List(10, 9091, 1000000).map(_.toString).toArray)
  }


  readLine()
}
