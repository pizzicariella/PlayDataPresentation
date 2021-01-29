package views

import entities.TextToTag
import org.scalatestplus.play.PlaySpec
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.test.CSRFTokenHelper.addCSRFToken
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, stubMessagesApi, stubMessagesRequest}

//Unfortunately integration testing with selenium Web browser is not possible because com.google.guava version
//>21.0 is required, while Spark requires guava version 15.0
class AnalyzeTest extends PlaySpec {

  "analyze template" should {
    val textForm = Form(mapping("text" -> nonEmptyText)(TextToTag.apply)(TextToTag.unapply))
    val messagesApi = stubMessagesApi()
    implicit lazy val messages = stubMessagesRequest(messagesApi, addCSRFToken(FakeRequest()))
    val loadAnnos = "f"
    val html = views.html.analyze(textForm, loadAnnos)

    "display important elements" in {
      contentAsString(html) must include("Analyse")
      contentAsString(html) must include("Text analysieren")
      contentAsString(html) must include("<textarea id=\"text\" name=\"text\" rows=\"10\" cols=\"70\"></textarea>")
      contentAsString(html) must include("<button type=\"submit\">Analysieren</button>")
    }

    "not display other elements" in {
      contentAsString(html) mustNot include("Ergebnis")
    }
  }
}
