package controlllers

import controllers.HomeController
import entities.AnnotatedText
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.mvc.{AnyContentAsEmpty, DefaultActionBuilder, DefaultMessagesActionBuilderImpl, DefaultMessagesControllerComponents, MessagesControllerComponents, Result, Results}
import play.api.test.Helpers.baseApplicationBuilder.injector
import services.PosPipeAnnotator
import testHelpers.TestHelpers

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class HomeControllerTest extends PlaySpec with Results{

  implicit lazy val executionContext = injector.instanceOf[ExecutionContext]
  implicit lazy val mcc = TestHelpers.stubMessagesControllerComponents()
  implicit lazy val annotator = new PosPipeAnnotator
  val controller = new HomeController()
  val text = "Dies ist ein Test."

  "Index Page" should {
    "return OK" in {
      val result: Future[Result] = controller.index().apply(FakeRequest())
      val stat = status(result)
      stat mustBe 200
    }

    "be html" in {
      val result: Future[Result] = controller.index().apply(FakeRequest())
      val content = contentType(result)
      content mustBe Some("text/html")
    }
  }

  "Corpus Page" should {
    "return OK" in {
      val result: Future[Result] = controller.corpus().apply(FakeRequest())
      val stat = status(result)
      stat mustBe 200
    }

    "be html" in {
      val result: Future[Result] = controller.corpus().apply(FakeRequest())
      val content = contentType(result)
      content mustBe Some("text/html")
    }
  }

  "Analyze Page" should {
    "return OK" in {
      val result: Future[Result] = controller.analyze().apply(addCSRFToken(FakeRequest()))
      val stat = status(result)
      stat mustBe 200
    }

    "be html" in {
      val result: Future[Result] = controller.analyze().apply(addCSRFToken(FakeRequest()))
      val content = contentType(result)
      content mustBe Some("text/html")
    }
  }

  "Info Page" should {
    "return OK" in {
      val result: Future[Result] = controller.info().apply(FakeRequest())
      val stat = status(result)
      stat mustBe 200
    }

    "be html" in {
      val result: Future[Result] = controller.info().apply(FakeRequest())
      val content = contentType(result)
      content mustBe Some("text/html")
    }
  }

  "ValidateTextForm" should {
    "be html" in {
      val result: Future[Result] = controller
        .validateTextForm()
        .apply(addCSRFToken(FakeRequest().withFormUrlEncodedBody()))
      val content = contentType(result)
      content mustBe Some("text/html")
    }

    "redirect to analyze page" in {
      val result: Future[Result] = controller
        .validateTextForm()
        .apply(addCSRFToken(FakeRequest().withFormUrlEncodedBody(("text", text))))
      val redirect = redirectLocation(result)
      val stat = status(result)
      redirect mustBe Some("/analyze")
      stat mustBe 303
    }

    "return BAD_REQUEST" in {
      val result: Future[Result] = controller
        .validateTextForm()
        .apply(addCSRFToken(FakeRequest().withFormUrlEncodedBody()))
      val stat = status(result)
      stat mustBe 400
    }
  }

  "AnnotatedText" should {
    "return OK" in {
      controller
        .validateTextForm()
        .apply(addCSRFToken(FakeRequest().withFormUrlEncodedBody(("text", text))))
      val result: Future[Result] = controller.annotatedText().apply(FakeRequest())
      val stat = status(result)
      stat mustBe 200
    }

    "contain valid json" in {
      val posAnnos = Seq("PRON", "AUX", "DET", "NOUN")
      val token = Seq("Dies", "ist", "ein", "Test")
      controller
        .validateTextForm()
        .apply(addCSRFToken(FakeRequest().withFormUrlEncodedBody(("text", text))))
      val result: Future[Result] = controller.annotatedText().apply(FakeRequest())
      val json = contentAsJson(result)
      val at = AnnotatedText(text, posAnnos, token)
      json mustBe Json.toJson(at)
    }
  }
}
