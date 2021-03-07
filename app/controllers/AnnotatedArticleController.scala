package controllers

import entities.AnnotatedArticle
import org.apache.commons.io.IOUtils
import play.Environment

import javax.inject.Inject
import play.api.libs.json.{JsObject, _}
import play.api.mvc._
import play.api.routing._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.mvc.Http.MimeTypes
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import utils.FileReader.readFile

import java.io.{BufferedReader, InputStreamReader}
import java.nio.charset.StandardCharsets
import scala.concurrent.{ExecutionContext, Future}

class AnnotatedArticleController @Inject()(cc: ControllerComponents,
                                           val reactiveMongoApi: ReactiveMongoApi,
                                           val env: Environment)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = cc.executionContext

  //TODO get collection name from config file
  def articleCollection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("articles_annotated"))

  def articleList = Action.async { implicit request =>

    //TODO change max size? to what?
    val futureArticleList: Future[Seq[AnnotatedArticle]] = articleCollection
      .flatMap(articleCollection => articleCollection
        .find(Json.obj(), projection = Option.empty[JsObject])
        .cursor[AnnotatedArticle](ReadPreference.primary)
        .collect[Seq](200, Cursor.FailOnError[Seq[AnnotatedArticle]]()))

    futureArticleList.map { article => Ok(Json.toJson(article)) }
  }

  def inMemoryArticleList() = Action { implicit request =>
    //only in development mode
    //val path = "conf/resources/inMemoryArticles.json"
    //val jsonString = readFile(path).reduce(_+_)

    //only in production mode
    val jsonString = IOUtils.toString(env.resourceAsStream("resources/inMemoryArticles.json"), StandardCharsets.UTF_8)


    val jsResult = Json.parse(jsonString).validate[Seq[AnnotatedArticle]]
    jsResult.fold(
      error => {Ok(error.toString())},
      success => {Ok(Json.toJson(success))}
    )
  }

  def javascriptRoutes = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")
    (routes.javascript.AnnotatedArticleController.articleList,
      routes.javascript.AnnotatedArticleController.inMemoryArticleList,
      routes.javascript.HomeController.annotatedText)).as(MimeTypes.JAVASCRIPT)
  }
}

