package nlpstudio.core

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
class Word(stringForm: String)(implicit codebook: Codebook) {
  val index = if (codebook.isFrozen) Codebook.unknownIndex else codebook(stringForm)

  override def equals(that: Any): Boolean = {
    that match {
      case that: Word ⇒ this.index == that.index
      case _ ⇒ false
    }
  }

  override def hashCode: Int = index.hashCode()

  override def toString: String = stringForm
}
