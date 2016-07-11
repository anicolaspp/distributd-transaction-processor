/**
  * Created by anicolaspp on 7/9/16.
  */

package simulation.actors.cluster.replicator

import akka.actor.{Actor, ActorLogging}
import akka.cluster.ddata.{DistributedData, ORSet, ORSetKey, Replicator}
import akka.cluster.ddata.Replicator._
import scala.concurrent.duration._

//class DistributedStorage extends Actor with ActorLogging {
//
//
//  val replicator = DistributedData(context.system).replicator
//
//  val key = ORSetKey[String]("key")
//
//  override def receive: Receive = {
//    case _  =>  {
//      replicator ! Update(key, ORSet.empty[String], WriteMajority(5.seconds))(_ + "hello")
//    }
//  }
//}
