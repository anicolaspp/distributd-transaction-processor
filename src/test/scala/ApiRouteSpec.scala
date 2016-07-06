/**
  * Created by nperez on 7/6/16.
  */

package com.nico
package test

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.nico.http.ApiResources
import org.scalatest.{Matchers, FlatSpec}

class ApiRouteSpec extends FlatSpec with Matchers with ScalatestRouteTest with ApiResources {
  it should "return a greeting for GET" in {

    Get("/transactions") ~> transactionRoutes ~> check {
      responseAs[String] should be ("This is a nice get")
    }
  }
}
