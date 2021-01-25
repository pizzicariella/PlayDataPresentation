package controlllers

import controllers.{AnnotatedArticleController, HomeController}
import entities.{AnnotatedArticle, AnnotatedToken}
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import play.api.Mode
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.CSRFTokenHelper.addCSRFToken
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, defaultAwaitTimeout, status, stubControllerComponents}
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.Future

class AnnotatedArticleControllerTest extends PlaySpec{

  implicit lazy val cc = stubControllerComponents()

  val reactiveMongoApi = mock[ReactiveMongoApi]

  val controller = new AnnotatedArticleController(cc, reactiveMongoApi)

  "inMemoryArticleList" should {
    "return OK" in {
      val result: Future[Result] = controller.inMemoryArticleList().apply(FakeRequest())
      val stat = status(result)
      stat mustBe 200
    }

    "contain valid json" in {
      val result: Future[Result] = controller.inMemoryArticleList().apply(FakeRequest())
      val json = contentAsJson(result)
      val jsonList = json.as[Seq[AnnotatedArticle]]
      jsonList mustBe of[Seq[AnnotatedArticle]]
    }
  }

}
