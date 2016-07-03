/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence

import java.io.{File, PrintWriter}
import java.nio.file.{Paths, Files}

import org.joda.time.DateTime

import scala.io.Source
import scala.util.Try

trait AccountStorageModule {

  val accountStorage: AccountStorage

  trait AccountStorage {
    def update(account: Account): Account

    def get(accountId: String): Account
  }
}

trait AccountStorageModuleImp extends AccountStorageModule {

  class AccountStorageImp(path: String)
    extends AccountStorage {

    override def update(account: Account): Account = {
      val file = new File(path + "/" + account.id)

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

      get(account.id)
    }

    override def get(accountId: String): Account = {
      val optionAccount = Try {
        val file =new File(path + "/")
          .listFiles
          .filter(_.isFile)
          .filter(_.getName == accountId)(0)

        val content = io.Source.fromFile(file).mkString.split(" ")

        Account(content(0), content(1).toDouble)

      }.toOption

      optionAccount.fold(createAccount(accountId))(x => x)
    }

    def createAccount(account: String): Account = {
      val pw = new PrintWriter(
        {
          val f = new File(path + "/" + account)
          f.createNewFile()

          val dir = new File(f.getPath + ".history")
          dir.mkdir()

          f
        }
      )

      pw.write(account + " 0")
      pw.flush()


      get(account)
    }
  }
}

class AccountStorage(path: String) extends AccountStorageModuleImp {
  override val accountStorage: AccountStorage = new AccountStorageImp(path)
}


object AccountStorage {
  def apply(path: String): AccountStorage =
    new AccountStorage(path)
}





