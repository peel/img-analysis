import java.io.{ByteArrayInputStream => JBAIS}

import akka.actor.Actor
import akka.event.Logging
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.filter.{ThresholdFilter, PixelateFilter}
import spray.json.DefaultJsonProtocol
import spray.routing.RequestContext
import scala.concurrent.Future
import scala.util.{Failure, Success}

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
  import spray.httpx.SprayJsonSupport._
  import AnalysisServiceProtocol._

  implicit val system = context.system
  import system.dispatcher
  val log = Logging(system, getClass)

  def receive = {
    case Analyse(image) =>
      log.info("Trying to analyse image: {}",image)
      analyse(image)
      context.stop(self)
  }

  def analyse(imgStream: JBAIS) = {
    val image = Future(Image(imgStream))
    log.info("Trying to transform image: {}",image)
    transform(image).map(completeWithMatrix)
  }

  def transform(image: Future[Image]) = {
    image.map(_.filter(PixelateFilter(100), ThresholdFilter(127)).scale(0.1))
  }

  def completeWithError(error: Throwable): Unit = {
    ctx.complete(NotAnalysed(error.toString))
  }

  def completeWithMatrix(img: Image): Unit = {
    Future {
      MatrixUtils.fromImage(img)
    } onComplete {
      case Success(matrix) => ctx.complete(Analysed(matrix.data))
      case Failure(error) => completeWithError(error)
    }
  }
}



