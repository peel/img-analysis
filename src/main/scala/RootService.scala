import akka.actor.{Props, Actor}
import spray.http.MultipartFormData
import spray.routing.HttpService
import java.io.{ByteArrayInputStream => JBAIS}

class RootServiceActor extends Actor with RootService {
 override def receive = runRoute(route)
 override def actorRefFactory = context
}

trait RootService extends HttpService {
  import spray.httpx.SprayJsonSupport._
  import analysis.AnalysisServiceProtocol._
  import analysis.AnalysisService
  import analysis.AnalysisService._
 val route =
  pathPrefix("api"){
    path("status"){
      get{
        complete{
          "OK"
        }
      }
    } ~
   path("images"){
    post{
     entity(as[MultipartFormData]){data =>
      data.get("file") match {
       case Some(image) =>
         requestContext=>
         val analysisService = actorRefFactory.actorOf(Props(new AnalysisService(requestContext)))
         analysisService ! AnalysisService.Analyse(new JBAIS(image.entity.data.toByteArray))
       case None =>
         requestContext=>
         complete{
           NotAnalysed("Not an image")
         }
      }
     }
    }
   }
  }
}