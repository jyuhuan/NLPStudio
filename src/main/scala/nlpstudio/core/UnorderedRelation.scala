package nlpstudio.core

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
case class UnorderedRelation[T](source: T, relationName: String, target: T) extends UndirectedLink[T] {
  override def name: String = relationName
  override def x: T = source
  override def y: T = target

  override def toString = name + "(" + x.toString + ", " + y.toString + ")"
}