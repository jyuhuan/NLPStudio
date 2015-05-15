package nlpstudio.core

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
trait DirectedLink[T] {
  def name: String
  def from: T
  def to: T

  override def equals(that: Any): Boolean = {
    that match {
      case that: DirectedLink[T] ⇒ (this.name == that.name) && (this.from == that.from) && (this.to == that.to)
      case _ ⇒ false
    }
  }

  override def hashCode: Int = {
    var hashCode = 17
    hashCode = hashCode * 23 + this.name.hashCode
    hashCode = hashCode * 23 + this.from.hashCode
    hashCode = hashCode * 23 + this.to.hashCode
    hashCode
  }

}
