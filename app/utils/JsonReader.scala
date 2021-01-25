package utils

import entities.AnnotatedArticle
import play.api.libs.json.Json
import java.io.{File, FileInputStream}
import scala.io.Source

object JsonReader {

  def parseJsonFromFile(path: String): List[AnnotatedArticle] = {
    val file = new File(path)
    val stream = new FileInputStream(file)
    val articles = try {
      Json.parse(stream).as[List[AnnotatedArticle]]
    } finally {
      stream.close()
    }
    println(articles)
    articles
  }

  def readJsonFile(path: String): List[String] = {
    val bufferedSource = Source.fromFile(path)
    val lines = bufferedSource.getLines().toList
    bufferedSource.close()
    lines
  }

  def parseJson(line: String): AnnotatedArticle = {
    val json = Json.parse(line)
    json.as[AnnotatedArticle]
  }

}
