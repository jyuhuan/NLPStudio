package nlpstudio.core

import nlpstudio.core.GlobalCodebooks._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
class Sentence(words: Iterable[Int]) extends Phrase(words)

object Sentence {
  def apply(sentenceString: String): Sentence = {
    val words = sentenceString.split(' ').map(x ⇒ cb(x))
    new Sentence(words)
  }

  def apply(words: Iterable[Int]) = new Sentence(words)
}