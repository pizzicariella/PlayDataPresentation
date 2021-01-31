package entities

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class AnnotatedTextTest extends PlaySpec{

  val exampleAnnotatedToken = AnnotatedText(
    "Dies ist ein Test Text",
    Seq("Dies", "ist", "ein", "Test", "Text"),
    Seq("PRON", "AUX", "DET", "NOUN", "NOUN"),
    Seq("dies", "sein", "ein", "Test", "Text"))

  val exampleAnnotatedTokenString = "{\"text\":\"Dies ist ein Test Text\",\"token\":[\"Dies\",\"ist\",\"ein\"," +
    "\"Test\",\"Text\"],\"posAnnos\":[\"PRON\",\"AUX\",\"DET\",\"NOUN\",\"NOUN\"],\"lemmas\":[\"dies\",\"sein\"," +
    "\"ein\",\"Test\",\"Text\"]}"

  "AnnotatedToken Json Format" should {
    "create correct object" in {
      val jsResult = Json.parse(exampleAnnotatedTokenString).validate[AnnotatedText]
      val res = jsResult.getOrElse("undefined")
      res mustBe exampleAnnotatedToken
    }

    "create correct json string" in {
      val jsonString = Json.stringify(Json.toJson(exampleAnnotatedToken))
      jsonString mustBe exampleAnnotatedTokenString
    }
  }
}
