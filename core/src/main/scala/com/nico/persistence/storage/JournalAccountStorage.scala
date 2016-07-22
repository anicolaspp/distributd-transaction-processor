/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence.storage

import cats.data.Writer
import com.nico.persistence.Account


trait JournalAccountStorage extends StorageComponent with PathProviderComponent {
  import JournalAccountStorage._

  private [JournalAccountStorage] var journal = Map[String, Writer[List[Account], Account]]()

  override val storage: Storage = new Storage {

    override def update(account: Account): Account = {
      val a = get(account.id)
      val x = journal.get(a.id)
      val current = x.get

      journal = journal.updated(account.id, current ~> account)

      get(account.id)
    }

    override def get(accountId: String): Account =
      journal.get(accountId).fold(newAccountWithId(accountId))(x => x.run._2)

    def newAccountWithId(accountId: String): Account = {
      journal = journal + (accountId -> new Writer(List.empty, Account(accountId, 0)))

      get(accountId)
    }
  }

  def history(id: String): List[Account] =
    journal
      .get(storage.get(id).id)
      .fold(List.empty[Account])(x => x.written)

  def clean(): Storage = {
    journal = journal.empty

    storage
  }
}

object JournalAccountStorage {

  implicit class toAccountArrow(current: Writer[List[Account], Account]) {
    def ~>(next: Account): Writer[List[Account], Account] =
      new Writer(current.written ++ List(current.value), next)
  }

}
