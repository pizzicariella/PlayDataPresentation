package entities

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class AnnotatedArticle(id: String,
                            longUrl: String,
                            crawlTime: Long,
                            text: String,
                            annotationsPos: Seq[PosAnnotation],
                            tagPercentage: Seq[TagPercentage])

object AnnotatedArticle{

  implicit val annotatedArticleReads: Reads[AnnotatedArticle] = (
     (JsPath \ "_id").read[String] and
    (JsPath \ "longUrl").read[String] and
    (JsPath \ "crawlTime" \ "$date").read[Long] and
    (JsPath \ "text").read[String] and
    (JsPath \ "annotationsPos").read[Seq[PosAnnotation]] and
    (JsPath \ "tagPercentage").read[Seq[TagPercentage]])(AnnotatedArticle.apply _)

  implicit val annotatedArticleWrites: Writes[AnnotatedArticle] = (
    (JsPath \ "_id").write[String] and
      (JsPath \ "longUrl").write[String] and
      (JsPath \ "crawlTime" \ "$date").write[Long] and
      (JsPath \ "text").write[String] and
      (JsPath \ "annotationsPos").write[Seq[PosAnnotation]] and
      (JsPath \ "tagPercentage").write[Seq[TagPercentage]])(unlift(AnnotatedArticle.unapply)
  )
}
