/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence
package test


import org.scalatest.{FlatSpec, Matchers}

class AccountManagerSpec extends FlatSpec with Matchers {
  "Account Manager" should "get account info" in {
    val manager =  new InMemoryTransactionManager("100").manager

    val account = manager.accountInfo

    account.id should be ("100")
    account.balance should be (200)
  }

  it should "deposit" in {
    val manager = new InMemoryTransactionManager("100").manager

    val account = manager.deposit(10)

    account.id should be ("100")
    account.balance should be (210)
  }

  it should "successfully extract" in {
    val manager = new InMemoryTransactionManager("100").manager

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
    account.balance should be (200)
  }
}



