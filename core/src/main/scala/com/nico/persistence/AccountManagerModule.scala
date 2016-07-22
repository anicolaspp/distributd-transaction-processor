/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence

import com.nico.persistence.storage.{InMemoryAccountStorage, AccountStorage, StorageComponent, PathProviderComponent}

trait AccountManagerComponent { this: StorageComponent =>
  val manager: AccountManager

  trait AccountManager {
    def accountInfo: Account

    def deposit(amount: Double): Account = {
      val account = accountInfo

      storage.update(Account(account.id, account.balance + amount))
    }

    def extract(amount: Double): (Boolean, Account) = {
      val account = accountInfo

      if (account.balance < amount) {
        (false, account)
      }
      else {
        (true, storage.update(Account(account.id, account.balance - amount)))
      }
    }
  }
}


trait TransactionManager extends AccountManagerComponent with StorageComponent with PathProviderComponent

object TransactionManager {

  def onDisk(account: String, root: String): TransactionManager = new TransactionManager with AccountStorage {
    override val pathProvider: PathProvider = new PathProvider {
      override def path: String = root
    }
    override val manager: AccountManager = new AccountManager {
        override def accountInfo: Account = storage.get(account)
      }
  }

  def inMemory(account: String): TransactionManager = new TransactionManager with InMemoryAccountStorage {
    override val manager: AccountManager = new AccountManager {
      override def accountInfo: Account = storage.get(account)
    }
  }
}






