import akka.actor.{ActorRefFactory, ActorSystem, Props}
import akka.actor.ActorDSL._
import akka.event.Logging
import akka.io.IO
import akka.io.Tcp._
import spray.can.Http


object Boot extends App {

  implicit val system = ActorSystem("image-analysis")
  val log = Logging(system,getClass)

  val callbackActor = actor(new Act{
    become {
      case b @ Bound(connection) => log.info(b.toString)
      case cf @ CommandFailed(cmd) => log.error(cf.toString)
      case all:Any => log.info("Message received: {}",all.toString)
    }
  })

  val service = system.actorOf(Props[RootServiceActor],"root-service")

  IO(Http).tell(Http.Bind(service, "localhost", 8080), callbackActor)

}
