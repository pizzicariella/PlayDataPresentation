package entities

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class PosAnnotationTest extends PlaySpec{

  val examplePosAnnotation = PosAnnotation(0,3,"Test")
  val examplePosAnnotationString = "{\"begin\":0,\"end\":3,\"tag\":\"Test\"}"

  "PosAnnotation Json Format" should {
    "create correct object" in {
      val jsResult = Json.parse(examplePosAnnotationString).validate[PosAnnotation]
      val res = jsResult.getOrElse("undefined")
      res mustBe examplePosAnnotation
    }

    "create correct json string" in {
      val jsonString = Json.stringify(Json.toJson(examplePosAnnotation))
      jsonString mustBe examplePosAnnotationString
    }
  }

}
