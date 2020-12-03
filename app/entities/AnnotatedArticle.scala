package entities

import play.api.libs.json.{OFormat, Json}

case class AnnotatedArticle(id: String,
                            longUrl: String,
                            crawlTime: BigDecimal,
                            text: String,
                            posAnnos: List[PosAnnotation])

object AnnotatedArticle{
  implicit val format: OFormat[AnnotatedArticle] = Json.format[AnnotatedArticle]
}
