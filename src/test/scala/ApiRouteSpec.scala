/**
  * Created by nperez on 7/6/16.
  */

package com.nico
package test

import akka.http.scaladsl.server.RouteResult.Rejected
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.nico.http.{ApiReportResources, ApiTransactionResources}
import com.nico.persistence.Account
import org.scalatest.{Matchers, FlatSpec}
import akka.http.scaladsl.model.StatusCodes._

class ApiRouteSpec extends FlatSpec with Matchers with ScalatestRouteTest with
  ApiTransactionResources with
  ApiReportResources {
  it should "return a greeting for GET" in {

    Get("/transactions") ~> transactionRoutes ~> check {
      responseAs[String] should be ("This is a nice get")
    }
  }

  it should "not return a greeting for GET on wrong path" in {

    Get("/transactions/abc") ~> transactionRoutes ~> check{
      handled should be (false)
    }
  }

  it should "return account balance" in {
    Get("/accounts?account=100") ~> reportRoutes ~> check {
      responseAs[String] should be (Account("100", 0).toString)
    }
  }
}
