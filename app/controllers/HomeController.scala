package controllers

import daos.AnnotatedArticleDao
import javax.inject._
import play.api.mvc._
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(implicit ec: ExecutionContext,
                               cc: ControllerComponents,
                               dao: AnnotatedArticleDao) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index())
  }

  def corpus = Action {
    Ok(views.html.corpus("News Corpus"))
  }

  def analyze = Action {
    Ok(views.html.analyze())
  }

  def info = Action {
    Ok(views.html.info())
  }

  def getArticles: Action[AnyContent] = Action.async {
    dao.listArticles().map(articles => Ok(Json.toJson(articles)))
  }

}
