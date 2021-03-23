package utils

import scala.io.Source

object FileReader {

  /**
   * Reads File into list of lines from given input path.
   * @param path
   * @return List containing lines of file as String
   */
  def readFile(path: String): List[String] = {
    val bufferedSource = Source.fromFile(path)
    val lines = bufferedSource.getLines().toList
    bufferedSource.close()
    lines
  }
}
