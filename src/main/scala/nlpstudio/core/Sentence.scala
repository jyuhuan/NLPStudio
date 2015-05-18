package nlpstudio.core

import nlpstudio.core.GlobalCodebooks._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
class Sentence(words: Seq[Int]) extends Phrase(words) {
  def apply(index: Int): Int = words(index)
}

object Sentence {
  def apply(sentenceString: String): Sentence = {
    val words = sentenceString.split(' ').map(x ⇒ cb(x))
    new Sentence(words)
  }

  def ofStringWords(stringWords: Seq[String]): Sentence = new Sentence(stringWords.map(w ⇒ cb(w)))

  def apply(words: Seq[Int]) = new Sentence(words)
}