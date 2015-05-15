package nlpstudio.tools.tokenizers

import java.io.StringReader
import nlpstudio.core.Word

import scala.collection.JavaConversions._
import edu.stanford.nlp.process.DocumentPreprocessor

import nlpstudio.core.ImplicitCodebooks._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
object StanfordTokenizer {
  def tokenize(sentence: String): Array[Word] = {
    val stringReader = new StringReader(sentence)
    val documentPreprocessor = new DocumentPreprocessor(stringReader)
    documentPreprocessor.head.map(x ⇒ new Word(x.word())).toArray
  }
}
