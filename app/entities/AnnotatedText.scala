package entities

import play.api.libs.json.Json

case class AnnotatedText(text: String, token: Seq[String], posAnnos: Seq[String], lemmas: Seq[String])

object AnnotatedText{
  implicit val format = Json.format[AnnotatedText]
}
