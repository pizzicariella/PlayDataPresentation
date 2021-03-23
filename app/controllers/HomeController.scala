package controllers

import entities.{AnnotatedText, TextToTag}
import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import services.Annotator
import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(implicit ec: ExecutionContext,
                               cc: MessagesControllerComponents,
                               annotator: Annotator) extends MessagesAbstractController(cc) {

  var textForm = Form(mapping("text" -> nonEmptyText)(TextToTag.apply)(TextToTag.unapply))
  var at: AnnotatedText = null
  var loadAnnos: String = "f"

  /**
   * Action to render index page.
   */
  def index = Action {
    Ok(views.html.index())
  }

  /**
   * Action to render corpus page.
   */
  def corpus = Action {
    Ok(views.html.corpus())
  }

  /**
   * Action to render analyze page.
   */
  def analyze() = Action { implicit request =>
    val textFormToLoad = textForm
    val loadAnnosToLoad = loadAnnos
    textForm = textForm.fill(TextToTag(""))
    loadAnnos = "f"
    Ok(views.html.analyze(textFormToLoad, loadAnnosToLoad))
  }

  /**
   * Action to validate user input. Calls annotator and annotatedText action and redirects to analyze page on valid
   * input. Returns BadRequest on invalid input.
   */
  def validateTextForm = Action { implicit request =>
    textForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.analyze(formWithErrors, "f")),
        data => {
          at = annotator.annotate(data.text)
          val filledForm = textForm.fill(TextToTag(data.text))
          textForm = filledForm
          loadAnnos = "t"
          annotatedText
          Redirect(routes.HomeController.analyze())
        }
    )
  }

  /**
   * Action that maps annotations to json.
   */
  def annotatedText() = Action { implicit request =>
    Ok(Json.toJson(at))
  }

  /**
   * Action that renders info page.
   */
  def info = Action {
    Ok(views.html.info())
  }
}
