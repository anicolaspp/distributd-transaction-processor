/**
  * Created by anicolaspp on 7/6/16.
  */

import com.nico.persistence.{Account, JournalAccountStorage}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}


class JournalSpec extends FlatSpec
  with Matchers
  with JournalAccountStorage
  with BeforeAndAfterAll {
  override val pathProvider: PathProvider = new PathProvider {
    override def path: String = "/"
  }

  override def beforeAll = {
    clean()
  }

  it should "init with a balance" in {
    val storage = this.storage

    storage.get("101") should be (Account("101", 0))
  }

  it should "update" in {
    val storage = this.storage

    val account = storage.update(Account("5", 100))

    account.id should be ("5")
    account.balance should be (100)

    storage.get("5") should be (account)
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

  it should "return history" in {

    val storage = this.storage

    clean()

    (1 to 50) foreach { i =>
      storage.update(Account("7", i))
    }

    val s = history("7")

    s.length should be(50)

    storage.get("7").balance should be(50)

    val zippedHistory = s.zipWithIndex

    zippedHistory.forall {
      case (acc, i) =>  acc.balance == i
    } should be (true)
  }
}
