//import akka.actor.Actor
//import com.sksamuel.scrimage.Image
//import com.sksamuel.scrimage.filter.{PixelateFilter, ThresholdFilter}
//import spray.json.{DefaultJsonProtocol, RootJsonFormat}
//
//import scala.concurrent._
//
//case class Analysis()
//
//object AnalysisJsonProtocol extends DefaultJsonProtocol {
//  implicit def analysisJsonFormat: RootJsonFormat[Analysis] = jsonFormat0(Analysis.apply)
//  implicit def analysedJsonFormat: RootJsonFormat[Analysed] = jsonFormat1(Analysed.apply)
//  implicit def notAnalysedJsonFormat: RootJsonFormat[NotAnalysed] = jsonFormat0(NotAnalysed.apply)
//}
//
//object AnalysisActor{
//  case class Analyse(image: Future[Image])
//  case class Analysed(analysis: Analysis)
//  case class NotAnalysed()
//}
//class AnalysisActor extends Actor {
//
//  override def receive = {
//    case Analyse(image) => sender ! reply(analyse(transform(image)))
//    case _ => NotAnalysed
//  }
//
//  def reply(analysis: Future[Analysis]) = analysis map {a => Analysed(a)}
//  def analyse(image: Future[Image]): Future[Analysis] = image map {i => Analysis()}
//  def transform(image: Future[Image]): Future[Image] = image map {
//    _.filter(PixelateFilter(100),ThresholdFilter(127)).scale(0.1)
//  }
//}
