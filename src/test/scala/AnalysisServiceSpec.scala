import akka.actor.ActorRefFactory
import org.specs2.mutable.Specification
import spray.testkit.ScalatestRouteTest
import spray.http.StatusCodes._

class AnalysisServiceSpec extends Specification with RootService with ScalatestRouteTest {
  override implicit def actorRefFactory: ActorRefFactory = system

  "The AnalysisService" should {
    "return 'OK' when asking for status " in {
     Get("/api/status") ~> route ~> check {
       status must be (OK)
       entity.toString must contain("OK")
     }
    }
    "return an array when asking  " in {
      Get("/api/status") ~> route ~> check {
        status must be (OK)
        entity.toString must contain("OK")
      }
    }
  }
}
