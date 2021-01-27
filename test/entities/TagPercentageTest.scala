package entities

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class TagPercentageTest extends PlaySpec{
  val exampleTagPercentage = TagPercentage("NOUN",0.4)
  val exampleTagPercentageString = "{\"tag\":\"NOUN\",\"percentage\":0.4}"

  "TagPercentage Json Format" should {
    "create correct object" in {
      val jsResult = Json.parse(exampleTagPercentageString).validate[TagPercentage]
      val res = jsResult.getOrElse("undefined")
      res mustBe exampleTagPercentage
    }

    "create correct json string" in {
      val jsonString = Json.stringify(Json.toJson(exampleTagPercentage))
      jsonString mustBe exampleTagPercentageString
    }
  }
}
