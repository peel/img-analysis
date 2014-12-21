import java.io.{ByteArrayInputStream => JBAIS}

import akka.actor.Actor
import akka.event.Logging
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.filter.{ThresholdFilter, PixelateFilter}
import spray.httpx.marshalling.Marshaller
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
  case class Analysed(data: Array[Int])
  case class NotAnalysed(error: String)
}

class AnalysisService(ctx: RequestContext) extends Actor {
  import AnalysisService._
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
    val responseFuture = image.map(_.filter(PixelateFilter(100),ThresholdFilter(127)).scale(0.1))
    responseFuture onComplete {
      case Success(processed) =>
        Future {
          Matrix(for (y <- 0 until processed.height) yield for(x<-0 until processed.width) yield processed.pixel(x,y))
        } onComplete {
          case Success(matrix) => ctx.complete(matrix.toString)
          case Failure(error) => completeWithError(error)
        }
      case Failure(error) => completeWithError(error)
    }
    def completeWithError(error: Throwable)(implicit ctx: RequestContext):Unit={
      ctx.complete(error.toString)
    }
  }
}

case class Matrix(data: Seq[Seq[Int]]){
  val height = data.size
  val width = data.head.size
  override def toString = data.map(_.mkString(";")).mkString("\n")
}
