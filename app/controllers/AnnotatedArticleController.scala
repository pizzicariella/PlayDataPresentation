package controllers

import entities.AnnotatedArticle
import javax.inject.Inject
import play.api.libs.json.{JsObject, _}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.routing.JavaScriptReverseRouter
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.mvc.Http.MimeTypes
import reactivemongo.api.Cursor
import reactivemongo.play.json._
import reactivemongo.play.json.collection.{JSONCollection, _}

import scala.concurrent.{ExecutionContext, Future}

class AnnotatedArticleController @Inject()(cc: ControllerComponents, val reactiveMongoApi: ReactiveMongoApi)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = cc.executionContext

  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("annotated_articles"))

  def articleList = Action.async { implicit request =>

    val cursor: Future[Cursor[AnnotatedArticle]] = collection.map {
      _.find(Json.obj(), projection = Option.empty[JsObject]).cursor[AnnotatedArticle]()
    }

    val futureArticleList: Future[List[AnnotatedArticle]] =
      cursor.flatMap(_.collect[List](-1, Cursor.FailOnError[List[AnnotatedArticle]]()))

    futureArticleList.map { article => Ok(Json.toJson(article)) }
  }

  def jsRoutes = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")
    (routes.javascript.AnnotatedArticleController.articleList)).as(MimeTypes.JAVASCRIPT)
  }
}

