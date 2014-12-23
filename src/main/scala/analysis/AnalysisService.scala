package analysis

import akka.actor.{Props, Actor}
import akka.event.Logging
import com.sksamuel.scrimage.Image
import spray.json.DefaultJsonProtocol
import spray.routing.RequestContext

import scala.concurrent.Future
import scala.util.{Success, Failure}

import java.io.{ByteArrayInputStream=>JBAIS}

object AnalysisServiceProtocol extends DefaultJsonProtocol {
  import AnalysisService.{Analysed, NotAnalysed}
  implicit val analysedFormat = jsonFormat1(Analysed)
  implicit val notAnalysedFormat = jsonFormat1(NotAnalysed)
}

object AnalysisService{
  case class Analyse(img: JBAIS)
  case class Analysed(data: Seq[Seq[Int]])
  case class NotAnalysed(error: String)
}

class AnalysisService(ctx: RequestContext) extends Actor {
  import AnalysisService._
  import AnalysisServiceProtocol._
  import ImageTransformer._
  import StreamConverter._
  import ImageConverter._
  import spray.httpx.SprayJsonSupport._

  implicit val system = context.system
  import system.dispatcher
  val imageTransformer = system.actorOf(Props[ImageTransformer])
  val streamConverter = system.actorOf(Props[StreamConverter])
  val imageConverter = system.actorOf(Props[ImageConverter])
  val log = Logging(system, getClass)

  def receive = {
    case Analyse(imageStream) =>
      log.info("Trying to analyse image: {}",imageStream)
      analyse(imageStream)
    case ImageFromStream(image) =>
      log.info("Converted image: {}",image)
      transform(image)
    case Transformed(image) =>
      log.info("Transformed image: {}",image)
      imageToMatrix(image)
    case MatrixFromImage(mx) =>
      log.info("Received matrix: {}",mx)
      mx.onComplete{
        case Success(mx) => ctx.complete(Analysed(mx.data))
        case Failure(err) => completeWithError(err.toString)
      }
    case _ =>  completeWithError("Unable to analyse image.")
  }

  def analyse(stream: JBAIS) = streamConverter ! StreamToImage(stream)

  def transform(image: Future[Image]) = imageTransformer ! Transform(image)

  def imageToMatrix(image: Future[Image]) = imageConverter ! ImageToMatrix(image)

  def completeWithError(error: String): Unit = ctx.complete(NotAnalysed(error.toString))

}