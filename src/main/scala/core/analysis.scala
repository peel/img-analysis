package core

import akka.actor.{Actor, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

case class Image(data: String, format: String)

object DataHandler{
  case class Analyse(data: Image)
  case class Analysed(analysis: Analysis)
  case object NotAnalysed
}

class DataHandler extends Actor {
  import core.DataHandler._
  import core.ImageAnalysisActor._

  val analyser = context.actorOf(Props[ImageAnalysisActor])
  implicit val timeout = Timeout(5 seconds)
  override def receive: Receive = {
    case Analyse(data)  => sender ! (analyser ? AnalyseImage(data)).mapTo[ImageAnalysed].map(img => Analysed(img.analysis)).recover{case _ => NotAnalysed}
    case _ => sender ! NotAnalysed
  }
}
