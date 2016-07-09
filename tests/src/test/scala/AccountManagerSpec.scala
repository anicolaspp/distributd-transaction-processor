/**
  * Created by anicolaspp on 7/2/16.
  */

import com.nico.persistence.TransactionManager
import org.scalatest.{FlatSpec, Matchers}

class AccountManagerSpec extends FlatSpec with Matchers {
  "Account Manager" should "get account info" in {
    val manager =  TransactionManager.inMemory("100").manager

    manager.deposit(200)

    val account = manager.accountInfo

    account.id should be ("100")
    account.balance should be (200)
  }

  it should "deposit" in {
    val manager = TransactionManager.inMemory("100").manager

    val account = manager.deposit(10)

    account.id should be ("100")
    account.balance should be (10)
  }

  it should "successfully extract" in {
    val manager = TransactionManager.inMemory("100").manager

    manager.deposit(200)

    val (result, account) = manager.extract(100)

    result should be (true)
    account.id should be ("100")
    account.balance should be (100)
  }

  it should "not extract if low balance" in {
    val manager = TransactionManager.inMemory("100").manager

    val (result, account) = manager.extract(500)

    result should be (false)
    account.id should be ("100")
    account.balance should be (0)
  }
}




