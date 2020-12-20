package akka

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object WorkerOnlyWord {
  def props(word: Char, masterActor: ActorRef): Props = Props(new WorkerOnlyWord(word, masterActor))
}

class WorkerOnlyWord(word: Char, masterActor: ActorRef) extends Actor with ActorLogging {
  println("Worker only started")

  def receive = {
    case line: String => {
      val number: Int = line.toList.count(_ == word)
      masterActor ! number
    }
  }
}