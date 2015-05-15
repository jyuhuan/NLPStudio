package nlpstudio.core

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
trait UndirectedLink[T] {
  def name: String
  def x: T
  def y: T

  override def equals(that: Any): Boolean = {
    that match {
      case that: UndirectedLink[T] ⇒ (this.name == that.name) && ((this.x == that.x && this.y == that.y) || (this.x == that.y && this.y == that.x))
      case _ ⇒ false
    }
  }

  override def hashCode: Int = {
    var hashCode = 17
    hashCode = hashCode * 23 + this.name.hashCode
    hashCode = hashCode * 23 + (this.x.hashCode +this.y.hashCode())
    hashCode
  }

}
