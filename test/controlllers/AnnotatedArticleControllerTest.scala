package controlllers

import controllers.AnnotatedArticleController
import entities.AnnotatedArticle
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, defaultAwaitTimeout, status, stubControllerComponents}
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.Future

class AnnotatedArticleControllerTest extends PlaySpec{

  implicit lazy val cc = stubControllerComponents()

  val reactiveMongoApi = mock[ReactiveMongoApi]

  val controller = new AnnotatedArticleController(cc, reactiveMongoApi)

  //TODO?? Methods that require database access are not tested yet

  "inMemoryArticleList" should {
    "return OK" in {
      val result: Future[Result] = controller.inMemoryArticleList().apply(FakeRequest())
      val stat = status(result)
      stat mustBe 200
    }

    "contain valid json List of size 9" in {
      val result: Future[Result] = controller.inMemoryArticleList().apply(FakeRequest())
      val json = contentAsJson(result)
      val jsonList = json.as[Seq[AnnotatedArticle]]
      jsonList.size mustBe 9
    }
  }
}
