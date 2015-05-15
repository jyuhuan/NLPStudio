package nlpstudio.core

import nlpstudio.tools.tokenizers.StanfordTokenizer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
class Sentence(sentenceString: String) extends Iterable[Word] {
  val words = StanfordTokenizer.tokenize(sentenceString)
  override def iterator: Iterator[Word] = words.iterator
}
