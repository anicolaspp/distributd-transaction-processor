/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence

import java.io.{File, PrintWriter}
import java.nio.file.{Paths, Files}

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

trait InMemoryAccountStorage extends AccountStorage {
  override val storage: Storage = new Storage {
    var storedAccount = Account("100", 200)

    override def get(accountId: String): Account = storedAccount

    override def update(account: Account): Account = {
      storedAccount = Account(storedAccount.id, account.balance)

      storedAccount
    }
  }
}






//trait AccountStorageModuleImp extends AccountStorageModule {
//
//  class AccountStorageImp(path: String)
//    extends AccountStorage {
//
//    override def update(account: Account): Account = {
//      val file = new File(path + "/" + account.id)
//
//      if (file.exists) {
//        val stamp = file.getPath + ".history/" + DateTime.now().toString()
//        val content = Source.fromFile(file).getLines().mkString
//        val mp = new PrintWriter(stamp)
//        mp.write(content)
//        mp.flush()
//
//        file.delete()
//      }
//
//      val pw = new PrintWriter(file)
//
//      pw.write(account.id + " " + account.balance)
//      pw.flush()
//
//      get(account.id)
//    }
//
//    override def get(accountId: String): Account = {
//      val optionAccount = Try {
//        val file =new File(path + "/")
//          .listFiles
//          .filter(_.isFile)
//          .filter(_.getName == accountId)(0)
//
//        val content = io.Source.fromFile(file).mkString.split(" ")
//
//        Account(content(0), content(1).toDouble)
//
//      }.toOption
//
//      optionAccount.fold(createAccount(accountId))(x => x)
//    }
//
//    def createAccount(account: String): Account = {
//      val pw = new PrintWriter(
//        {
//          val f = new File(path + "/" + account)
//          f.createNewFile()
//
//          val dir = new File(f.getPath + ".history")
//          dir.mkdir()
//
//          f
//        }
//      )
//
//      pw.write(account + " 0")
//      pw.flush()
//
//
//      get(account)
//    }
//  }
//}


//trait PathProviderComponent {
//  val pathProvider: PathProvider
//
//  trait PathProvider {
//    def path: String = "/"
//  }
//
//}
//
//
//trait RepositoryComponent { this: PathProviderComponent =>
//
//  val repository: Repository
//
//  trait Repository {
//    def get(): List[String]
//  }
//
//}
//
//trait ServiceComponent { this: RepositoryComponent =>
//
//  val service: Service
//
//  class Service {
//    def someGet() = repository.get()
//  }
//}
//
//object InMemoryService
//  extends ServiceComponent with
//          RepositoryComponent with
//          PathProviderComponent {
//
//  override val service: InMemoryService.Service = new Service
//  override val repository: InMemoryService.Repository = new Repository {
//    override def get(): List[String] = List(pathProvider.path)
//  }
//  override val pathProvider: InMemoryService.PathProvider = new PathProvider {
//    override def path: String = "~/acounts"
//  }
//}



