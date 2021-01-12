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

case class TextToTag(text: String)

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(implicit ec: ExecutionContext,
                               cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  val textForm = Form(mapping("text" -> text)(TextToTag.apply)(TextToTag.unapply))
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

  def analyze() = Action { implicit request =>
    Ok(views.html.analyze(textForm))
  }

  def validateTextForm = Action { implicit request =>
    textForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.analyze(formWithErrors)),
        data => {
          val filledForm = textForm.fill(TextToTag(data.text))
          //Redirect(routes.HomeController.analyze(filledForm))
          Ok(views.html.analyze(filledForm))
        }
    )
  }

  def info = Action {
    Ok(views.html.info())
  }

}
