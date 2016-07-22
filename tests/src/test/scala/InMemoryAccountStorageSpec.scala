/**
  * Created by anicolaspp on 7/6/16.
  */

import com.nico.persistence.storage.InMemoryAccountStorage
import com.nico.persistence.Account
import org.scalatest.{FlatSpec, Matchers}

class InMemoryAccountStorageSpec extends FlatSpec with Matchers with InMemoryAccountStorage {
  it should "init with a balance" in {
    val storage = this.storage

    storage.get("101") should be (Account("101", 0))
  }

  it should "update" in {
    val storage = this.storage

    val account = storage.update(Account("101", 100))

    account.id should be ("101")
    account.balance should be (100)

    storage.get("101") should be (account)
  }

  it should "manage multi accounts" in {
    val storage = this.storage

    (1 to 100) foreach { i =>
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