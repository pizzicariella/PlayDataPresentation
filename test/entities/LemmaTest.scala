package entities

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class LemmaTest extends PlaySpec{

  val exampleLemma = Lemma(0,3,"Test")
  val exampleLemmaString = "{\"beginToken\":0,\"endToken\":3,\"result\":\"Test\"}"

  "Lemma Json Format" should {
    "create correct object" in {
      val jsResult = Json.parse(exampleLemmaString).validate[Lemma]
      val res = jsResult.getOrElse("undefined")
      res mustBe exampleLemma
    }

    "create correct json string" in {
      val jsonString = Json.stringify(Json.toJson(exampleLemma))
      jsonString mustBe exampleLemmaString
    }
  }
}
