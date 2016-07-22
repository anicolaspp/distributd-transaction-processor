/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence.storage

import com.nico.persistence.Account


trait StorageComponent { this: PathProviderComponent =>

  val storage: Storage

  trait Storage {
    def update(account: Account): Account

    def get(accountId: String): Account
  }
}














