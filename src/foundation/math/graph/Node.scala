package foundation.math.graph

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable
/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/9/15.
 */
/**
 * An n-ary node. Provides a depth first iterator.
 * @param data
 * @tparam T
 */
case class Node[T](var data: T) {
  /**
   * The parent node.
   * this is in this.parent.children
   */
  var parent: Node[T] = null
  var children: mutable.Buffer[Node[T]] = new mutable.ListBuffer[Node[T]]()

  def this(data: T, parent: Node[T], children: mutable.Buffer[Node[T]]) {
    this(data)
    this.parent = parent
    this.children = children
  }

  def isLeaf = children.isEmpty

  def addChild(dataOfNewChild: T) = {
    val newChild = new Node[T](dataOfNewChild, this, new mutable.ListBuffer[Node[T]]())
    children += newChild
    newChild
  }

  def -->(dataOfNewChild: T) = addChild(dataOfNewChild)

  def addChild(newChild: Node[T]) = {
    children += newChild
    newChild
  }

  def -->(newChild: Node[T]) = addChild(newChild)

  def addChildren(dataSeqofNewChildres: TraversableOnce[T]) = {
    children ++= dataSeqofNewChildres.map(d ⇒ new Node[T](d, this, new mutable.ListBuffer[Node[T]]()))
  }

  def ==>(dataSeqofNewChildres: TraversableOnce[T]) = addChildren(dataSeqofNewChildres)

  override def toString() = {
    if (children.length == 0) {
      data.toString
    }
    else {
      data.toString + " => " + children.map(n ⇒ n.data).mkString(" ")
    }
  }
}