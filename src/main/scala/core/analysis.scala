package core

import akka.actor.{Actor, Props}

case class Image(data: Image, format: String)

object DataHandler{
  case class Analyse(data: Image)
  case class Analysed(analysis: Analysis)
  case object NotAnalysed
}

class DataHandler extends Actor {
  import core.DataHandler._
  import core.ImageAnalysisActor._

  val analyser = context.actorOf(Props[ImageAnalysisActor])
  override def receive: Receive = {
    case Analyse(data)  => analyser ! AnalyseImage(data)
    case _ => sender ! NotAnalysed
  }
}
