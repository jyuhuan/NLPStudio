package nlpstudio.core

import nlpstudio.tools.tokenizers.StanfordTokenizer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
class Sentence(words: Iterable[Int])(implicit cb: Codebook) extends Phrase(words)

object Sentence {
  def apply(sentenceString: String)(implicit cb: Codebook): Sentence = {
    val words = StanfordTokenizer.tokenize(sentenceString)
    new Sentence(words)
  }
}