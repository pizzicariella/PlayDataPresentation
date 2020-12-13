package entities

import play.api.libs.json.Json

case class TagPercentage(tag: String, percentage: Double)

object TagPercentage{
  implicit val format = Json.format[TagPercentage]
}