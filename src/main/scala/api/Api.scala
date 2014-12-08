package api

import core.{Core, CoreActors}
import spray.routing.HttpService
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

trait Api extends HttpService with Core with CoreActors{
  val routes = new AnalysisService(dataHandler).route
}
