package analysis

import akka.actor.ActorRefFactory
import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest

class AnalysisServiceSpec extends Specification with RootService with ScalatestRouteTest {
  override implicit def actorRefFactory: ActorRefFactory = system

  "The AnalysisService" should {
    val API_URL: String = "/api/status"
    val STATUS_CODE: String = "OK"
    "return 'OK' when asking for status " in {
     Get(API_URL) ~> route ~> check {
       status must be (OK)
       entity.toString must contain(STATUS_CODE)
     }
    }
    "return an array when asking  " in {
      Get(API_URL) ~> route ~> check {
        status must be (OK)
        entity.toString must contain(STATUS_CODE)
      }
    }
  }
}
