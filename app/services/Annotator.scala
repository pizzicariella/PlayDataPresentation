package services

import com.google.inject.ImplementedBy
import com.johnsnowlabs.nlp.LightPipeline
import entities.AnnotatedArticle
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession

import javax.inject._

@ImplementedBy(classOf[PosPipeAnnotator])
trait Annotator {
  def annotate(text: String): String
}

@Singleton
class PosPipeAnnotator extends Annotator {

  val sc: SparkSession = SparkSession
    .builder()
    .appName("PlayDataPresentation")
    .master("local[*]")
    .getOrCreate()


  override def annotate(text: String): String = {
    val model = PipelineModel.load("resources/posPipelineModel")
    val map = new LightPipeline(model).annotate(text)
    map.getOrElse("pos", Seq[String]("error")).head
  }
}
