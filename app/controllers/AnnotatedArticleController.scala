package controllers

import java.io.{File, FileInputStream}
import entities.AnnotatedArticle

import javax.inject.Inject
import play.api.libs.json.{JsObject, _}
import play.api.mvc._
import play.api.routing._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.mvc.Http.MimeTypes
import reactivemongo.api.{AsyncDriver, Cursor, DB, MongoConnection, ReadPreference}
import reactivemongo.api.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID, BSONValue, Macros, document}
import reactivemongo.play.json._

import compat._
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.play.json.collection.JSONCollection
//import reactivemongo.play.json.collection.{JSONCollection, _}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source


class AnnotatedArticleController @Inject()(cc: ControllerComponents, val reactiveMongoApi: ReactiveMongoApi)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = cc.executionContext

  //TODO get collection name from config file
  //val mongoUri = "mongodb://marie:password@localhost:27017"
  //val driver = AsyncDriver()
  //val parsedUri = MongoConnection.fromString(mongoUri)
  //val futureConnection = parsedUri.flatMap(driver.connect(_))
  //def db: Future[DB] = futureConnection.flatMap(_.database("ba"))

  //def articleCollection: Future[BSONCollection] = reactiveMongoApi.database.map(_.collection("annotated_articles"))
  def articleCollection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("annotated_articles"))

  def articleList = Action.async { implicit request =>

    /*val cursor: Future[Cursor[AnnotatedArticle]] = articleCollection.map {
      _.find(Json.obj(), projection = Option.empty[JsObject]).cursor[AnnotatedArticle]()
    }*/
    //articleCollection
   // val cursor = articleCollection.map(_.find(BSONDocument.empty).cursor[AnnotatedArticle]())

    /*val futureArticleList: Future[List[AnnotatedArticle]] =
      cursor.flatMap(_.collect[List](-1, Cursor.FailOnError[List[AnnotatedArticle]]()))*/

    /*val futureArticleList = articleCollection.flatMap(_.find("age" -> 13))
      .cursor[AnnotatedArticle]().collect[List](-1, Cursor.FailOnError[List[AnnotatedArticle]]()))*/
    //val futureArticleList = cursor.flatMap(_.collect[List]())
    val futureArticleList: Future[Seq[AnnotatedArticle]] = articleCollection
      .flatMap(articleCollection => articleCollection
        .find(Json.obj(), projection = Option.empty[JsObject])
        .cursor[AnnotatedArticle](ReadPreference.primary)
        .collect[Seq](-1, Cursor.FailOnError[Seq[AnnotatedArticle]]()))

    futureArticleList.map { article => Ok(Json.toJson(article)) }
  }

  def inMemoryArticleList = Action { implicit request =>
    val path = "resources/annotatedArticles.json"
    /*val articleList = readJsonFile(path).map(line => {
      val j = parseJson(line)
      println(j)
      j
    })

    Ok(Json.toJson(articleList))*/
    //Ok(Json.toJson[List[AnnotatedArticle]](readJsonFile(path).map(json => Json.parse(json).as[AnnotatedArticle])))
    //Ok(Json.toJson(parseJsonFromFile(path)))
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

