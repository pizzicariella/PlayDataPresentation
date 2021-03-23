package controllers

import com.typesafe.config.ConfigFactory
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
import java.nio.charset.StandardCharsets
import scala.concurrent.{ExecutionContext, Future}

class AnnotatedArticleController @Inject()(cc: ControllerComponents,
                                           val reactiveMongoApi: ReactiveMongoApi,
                                           val env: Environment)
  extends AbstractController(cc) with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = cc.executionContext

  val collectionName = ConfigFactory.load().getString("mongodb.collection")

  def articleCollection: Future[JSONCollection] = database.map(_.collection[JSONCollection](collectionName))

  /**
   * Action that maps articles from MongoDB to Json.
   */
  def articleList = Action.async { implicit request =>

    val futureArticleList: Future[Seq[AnnotatedArticle]] = articleCollection
      .flatMap(articleCollection => articleCollection
        .find(Json.obj(), projection = Option.empty[JsObject])
        .cursor[AnnotatedArticle](ReadPreference.primary)
        .collect[Seq](400, Cursor.FailOnError[Seq[AnnotatedArticle]]()))

    futureArticleList.map { article => Ok(Json.toJson(article)) }
  }

  /**
   * Action that maps articles from File to Json.
   * @return
   */
  def inMemoryArticleList() = Action { implicit request =>
    //only in development mode
    val path = "conf/resources/inMemoryArticles.json"
    val jsonString = readFile(path).reduce(_+_)

    //only in production mode
    //val jsonString = IOUtils.toString(env.resourceAsStream("resources/inMemoryArticles.json"), StandardCharsets.UTF_8)

    val jsResult = Json.parse(jsonString).validate[Seq[AnnotatedArticle]]
    jsResult.fold(
      error => {Ok(error.toString())},
      success => {Ok(Json.toJson(success))}
    )
  }

  /**
   * Action that defines Javascript routes.
   */
  def javascriptRoutes = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")
    (routes.javascript.AnnotatedArticleController.articleList,
      routes.javascript.AnnotatedArticleController.inMemoryArticleList,
      routes.javascript.HomeController.annotatedText)).as(MimeTypes.JAVASCRIPT)
  }
}

