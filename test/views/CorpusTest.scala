package views

import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout}

class CorpusTest extends PlaySpec{

  val html = views.html.corpus()

  "corpus template" should {
    "have correct titles" in {
      contentAsString(html) must include("Corpus")
      contentAsString(html) must include("News Corpus")
    }

    "contain important elements" in {
      contentAsString(html) must include("<template id=\"articleTemplate\">")
      contentAsString(html) must include("<button id=\"showAnnotationsButton\" type=\"button\">POS Annotationen zeigen</button>")
      contentAsString(html) must include("<dl id=\"posTagList\">")
    }
  }

}
