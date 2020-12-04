package entities

import play.api.libs.json.Json

case class AnnotatedArticle(_id: String,
                            longUrl: String,
                            crawlTime: String,
                            text: String,
                            annotationsPos: List[PosAnnotation])

object AnnotatedArticle{
  implicit val format = Json.format[AnnotatedArticle]
}
