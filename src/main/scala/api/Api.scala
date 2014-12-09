package api

import core.{Core, CoreActors}
import org.json4s.{DefaultFormats, Formats}
import spray.httpx.Json4sSupport
import spray.routing.HttpService
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}
trait Api extends HttpService with Core with CoreActors{
  val routes = new AnalysisService(dataHandler).route
}
