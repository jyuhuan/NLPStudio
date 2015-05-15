package nlpstudio.core

import nlpstudio.tools.tokenizers.StanfordTokenizer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
class Phrase(val words: Iterable[Int])(implicit cb: Codebook) extends Iterable[Int] {
  override def iterator: Iterator[Int] = words.iterator

  override def equals(that: Any): Boolean = {
    that match {
      case that: Phrase ⇒ this.words == that.words
      case _ ⇒ false
    }
  }

  override def hashCode: Int = words.hashCode()

  override def toString: String = words.map(x ⇒ cb(x)).mkString(" ")
}

object Phrase {
  def apply(phraseString: String)(implicit cb: Codebook): Phrase = {
    val words = StanfordTokenizer.tokenize(phraseString)
    new Phrase(words)
  }
}