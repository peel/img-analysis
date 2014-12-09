package core

import akka.actor.{ActorRefFactory, ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import api.{RoutedHttpService, Api}
import spray.can.Http

import scala.concurrent.duration._

trait Core {
  protected implicit def system: ActorSystem
}

trait CoreActors {
  this: Core =>
  val dataHandler = system.actorOf(Props[DataHandler])
}

object App extends App with Core with CoreActors with Api {

  def system: ActorSystem = ActorSystem("activator-akka-spray")
  def actorRefFactory: ActorRefFactory = system
  implicit val timeout = Timeout(5 seconds)

  val rootService = system.actorOf(Props(new RoutedHttpService(routes)))

  IO(Http)(system) ! Http.Bind(rootService, "0.0.0.0", port = 8080)

  sys.addShutdownHook(system.shutdown())

}

