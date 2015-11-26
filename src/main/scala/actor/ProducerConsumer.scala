package actor

import akka.actor._
import akka.util.Timeout

object ProducerConsumer extends App  {
  val system = ActorSystem("ProducerConsumer")
  val producerActor = system.actorOf(Props[ProducerActor], name = "ProducerActor")
  producerActor ! "start"
}

class ProducerActor extends Actor {
  var messageCount = 1
  val maxMessages = 1000
  val startTime = System.currentTimeMillis()

  def terminate() = {
    val finishTime = System.currentTimeMillis()
    val totalTime = finishTime - startTime
    println(s"Terminated after $maxMessages messages. $totalTime ms")
    context.system.shutdown()
  }

  def sendWork() = {
    val actor = context.system.actorOf(Props[WorkerActor])
    actor ! "calculate"
  }

  def receive = {
    case msg: String => sendWork()

    case msg: Int =>
      if (messageCount < maxMessages) {
        messageCount += 1
        sendWork()
      } else {
        terminate()
      }
  }
}

class WorkerActor extends Actor {
  def receive = {
    case msg: String =>
      val result = SlowPrimeCalculator.calculate()
      sender ! result
  }
}
