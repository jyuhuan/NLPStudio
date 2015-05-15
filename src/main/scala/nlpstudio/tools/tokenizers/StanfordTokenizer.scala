package nlpstudio.tools.tokenizers

import java.io.StringReader
import nlpstudio.core._

import scala.collection.JavaConversions._
import edu.stanford.nlp.process.DocumentPreprocessor

import nlpstudio.core.ImplicitCodebooks._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
object StanfordTokenizer {
  def tokenize(sentence: String)(implicit cb: Codebook): Array[Int] = {
    val stringReader = new StringReader(sentence)
    val documentPreprocessor = new DocumentPreprocessor(stringReader)
    documentPreprocessor.head.map(x ⇒ cb(x.word())).toArray
  }
}
