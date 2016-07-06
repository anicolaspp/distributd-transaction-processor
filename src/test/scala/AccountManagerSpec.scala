/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence
package test


import org.scalatest.{FlatSpec, Matchers}

class AccountManagerSpec extends FlatSpec with Matchers {
  "Account Manager" should "get account info" in {
    val manager =  new InMemoryTransactionManager("100").manager

    manager.deposit(200)

    val account = manager.accountInfo

    account.id should be ("100")
    account.balance should be (200)
  }

  it should "deposit" in {
    val manager = new InMemoryTransactionManager("100").manager

    val account = manager.deposit(10)

    account.id should be ("100")
    account.balance should be (10)
  }

  it should "successfully extract" in {
    val manager = new InMemoryTransactionManager("100").manager

    manager.deposit(200)

    val (result, account) = manager.extract(100)

    result should be (true)
    account.id should be ("100")
    account.balance should be (100)
  }

  it should "not extract if low balance" in {
    val manager = new InMemoryTransactionManager("100").manager

    val (result, account) = manager.extract(500)

    result should be (false)
    account.id should be ("100")
    account.balance should be (0)
  }
}

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


