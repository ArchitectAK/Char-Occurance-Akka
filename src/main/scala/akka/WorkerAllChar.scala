package akka

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.collection.mutable


object WorkerAllChar {
  def props(masterActor: ActorRef): Props = Props(new WorkerAllChar(masterActor))
}

class WorkerAllChar(masterActor: ActorRef) extends Actor with ActorLogging {
  println("Worker all started")

  def receive = {
    case line: String => {
      val numbers: mutable.Map[Char, Int] = mutable.Map.empty[Char, Int].withDefaultValue(0)
      line.toList.foreach(rawWord => {
        val word = rawWord
        numbers(word) += 1
      })
      masterActor ! numbers
    }
  }
}