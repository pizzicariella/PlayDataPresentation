package entities

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class AnnotatedArticle(id: String,
                            longUrl: String,
                            crawlTime: Long,
                            text: String,
                            annotationsPos: Seq[PosAnnotation],
                            lemmas: Seq[Lemma],
                            tagPercentage: Seq[PosPercentage])

object AnnotatedArticle{

  implicit val annotatedArticleReads: Reads[AnnotatedArticle] = (
    (JsPath \ "_id" \ "$oid").read[String] and
    (JsPath \ "long_url").read[String] and
    (JsPath \ "crawl_time" \ "$date").read[Long] and
    (JsPath \ "text").read[String] and
    (JsPath \ "pos").read[Seq[PosAnnotation]] and
    (JsPath \ "lemma").read[Seq[Lemma]] and
    (JsPath \ "pos_percentage").read[Seq[PosPercentage]])(AnnotatedArticle.apply _)

  implicit val annotatedArticleWrites: Writes[AnnotatedArticle] = (
    (JsPath \ "_id" \ "$oid").write[String] and
    (JsPath \ "long_url").write[String] and
    (JsPath \ "crawl_time" \ "$date").write[Long] and
    (JsPath \ "text").write[String] and
    (JsPath \ "pos").write[Seq[PosAnnotation]] and
    (JsPath \ "lemma").write[Seq[Lemma]] and
    (JsPath \ "pos_percentage").write[Seq[PosPercentage]])(unlift(AnnotatedArticle.unapply)
  )
}
