package nlpstudio.core

import nlpstudio.core.GlobalCodebooks._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
class Phrase(val words: Seq[Int]) extends Iterable[Int] {
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
  def apply(phraseString: String): Phrase = {
    val words = phraseString.split(' ').map(x ⇒ cb(x))
    new Phrase(words)
  }

  def apply(words: Seq[Int]): Phrase = new Phrase(words)
}