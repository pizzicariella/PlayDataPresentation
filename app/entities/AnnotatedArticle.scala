package entities

import play.api.libs.json.Json

case class AnnotatedArticle(id: String,
                            longUrl: String,
                            crawlTime: String,
                            text: String,
                            annotationsPos: List[PosAnnotation],
                            tagPercentage: List[TagPercentage])

object AnnotatedArticle{
  implicit val format = Json.format[AnnotatedArticle]
}
