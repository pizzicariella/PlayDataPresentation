package entities

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class AnnotatedTokenTest extends PlaySpec{

  val exampleAnnotatedToken = AnnotatedToken("Dies ist ein Test Text",
    Seq("PRON", "AUX", "DET", "NOUN", "NOUN"),
    Seq("Dies", "ist", "ein", "Test", "Text"))

  val exampleAnnotatedTokenString = "{\"text\":\"Dies ist ein Test Text\",\"posAnnos\":[\"PRON\",\"AUX\",\"DET\"," +
    "\"NOUN\",\"NOUN\"],\"token\":[\"Dies\",\"ist\",\"ein\",\"Test\",\"Text\"]}"

  "AnnotatedToken Json Format" should {
    "create correct object" in {
      val jsResult = Json.parse(exampleAnnotatedTokenString).validate[AnnotatedToken]
      val res = jsResult.getOrElse("undefined")
      res mustBe exampleAnnotatedToken
    }

    "create correct json string" in {
      val jsonString = Json.stringify(Json.toJson(exampleAnnotatedToken))
      jsonString mustBe exampleAnnotatedTokenString
    }
  }
}
