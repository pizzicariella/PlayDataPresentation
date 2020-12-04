package entities

import play.api.libs.json.{OFormat, Json}

case class PosAnnotation(begin: Int, end: Int, tag: String)

object PosAnnotation{
  implicit val format = Json.format[PosAnnotation]
}
