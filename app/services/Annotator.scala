package services

import com.google.inject.ImplementedBy
import com.johnsnowlabs.nlp.LightPipeline
import entities.{AnnotatedArticle, AnnotatedToken}
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession

import javax.inject._

@ImplementedBy(classOf[PosPipeAnnotator])
trait Annotator {
  def annotate(text: String): AnnotatedToken
}

@Singleton
class PosPipeAnnotator extends Annotator {

  val sc: SparkSession = SparkSession
    .builder()
    .appName("PlayDataPresentation")
    .master("local[*]")
    .getOrCreate()


  override def annotate(text: String): AnnotatedToken = {
    val model = PipelineModel.load("resources/posPipelineModel")
    val map = new LightPipeline(model).annotate(text)
    println(map)
    val normalized = map.getOrElse("normalized", Seq[String]("error"))
    val pos = map.getOrElse("pos", Seq[String]("error"))
    AnnotatedToken(pos, normalized)
  }
}
