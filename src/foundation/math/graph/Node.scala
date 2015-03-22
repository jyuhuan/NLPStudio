package foundation.math.graph

import foundation.sugar.OrderedCollection

import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/9/15.
 */
/**
 * An n-ary node. Provides a depth first iterator.
 * @param data
 * @tparam T
 */

import foundation.sugar.OrderedCollection
import foundation.sugar.OrderedCollectionClasses._

trait Node[T] {
  def parent: Node[T]
  def children: Seq[Node[T]]
  def isLeaf: Boolean


  /**
   * Traverse the sub-tree starting from a given node.
   * @param node The root of the sub-tree.
   * @param conditionToKeep A condition for the traversal algorithm to decide whether to keep the node in the result list.
   * @param isDepthFirst If true, the resulting list of nodes will be in a depth-first ordering. Otherwise, breadth-first.
   * @return An iterable of all nodes that satisfy the condition to keep.
   */
  def traverse(childrenOf: Node[T] ⇒ Seq[Node[T]], conditionToKeep: Node[T] ⇒ Boolean, isDepthFirst: Boolean): Seq[Node[T]] = {
    val allNodes = mutable.ListBuffer[Node[T]]()
    val startNode = this
    val fringe: OrderedCollection[Node[T]] = if (isDepthFirst) mutable.Stack(startNode) else mutable.Queue(startNode)
    while (fringe.notEmpty()) {
      val top = fringe.dequeue()
      if (conditionToKeep(top)) allNodes += top
      val successors = childrenOf(top)
      fringe enqueueAll successors.reverse
    }
    allNodes
  }

  def traverse(isDepthFirst: Boolean = true): Iterable[Node[T]] = traverse(n ⇒ n.children, n ⇒ true, isDepthFirst)

  def leaves = traverse(n ⇒ n.children, n ⇒ n.isLeaf, isDepthFirst = true).toArray

  def internalNodes = traverse(n ⇒ n.children, n ⇒ !n.isLeaf, isDepthFirst = true)

}

//object Node {
//  def apply[T](data: T, parent: Node[T], children: Node[T]*) = new Node[T](data, parent, children: _*)
//  def apply[T](data: T) = new Node[T](data)
//}