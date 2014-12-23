package analysis


import akka.actor.Actor
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.filter.{PixelateFilter, ThresholdFilter}

import scala.concurrent.Future

object ImageTransformer{
  case class Transform(img: Future[Image])
  case class Transformed(img: Future[Image])
  case object NotTransformed
}

class ImageTransformer extends Actor{
  import analysis.ImageTransformer._
  import context.dispatcher

  def receive = {
    case Transform(image) => sender ! Transformed(image.map(_.filter(PixelateFilter(100), ThresholdFilter(127)).scale(0.1)))
    case _ => sender ! NotTransformed
  }
}

