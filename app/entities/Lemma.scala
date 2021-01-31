package entities

import play.api.libs.json.Json

case class Lemma(beginToken: Int, endToken: Int, result: String)

object Lemma{
  implicit val format = Json.format[Lemma]
}

