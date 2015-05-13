package foundation.math.graph

import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/9/15.
 */
/**
 * An n-ary node. Provides a depth first iterator.
 * @param data
 * @tparam T
 */

trait Node[T] {
  def data: T
  def parent: Node[T]
  def children: Seq[Node[T]]

  def isLeaf: Boolean = children.isEmpty


  /**
   * Traverse the sub-tree starting from a given node.
   * @param node The root of the sub-tree.
   * @param conditionToKeep A condition for the traversal algorithm to decide whether to keep the node in the result list.
   * @param isDepthFirst If true, the resulting list of nodes will be in a depth-first ordering. Otherwise, breadth-first.
   * @return An iterable of all nodes that satisfy the condition to keep.
   */
  def traverse(childrenOf: Node[T] ⇒ Seq[Node[T]],
               conditionToKeep: Node[T] ⇒ Boolean,
               shouldStop: Node[T] ⇒ Boolean,
               action: Node[T] ⇒ Unit): Seq[Node[T]] = {
    val allNodes = mutable.ListBuffer[Node[T]]()
    val startNode = this
    val fringe = mutable.Stack(startNode)
    var finished = false
    while (!finished && fringe.nonEmpty) {
      val top = fringe.pop()
      if (conditionToKeep(top)) {
        allNodes += top
        action(top)
      }
      if (shouldStop(top)) finished = true
      val successors = childrenOf(top)
      fringe pushAll successors.reverse
    }
    allNodes
  }

  def traverse(): Iterable[Node[T]] = traverse(n ⇒ n.children, n ⇒ true, n ⇒ false, n ⇒ Unit)
  def traverse(action: Node[T] ⇒ Unit): Iterable[Node[T]] = traverse(n ⇒ n.children, n ⇒ true, n ⇒ false, action)

  def leaves = traverse(n ⇒ n.children, n ⇒ n.isLeaf, n ⇒ false, n ⇒ Unit).toArray
  def leaves(action: Node[T] ⇒ Unit) = traverse(n ⇒ n.children, n ⇒ n.isLeaf, n ⇒ false, action).toArray

  def internalNodes = traverse(n ⇒ n.children, n ⇒ !n.isLeaf, n ⇒ false, n ⇒ Unit)

}

//object Node {
//  def apply[T](data: T, parent: Node[T], children: Node[T]*) = new Node[T](data, parent, children: _*)
//  def apply[T](data: T) = new Node[T](data)
//}