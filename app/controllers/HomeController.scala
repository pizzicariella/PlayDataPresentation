package controllers

import entities.AnnotatedToken

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.routing.JavaScriptReverseRouter
import play.mvc.Http.MimeTypes
import services.Annotator

import scala.concurrent.ExecutionContext

case class TextToTag(text: String)

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(implicit ec: ExecutionContext,
                               cc: MessagesControllerComponents,
                               annotator: Annotator) extends MessagesAbstractController(cc) {

  var textForm = Form(mapping("text" -> text)(TextToTag.apply)(TextToTag.unapply))
  var at: AnnotatedToken = null
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
          at = annotator.annotate(data.text)
          val filledForm = textForm.fill(TextToTag(data.text))
          textForm = filledForm
          //val filledForm = textForm.fill(TextToTag(test))
          //Redirect(routes.HomeController.analyze(filledForm))
          annotatedText
          //Ok(views.html.analyze(filledForm))
          Redirect(routes.HomeController.analyze())
        }
    )
  }

  def annotatedText() = Action { implicit request =>
    Ok(Json.toJson(at))
  }

  def info = Action {
    Ok(views.html.info())
  }

}
