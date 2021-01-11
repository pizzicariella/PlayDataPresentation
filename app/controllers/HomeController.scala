package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport

import play.api.routing.JavaScriptReverseRouter
import play.mvc.Http.MimeTypes

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(implicit ec: ExecutionContext,
                               cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {


  val textForm = Form(single("text" -> nonEmptyText))
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index())
  }

  def corpus = Action { implicit request =>
    Ok(views.html.corpus())
  }

  def analyze = Action { implicit request =>
    Ok(views.html.analyze(textForm))
  }

  def analyzeText = Action { implicit request =>

    //val textValue = textForm.bind(Map("text" -> "testText")).get
    textForm.bindFromRequest.fold(
      hasErrors => {
        //TODO handle errors
        BadRequest(views.html.analyze(textForm))
    },
      textData => {
        //TODO
        Ok(views.html.analyze(textForm))
      }
    )
  }

  def info = Action {
    Ok(views.html.info())
  }

}
