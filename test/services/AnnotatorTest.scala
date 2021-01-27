package services

import entities.AnnotatedToken
import org.scalatestplus.play.PlaySpec

//This class ist not testing for correct results, as results depend on the model which should not be tested
class AnnotatorTest extends PlaySpec{

  "PosPipeAnnotator annotate" should {
    "create AnnotatedToken" in {
      val annotator = new PosPipeAnnotator
      val at = annotator.annotate("Das ist ein Test Text.")
      val atInstance = at.isInstanceOf[AnnotatedToken]
      atInstance mustBe true
    }
  }

}