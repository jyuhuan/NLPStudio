package nlpstudio.core

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
case class OrderedRelation[T](source: T, relationName: String, target: T) extends DirectedLink[T] {
  override def name: String = relationName
  override def from: T = source
  override def to: T = target

  override def toString = name + "(" + from.toString + ", " + to.toString + ")"
}
