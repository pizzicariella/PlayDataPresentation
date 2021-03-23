package services

import com.google.inject.ImplementedBy
import com.johnsnowlabs.nlp.LightPipeline
import entities.AnnotatedText
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession

import javax.inject._

@ImplementedBy(classOf[PosPipeAnnotator])
trait Annotator {
  /**
   * Annotates given Text according to a PipelineModel.
   * @param text Text to annotate
   * @return an instance of AnnotatedText, containing annotations
   */
  def annotate(text: String): AnnotatedText
}

@Singleton
class PosPipeAnnotator extends Annotator {

  val sc: SparkSession = SparkSession
    .builder()
    .appName("PlayDataPresentation")
    .master("local[*]")
    .config("spark.testing.memory", "2147480000")
    .config("spark.driver.host", "127.0.0.1")
    .config("spark.driver.bindAddress", "127.0.0.1")
    .getOrCreate()

  val model = PipelineModel.load("conf/resources/posPipelineModel")

  override def annotate(text: String): AnnotatedText = {
    val map = new LightPipeline(model).annotate(text)
    val normalized = map.getOrElse("normalized", Seq[String]("error"))
    val pos = map.getOrElse("pos", Seq[String]("error"))
    val lemmas = map.getOrElse("lemma", Seq[String]("error"))
    AnnotatedText(text, normalized, pos, lemmas)
  }
}
