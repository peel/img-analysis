
import akka.actor.Actor
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.filter.{ThresholdFilter, PixelateFilter}

import scala.concurrent.Future

object ImageTransformer{
  case class Transform(img: Future[Image])
  case class Transformed(img: Future[Image])
  case object NotTransformed
}

class ImageTransformer extends Actor{
  import ImageTransformer._
  import context.dispatcher

  def receive = {
    case Transform(image) => sender ! Transformed(image.map(_.filter(PixelateFilter(100), ThresholdFilter(127)).scale(0.1)))
    case _ => sender ! NotTransformed
  }
}

object ImageConverter{
  case class ImageToMatrix(img: Future[Image])
  case class MatrixFromImage(mx: Future[Matrix])
  case object NotConverted
}

class ImageConverter extends Actor{
  import ImageConverter._
  import context.dispatcher

  def receive = {
    case ImageToMatrix(img) => sender ! MatrixFromImage(img.map(MatrixUtils.fromImage))
    case _ => sender ! NotConverted
  }
}

