package views

import entities.TextToTag
import org.scalatestplus.play.{HtmlUnitFactory, OneBrowserPerSuite, PlaySpec}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.CSRFTokenHelper.addCSRFToken
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, stubMessagesApi, stubMessagesRequest}

class AnalyzeTest extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory{

  val application: Application = new GuiceApplicationBuilder().build()
  "analyze template" should {
    val textForm = Form(mapping("text" -> nonEmptyText)(TextToTag.apply)(TextToTag.unapply))
    val loadAnnos = "f"
    val messagesApi = stubMessagesApi()
    implicit lazy val messages = stubMessagesRequest(messagesApi, addCSRFToken(FakeRequest()))
    val html = views.html.analyze(textForm, loadAnnos)

    "display title" in {
      contentAsString(html) must include("Analyse")
      go to s"http://localhost:9000/analyze"
      currentUrl mustBe "http://localhost:9000/analyze"
      //pageTitle mustBe "Analyse"
    }

    "show textarea" in {
      contentAsString(html) must include("<textarea id=\"text\" name=\"text\" rows=\"10\" cols=\"70\"></textarea>")
    }

    "show results after submit" in {

    }
  }
}
