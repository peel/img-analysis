package api

import akka.actor.ActorRef
import scala.concurrent.ExecutionContext

class AnalysisService(dataHandler: ActorRef)(implicit ctx: ExecutionContext) {
  val route = ???
}
