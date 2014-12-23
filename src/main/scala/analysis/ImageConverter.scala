package analysis

import akka.actor.Actor
import com.sksamuel.scrimage.Image

import scala.concurrent.Future

class ImageConverter extends Actor{
   import analysis.ImageConverter._
   import context.dispatcher

   def receive = {
     case ImageToMatrix(img) => sender ! MatrixFromImage(img.map(MatrixUtils.fromImage))
     case _ => sender ! NotConverted
   }
 }

object ImageConverter {
   case class ImageToMatrix(img: Future[Image])
   case class MatrixFromImage(mx: Future[Matrix])
   case object NotConverted
 }