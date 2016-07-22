/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence.storage

import com.nico.persistence.Account


trait InMemoryAccountStorage extends StorageComponent with PathProviderComponent {
  override val storage: Storage = new Storage {

    var accounts = Map[String, Account]()

    def newAccountWithId(accountId: String): Account = {
      accounts = accounts + (accountId -> Account(accountId, 0))

      accounts(accountId)
    }

    override def get(accountId: String): Account =
      accounts.get(accountId).fold(newAccountWithId(accountId))(x => x)

    override def update(account: Account): Account = {
      accounts = accounts.updated(account.id, account)

      account
    }
  }

  override val pathProvider: PathProvider = new PathProvider {
    override def path: String = "/"
  }
}

object InMemoryAccountStorage {
  def apply(): InMemoryAccountStorage = new InMemoryAccountStorage {}
}



