package daos

import entities.AnnotatedArticle
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AnnotatedArticleDao @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi){

  private def collection: Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection("annotated_articles"))

  def listArticles(limit: Int = 100): Future[Seq[AnnotatedArticle]] = collection
    .flatMap(articleCollection => articleCollection
      .find(Json.obj(), projection = Option.empty[JsObject])
      .cursor[AnnotatedArticle](ReadPreference.primary)
      .collect[Seq](limit, Cursor.FailOnError[Seq[AnnotatedArticle]]()))

}

