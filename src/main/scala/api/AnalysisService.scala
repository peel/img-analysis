package api

import scala.concurrent.duration._
import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import core.{Image, DataHandler}
import core.DataHandler.{Analysed, Analyse}
import org.json4s.JsonAST.JObject
import spray.routing._
import spray.http._
import spray.http.StatusCodes._

import scala.concurrent.ExecutionContext

class AnalysisService(dataHandler: ActorRef)(implicit ctx: ExecutionContext) extends Directives {
  import Json4sProtocol._
  implicit val timeout = Timeout(5 seconds)

  val route =
    path("") {
      post {
        respondWithStatus(Created){
          entity(as[JObject]){data =>
            val image:Image = data.extract[Image]
            println(image)
            complete((dataHandler ? Analyse(image)).mapTo[Analysed].map(a => a.analysis).recover{case _ => "error"})
          }
        }
      }
    }
}
