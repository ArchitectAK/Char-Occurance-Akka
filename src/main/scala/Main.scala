import akka.Master
import akka.actor.{ActorRef, ActorSystem, Props}

import scala.io.StdIn

object Main extends App {

  println("[INFO] System creation : akka-counter-scala")
  val system: ActorSystem = ActorSystem("akka-counter-scala")

  var textFiles = Map(1 -> "few-sentences.txt", 2 -> "small-text.txt", 3 -> "thirdFile.txt")
  var choiceTextFile: Int = 1
  var numberOfWorkers: Int = 0

  println("Counting the number of occurrences of each letter")

  try {
    println("What text do you want to analyze ? (default 1)")
    for ((key,value) <- textFiles) printf("\t%s) %s\n", key, value)
    this.choiceTextFile = StdIn.readInt()
    if(this.choiceTextFile < 1 || this.choiceTextFile > textFiles.size)
      throw new NumberFormatException
  } catch {
    case e: NumberFormatException => {
      println("[ERROR] Incorrect value, we have taken the default choice")
      this.choiceTextFile = 1
    };
  }
  try {
    println("How many workers do you want (default 5) ?")
    this.numberOfWorkers = StdIn.readInt()
  } catch {
    case e: NumberFormatException => {
      println("[ERROR] Incorrect value, we have taken the default choice")
      this.numberOfWorkers = 5
    };
  }

  println("[INFO] Creation & configuration of the master")
  val master: ActorRef = system.actorOf(Props(new Master(this.numberOfWorkers)))

  master ! textFiles(choiceTextFile)
}