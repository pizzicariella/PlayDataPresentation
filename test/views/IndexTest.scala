package views

import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout}

class IndexTest extends PlaySpec{

  "Index template" should {
    "show correct titles" in {
      val html = views.html.index()
      contentAsString(html) must include("Home")
      contentAsString(html) must include("Präsentation Bachelorarbeit")
    }
  }

}
