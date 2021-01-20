package services

import com.google.inject.ImplementedBy
import com.johnsnowlabs.nlp.LightPipeline
import entities.AnnotatedToken
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession

import javax.inject._

@ImplementedBy(classOf[PosPipeAnnotator])
trait Annotator {
  def annotate(text: String): AnnotatedToken
}

@Singleton
class PosPipeAnnotator extends Annotator {

  //TODO lazy creation. better to create eagerly on app start up?
  val sc: SparkSession = SparkSession
    .builder()
    .appName("PlayDataPresentation")
    .master("local[*]")
    .config("spark.testing.memory", "2147480000")
    .getOrCreate()


  override def annotate(text: String): AnnotatedToken = {
    val model = PipelineModel.load("resources/posPipelineModel")
    val map = new LightPipeline(model).annotate(text)
    val normalized = map.getOrElse("normalized", Seq[String]("error"))
    val pos = map.getOrElse("pos", Seq[String]("error"))
    AnnotatedToken(text, pos, normalized)
  }
}
