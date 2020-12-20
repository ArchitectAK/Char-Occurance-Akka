package akka

import akka.actor.{Actor, ActorLogging}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

import scala.collection.mutable
import scala.io.Source

class Master(numberOfWorkers: Int) extends Actor with ActorLogging {

  var filename : String = ""
  var numberOfOccurrences : Int = 0
  var numbersOfCharOccurrences: mutable.Map[Char, Int] = mutable.Map.empty[Char, Int].withDefaultValue(0)
  var numberOfLines : Int = 0
  var numberOfResponses : Int = 0

  var router: Router = {
    val routees = Vector.fill(numberOfWorkers) {
      val worker = WorkerAllChar.props(self)
      val r = context.actorOf(worker)
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive: Receive = {
    case filename: String => {
      this.filename = filename

      Source
        .fromResource(filename)
        .getLines
        .foreach(line => {
          router.route(line, sender())
        })

      numberOfLines = Source
        .fromResource(filename)
        .getLines
        .length
    }

    case number: Int => {
      numberOfOccurrences += number
      numberOfResponses += 1

      if (numberOfResponses == numberOfLines) {
        println(s"There are a total of ${this.numberOfOccurrences} times in the file ${filename}")
      }
    }

    case numbers: mutable.Map[Char, Int] => {
      numberOfResponses += 1

      for ((key, value) <- numbers) {
        this.numbersOfCharOccurrences(key) += value
      }

      if (numberOfResponses == numberOfLines) {
        println(this.numbersOfCharOccurrences)
      }
    }
  }
}