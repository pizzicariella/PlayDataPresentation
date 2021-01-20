package controllers

import java.io.{File, FileInputStream}
import entities.AnnotatedArticle

import javax.inject.Inject
import play.api.libs.json.{JsObject, _}
import play.api.mvc._
import play.api.routing._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.mvc.Http.MimeTypes
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.play.json._

import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source


class AnnotatedArticleController @Inject()(cc: ControllerComponents, val reactiveMongoApi: ReactiveMongoApi)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = cc.executionContext

  //TODO get collection name from config file
  def articleCollection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("annotated_articles"))

  def articleList = Action.async { implicit request =>

    val futureArticleList: Future[Seq[AnnotatedArticle]] = articleCollection
      .flatMap(articleCollection => articleCollection
        .find(Json.obj(), projection = Option.empty[JsObject])
        .cursor[AnnotatedArticle](ReadPreference.primary)
        .collect[Seq](-1, Cursor.FailOnError[Seq[AnnotatedArticle]]()))

    futureArticleList.map { article => Ok(Json.toJson(article)) }
  }

  def inMemoryArticleList = Action { implicit request =>
    val path = "resources/annotatedArticles.json"

    val jsonString = readJsonFile(path).reduce(_+_)
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

  def parseJsonFromFile(path: String): List[AnnotatedArticle] = {
    val file = new File(path)
    val stream = new FileInputStream(file)
    val articles = try {
      Json.parse(stream).as[List[AnnotatedArticle]]
    } finally {
      stream.close()
    }
    println(articles)
    articles
  }

  //TODO refactor to utils
  def readJsonFile(path: String): List[String] = {
    val bufferedSource = Source.fromFile(path)
    val lines = bufferedSource.getLines().toList
    bufferedSource.close()
    lines
  }

  //TODO refactor to utils
  def parseJson(line: String): AnnotatedArticle = {
    val json = Json.parse(line)
    json.as[AnnotatedArticle]
  }
}

