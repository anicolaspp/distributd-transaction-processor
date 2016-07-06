/**
  * Created by anicolaspp on 7/3/16.
  */

package com.nico.actors
package tests

import java.io.File

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.nico.actors.TransactionManagerActor._
import com.nico.persistence.Account
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike}

import scala.util.Random

class TransactionManagerActorSpec extends TestKit(ActorSystem("FlatSpec"))
    with FlatSpecLike
    with ImplicitSender
    with BeforeAndAfterAll {

  val storagePath = "/Users/anicolaspp/accounts/"

  def beforeEach: String =  Math.abs(Random.nextInt()).toString

  def afterEach(accId: String) = {
    val file = new File(storagePath + accId)
    file.delete()

    def delete(file: File) {
      if (file.isDirectory)
        Option(file.listFiles).map(_.toList).getOrElse(Nil).foreach(delete(_))
      file.delete
    }

    val dir = new File(storagePath + accId + ".history")
    delete(dir)
  }

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)

  }

  it should "get account info async" in {

    val accId = beforeEach

    val actor = system.actorOf(TransactionManagerActor.props(accId))

    actor ! AccountInfo()

    expectMsg(AccountInfoResult(Account(accId, 0)))

    afterEach(accId)
  }

  it should "deposit" in {
    val accId = beforeEach

    val actor = system.actorOf(TransactionManagerActor.props(accId))

    actor ! Deposit(200)

    expectMsg(AccountInfoResult(Account(accId, 200)))

    afterEach(accId)
  }

  it should "not extract if low balance" in {
    val accId = beforeEach

    val actor = system.actorOf(TransactionManagerActor.props(accId))

    actor  ! Deposit (200)
    expectMsg(AccountInfoResult(Account(accId, 200)))

    actor ! Extract(300)
    expectMsg(ExtractResult(false, Account(accId, 200)))

    afterEach(accId)
  }

  it should "successfully extract"  in {
    val accId = beforeEach

    val actor = system.actorOf(TransactionManagerActor.props(accId))

    actor  ! Deposit (200)
    expectMsg(AccountInfoResult(Account(accId, 200)))

    actor ! Extract(150)
    expectMsg(ExtractResult(true, Account(accId, 50)))

    afterEach(accId)
  }
}





