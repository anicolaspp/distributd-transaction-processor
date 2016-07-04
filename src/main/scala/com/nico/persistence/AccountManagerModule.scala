/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence

trait AccountManagerComponent {
  val manager: AccountManager

  trait AccountManager {
    def accountInfo: Account
    
    def deposit(amount: Double): Account

    def extract(amount: Double): (Boolean, Account)
  }
}

class TransactionManager (account: String) extends AccountManagerComponent{
  this: AccountStorage  =>
  override val manager = new AccountManager {
    override def deposit(amount: Double): Account = {
      val account = accountInfo

      storage.update(Account(account.id, account.balance + amount))
    }

    override def extract(amount: Double): (Boolean, Account) = {
      val account = accountInfo

      if (account.balance < amount) {
        (false, account)
      }
      else {
        (true, storage.update(Account(account.id, account.balance - amount)))
      }
    }

    override def accountInfo: Account = storage.get(account)
  }
  override val pathProvider = new PathProvider {
    override def path: String = "/Users/anicolaspp/accounts/"
  }
}


class InMemoryTransactionManager(acc: String) extends TransactionManager(acc) with InMemoryAccountStorage






