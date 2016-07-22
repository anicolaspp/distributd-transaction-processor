/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence.storage

import java.io.{File, PrintWriter}

import cats.data.Writer
import com.nico.persistence.Account
import org.joda.time.DateTime

import scala.io.Source
import scala.util.Try

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





