package controlllers

import controllers.HomeController
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import play.api.mvc.{AnyContentAsEmpty, DefaultActionBuilder, DefaultMessagesActionBuilderImpl, DefaultMessagesControllerComponents, MessagesControllerComponents, Result, Results}
import play.api.test.Helpers.baseApplicationBuilder.injector
import services.PosPipeAnnotator

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class HomeControllerTest extends PlaySpec with Results{

  "Index Page" should {
    "should be valid" in {
      implicit lazy val executionContext = injector.instanceOf[ExecutionContext]
      implicit lazy val mcc = stubMessagesControllerComponents()
      implicit lazy val annotator = new PosPipeAnnotator
      val controller = new HomeController()
      val result: Future[Result] = controller.index().apply(FakeRequest())
      val content = contentType(result)
      content mustBe Some("text/html")
    }
  }

  //Method taken from scalatestplus-play version 4.0.0 which could not be used because not compatible with Play 2.6
  def stubMessagesControllerComponents(): MessagesControllerComponents = {
    val stub = stubControllerComponents()
    new DefaultMessagesControllerComponents(
      new DefaultMessagesActionBuilderImpl(stubBodyParser(AnyContentAsEmpty), stub.messagesApi)(stub.executionContext),
      DefaultActionBuilder(stub.actionBuilder.parser)(stub.executionContext), stub.parsers,
      stub.messagesApi, stub.langs, stub.fileMimeTypes, stub.executionContext
    )
  }

}
