package entities

import play.api.libs.json.Json

case class AnnotatedToken(text: String, posAnnos: Seq[String], token: Seq[String])

object AnnotatedToken{
  implicit val format = Json.format[AnnotatedToken]
}
