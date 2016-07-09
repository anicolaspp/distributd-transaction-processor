/**
  * Created by anicolaspp on 7/3/16.
  */

import java.io.File

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.nico.actors.TransactionManagerActor
import com.nico.actors.TransactionManagerActor._
import com.nico.persistence.{TransactionManager, Account}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike}

import scala.util.Random

class TransactionManagerActorSpec extends TestKit(ActorSystem("FlatSpec"))
    with FlatSpecLike
    with ImplicitSender
    with BeforeAndAfterAll {

  def beforeEach: String =  Math.abs(Random.nextInt()).toString

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)

  }

  it should "get account info async" in {

    val accId = beforeEach

    val actor = system.actorOf(TransactionManagerActor.props(TransactionManager.inMemory(accId)))

    actor ! AccountInfo()

    expectMsg(AccountInfoResult(Account(accId, 0)))
  }

  it should "deposit" in {
    val accId = beforeEach

    val actor = system.actorOf(TransactionManagerActor.props(TransactionManager.inMemory(accId)))

    actor ! Deposit(200)

    expectMsg(AccountInfoResult(Account(accId, 200)))
  }

  it should "not extract if low balance" in {
    val accId = beforeEach

    val actor = system.actorOf(TransactionManagerActor.props(TransactionManager.inMemory(accId)))

    actor  ! Deposit (200)
    expectMsg(AccountInfoResult(Account(accId, 200)))

    actor ! Extract(300)
    expectMsg(ExtractResult(false, Account(accId, 200)))
  }

  it should "successfully extract"  in {
    val accId = beforeEach

    val actor = system.actorOf(TransactionManagerActor.props(TransactionManager.inMemory(accId)))

    actor  ! Deposit (200)
    expectMsg(AccountInfoResult(Account(accId, 200)))

    actor ! Extract(150)
    expectMsg(ExtractResult(true, Account(accId, 50)))
  }
}





