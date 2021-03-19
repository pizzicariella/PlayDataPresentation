package entities

import play.api.libs.json.Json

case class PosPercentage(tag: String, percentage: Double)

object PosPercentage{
  implicit val format = Json.format[PosPercentage]
}