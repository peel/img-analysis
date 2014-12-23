package analysis

import java.io.{ByteArrayInputStream => JBAIS}

import akka.actor.Actor
import com.sksamuel.scrimage.Image

import scala.concurrent.Future

object StreamConverter{
  case class StreamToImage(stream: JBAIS)
  case class ImageFromStream(img: Future[Image])
  case object NotConverted
}

class StreamConverter extends Actor{
  import analysis.StreamConverter._
  import context.dispatcher

  def receive = {
    case StreamToImage(stream) => sender ! ImageFromStream(imageFrom(stream))
    case _ => sender ! NotConverted
  }

  def imageFrom(stream:JBAIS): Future[Image] = {
    Future(Image(stream))
  }
}