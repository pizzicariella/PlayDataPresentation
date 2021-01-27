package utils

import org.scalatestplus.play.PlaySpec

class FileReaderTest extends PlaySpec{

  "readFile" should {
    "read lines into List" in {
      val lines = FileReader.readFile("conf/resources/fileReaderTest")
      lines.size mustBe 3
      lines.head mustBe "Dies ist"
    }
  }

}
