package actor

import akka.actor._
import akka.routing._

object ProducerConsumer extends App  {
  val system = ActorSystem("ProducerConsumer")
  val producerActor = system.actorOf(Props[MessagesProducer])
  producerActor ! "start"
}

abstract class TerminatingProducer extends Actor {
  val maxMessages = 1000
  val startTime = System.currentTimeMillis()

  def terminate() = {
    val finishTime = System.currentTimeMillis()
    val totalTime = finishTime - startTime
    println(s"Terminated after $maxMessages messages. $totalTime ms")
    context.system.shutdown()
  }

  def terminated: Receive = {
    case _ ⇒
  }

}

class ActorProducer extends TerminatingProducer {
  def sendWork(messageId: Int) = {
    val worker = context.system.actorOf(Props[WorkerActor])
    worker ! messageId
  }

  def receive = {
    case start: String =>
      sendWork(0)

    case Result(id: Int, _) =>
      if (id < maxMessages) {
        sendWork(id + 1)
      } else {
        terminate()
      }
  }

}

class MessagesProducer extends TerminatingProducer {
  var resultCollection = List.empty[Result]

  def receive = {
    case start: String =>
      val worker = context.actorOf(RoundRobinPool(5).props(Props[WorkerActor]))
      for (i <- 0 to maxMessages) {
        worker ! i
      }

    case r: Result =>
      resultCollection = resultCollection :+ r
      if (resultCollection.size >= maxMessages) {
        context become terminated
        terminate()
      }
  }
}

case class Result(id: Int, value: Int)
class WorkerActor extends Actor {
  def receive = {
    case id: Int =>
      val result = SlowPrimeCalculator.calculate()
      sender ! Result(id, result)
  }
}
