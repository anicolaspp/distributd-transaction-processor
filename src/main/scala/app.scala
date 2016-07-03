/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico

import com.nico.persistence
import com.nico.persistence.{Account, AccountStorage}

object app {
  def main(args: Array[String]) {

    val storage = AccountStorage("/Users/anicolaspp/accounts").accountStorage

    val account = storage.get("1")

    val updated = storage.update(Account("1", 500))

    println(account)
  }
}
