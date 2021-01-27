package views

import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout}
import play.twirl.api.Html

class MainTest extends PlaySpec{

  "main template" should {
    "display title" in {
      val content = new Html("""
                      |<html>
                      | <head>
                      |   <body>
                      |     <p>Test Content </p>
                      |   </body>
                      | </head>
                      |</html>
            """)
      val html = views.html.main("test title")(content)
      contentAsString(html) must include("test title")
    }
  }
}
