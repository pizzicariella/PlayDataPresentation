package services

import com.google.inject.ImplementedBy
import com.johnsnowlabs.nlp.LightPipeline
import entities.AnnotatedText
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession

import javax.inject._

@ImplementedBy(classOf[PosPipeAnnotator])
trait Annotator {
  def annotate(text: String): AnnotatedText
}

@Singleton
class PosPipeAnnotator extends Annotator {

  //TODO lazy creation. better to create eagerly on app start up?
  val sc: SparkSession = SparkSession
    .builder()
    .appName("PlayDataPresentation")
    .master("local[*]")
    .config("spark.testing.memory", "2147480000")
    .config("spark.driver.host", "127.0.0.1")
    .config("spark.driver.bindAddress", "127.0.0.1")
    .getOrCreate()


  override def annotate(text: String): AnnotatedText = {
    val model = PipelineModel.load("conf/resources/posPipelineModel")
    val map = new LightPipeline(model).annotate(text)
    val normalized = map.getOrElse("normalized", Seq[String]("error"))
    val pos = map.getOrElse("pos", Seq[String]("error"))
    val lemmas = map.getOrElse("lemma", Seq[String]("error"))
    AnnotatedText(text, normalized, pos, lemmas)
  }
}
