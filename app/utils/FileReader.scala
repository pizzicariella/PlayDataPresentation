package utils

import entities.AnnotatedArticle
import play.api.libs.json.Json
import java.io.{File, FileInputStream}
import scala.io.Source

object FileReader {

  def readFile(path: String): List[String] = {
    val bufferedSource = Source.fromFile(path)
    val lines = bufferedSource.getLines().toList
    bufferedSource.close()
    lines
  }
}
