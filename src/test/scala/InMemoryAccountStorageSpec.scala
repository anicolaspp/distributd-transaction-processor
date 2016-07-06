/**
  * Created by anicolaspp on 7/6/16.
  */

package com.nico
package test

import com.nico.persistence.{Account, InMemoryAccountStorage}
import org.scalatest.{Matchers, FlatSpec}

class InMemoryAccountStorageSpec extends FlatSpec with Matchers {
  it should "init with a balance" in {
    val storage = InMemoryAccountStorage().storage

    storage.get("101") should be (Account("101", 0))
  }

  it should "update" in {
    val storage = InMemoryAccountStorage().storage

    val account = storage.update(Account("101", 100))

    account.id should be ("101")
    account.balance should be (100)

    storage.get("101") should be (account)
  }

  it should "manage multi accounts" in {
    val storage = InMemoryAccountStorage().storage

    (1 to 100) map { i =>
      if (i % 2 == 0) {
        storage.update(Account(i.toString, 100))
      }
      else {
        storage.update(Account(i.toString, 200))
      }
    }

    (1 to 100) foreach { i =>
      if (i % 2 == 0) {
        storage.get(i.toString).balance should be (100)
      }
      else {
        storage.get(i.toString).balance should be (200)
      }
    }
  }
}
