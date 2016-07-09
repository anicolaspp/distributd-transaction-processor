/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.simulation.persistence

import com.nico.persistence._

import scala.util.Random

object app {
  def main(args: Array[String]) {
    val accId = Random.nextInt(100).toString

    val numerberOfIterations = 1000000

    val manager = new TransactionManager(accId) with AccountStorage



    (0 to numerberOfIterations).foreach{ _ =>
      val transactionAmount = Random.nextInt() % 100

      if (transactionAmount > 0) {
        manager.manager.deposit(transactionAmount)
      }
      else {
        manager.manager.extract(Math.abs(transactionAmount))
      }
    }
  }
}
