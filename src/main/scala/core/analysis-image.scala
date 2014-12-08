package core

import akka.actor.Actor

case class Analysis()

object ImageAnalysisActor{
  case class AnalyseImage(image: Image)
  case class ImageAnalysed(analysis: Analysis)
  case class ImageAnalysis()
  case object ImageNotAnalysed
}
class ImageAnalysisActor extends Actor {
  import core.ImageAnalysisActor._

  override def receive = {
    case AnalyseImage(image) =>
      sender ! new ImageAnalysed(analyse(image))
  }

  def analyse(image: Image): Analysis = Analysis()
}
