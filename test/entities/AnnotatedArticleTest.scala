package entities

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class AnnotatedArticleTest extends PlaySpec{

  val exampleArticle = AnnotatedArticle(
    "testId",
    "www.test.de",
    1575834314135L,
    "Dies ist ein Test Text",
    Seq(PosAnnotation(0, 3, "PRON"), PosAnnotation(5, 7, "AUX"), PosAnnotation(9, 11, "DET"),
      PosAnnotation(13, 16, "NOUN"), PosAnnotation(18, 21, "NOUN")),
    Seq(Lemma(0, 3, "dies"), Lemma(5, 7, "sein"), Lemma(9, 11, "ein"), Lemma(13, 16, "Test"), Lemma(18, 21, "Text")),
    Seq(PosPercentage("PRON", 0.2), PosPercentage("AUX", 0.2), PosPercentage("DET", 0.2), PosPercentage("NOUN", 0.4)))

  val exampleArticleString = "{\"_id\":{\"$oid\":\"testId\"},\"long_url\":\"www.test.de\",\"crawl_time\":" +
    "{\"$date\":1575834314135},\"text\":\"Dies ist ein Test Text\",\"pos\":[" +
    "{\"begin\":0,\"end\":3,\"tag\":\"PRON\"},{\"begin\":5,\"end\":7,\"tag\":\"AUX\"}," +
    "{\"begin\":9,\"end\":11,\"tag\":\"DET\"},{\"begin\":13,\"end\":16,\"tag\":\"NOUN\"}," +
    "{\"begin\":18,\"end\":21,\"tag\":\"NOUN\"}],\"lemma\":[{\"beginToken\":0,\"endToken\":3,\"result\":\"dies\"}," +
    "{\"beginToken\":5,\"endToken\":7,\"result\":\"sein\"},{\"beginToken\":9,\"endToken\":11,\"result\":\"ein\"}," +
    "{\"beginToken\":13,\"endToken\":16,\"result\":\"Test\"},{\"beginToken\":18,\"endToken\":21,\"result\":\"Text\"}]," +
    "\"pos_percentage\":[{\"tag\":\"PRON\",\"percentage\":0.2},{\"tag\":\"AUX\",\"percentage\":0.2}," +
    "{\"tag\":\"DET\",\"percentage\":0.2},{\"tag\":\"NOUN\",\"percentage\":0.4}]}"

  val exampleWritesArticleString = "{\"_id\":{\"$oid\":\"testId\"},\"long_url\":\"www.test.de\",\"crawl_time\":" +
    "{\"$date\":1575834314135},\"text\":\"Dies ist ein Test Text\",\"pos\":[" +
    "{\"begin\":0,\"end\":3,\"tag\":\"PRON\"},{\"begin\":5,\"end\":7,\"tag\":\"AUX\"}," +
    "{\"begin\":9,\"end\":11,\"tag\":\"DET\"},{\"begin\":13,\"end\":16,\"tag\":\"NOUN\"}," +
    "{\"begin\":18,\"end\":21,\"tag\":\"NOUN\"}],\"lemma\":[{\"beginToken\":0,\"endToken\":3,\"result\":\"dies\"}," +
    "{\"beginToken\":5,\"endToken\":7,\"result\":\"sein\"},{\"beginToken\":9,\"endToken\":11,\"result\":\"ein\"}," +
    "{\"beginToken\":13,\"endToken\":16,\"result\":\"Test\"},{\"beginToken\":18,\"endToken\":21,\"result\":\"Text\"}]," +
    "\"pos_percentage\":[{\"tag\":\"PRON\",\"percentage\":0.2},{\"tag\":\"AUX\",\"percentage\":0.2}," +
    "{\"tag\":\"DET\",\"percentage\":0.2},{\"tag\":\"NOUN\",\"percentage\":0.4}]}"

  val exampleNonArticleString = "{\"id\":\"testId\",\"longUrl\":\"www.test.de\",\"crawlTime\":" +
    "1575834314135,\"text\":\"Dies ist ein Test Text\",\"annotationsPos\":[" +
    "{\"begin\":0,\"end\":3,\"tag\":\"PRON\"},{\"begin\":5,\"end\":7,\"tag\":\"AUX\"}," +
    "{\"begin\":9,\"end\":11,\"tag\":\"DET\"},{\"begin\":13,\"end\":16,\"tag\":\"NOUN\"}," +
    "{\"begin\":18,\"end\":21,\"tag\":\"NOUN\"}],\"tagPercentage\":[{\"tag\":\"PRON\",\"percentage\":0.2}," +
    "{\"tag\":\"AUX\",\"percentage\":0.2},{\"tag\":\"DET\",\"percentage\":0.2},{\"tag\":\"NOUN\",\"percentage\":0.4}]}"

  "AnnotatedArticle Reads" should {
    "create correct object" in {
      val jsResult = Json.parse(exampleArticleString).validate[AnnotatedArticle]
      val res = jsResult.getOrElse("undefined")
      res mustBe exampleArticle
    }

    "return JsError" in {
      val jsResult = Json.parse(exampleNonArticleString).validate[AnnotatedArticle]
      val res = jsResult.getOrElse("undefined")
      res mustBe "undefined"
    }
  }

  "AnnotatedArticle Writes" should {
    "create correct json String" in {
      val jsonString = Json.stringify(Json.toJson(exampleArticle))
      jsonString mustBe exampleWritesArticleString
    }
  }
}
