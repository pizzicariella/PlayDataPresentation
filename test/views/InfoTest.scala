package views

import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout}

class InfoTest extends PlaySpec{

  val html = views.html.info()

  "Info template" should {
    "have correct titles" in {
      contentAsString(html) must include("About")
      contentAsString(html) must include("Infos zum Projekt")
    }

    "contain links" in {
      contentAsString(html) must include("<a href=\"https://github.com/pizzicariella/POSPipelineGerman\">Spark NLP Projekt</a>")
      contentAsString(html) must include("<a href=\"https://github.com/pizzicariella/PlayDataPresentation\">Play Web Projekt</a>")
    }
  }
}
