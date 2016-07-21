/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence

import java.io.{File, PrintWriter}
import cats.data.{WriterT, Writer}
import org.joda.time.DateTime
import scala.io.Source
import scala.util.Try


trait PathProviderComponent {
  val pathProvider: PathProvider

  trait PathProvider {
    def path: String
  }
}

trait StorageComponent { this: PathProviderComponent =>

  val storage: Storage

  trait Storage {
    def update(account: Account): Account

    def get(accountId: String): Account
  }
}


trait AccountStorage extends StorageComponent with PathProviderComponent {
  override val storage: Storage = new Storage {

      override def update(account: Account): Account = {
        val file = new File(pathProvider.path + "/" + account.id)

        if (file.exists) {
          val stamp = file.getPath + ".history/" + DateTime.now().toString()
          val content = Source.fromFile(file).getLines().mkString
          val mp = new PrintWriter(stamp)
          mp.write(content)
          mp.flush()

          file.delete()
        }

        val pw = new PrintWriter(file)

        pw.write(account.id + " " + account.balance)
        pw.flush()
        pw.close()


        get(account.id)
      }

      override def get(accountId: String): Account = {
        val optionAccount = Try {
          val file =new File(pathProvider.path + "/")
            .listFiles
            .filter(_.isFile)
            .filter(_.getName == accountId)(0)

          val fcontent = io.Source.fromFile(file)

          val content = fcontent.mkString.split(" ")

          fcontent.close()


          Account(content(0), content(1).toDouble)

        }.toOption

        optionAccount.fold(createAccount(accountId))(x => x)
      }

      def createAccount(account: String): Account = {
        val pw = new PrintWriter(
          {
            val f = new File(pathProvider.path + "/" + account)
            f.createNewFile()

            val dir = new File(f.getPath + ".history")
            dir.mkdir()


            f
          }
        )

        pw.write(account + " 0")
        pw.flush()
        pw.close()

        get(account)
      }
    }
}


object AccountStorage {
  def apply(storageLocation: String): AccountStorage = new AccountStorage {
    override val pathProvider: PathProvider = new PathProvider {
      override def path: String = storageLocation
    }
  }
}


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