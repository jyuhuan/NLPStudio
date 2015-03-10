package foundation.math.graph

import scala.collection.mutable
import foundation.sugar.OrderedCollection
import foundation.sugar.OrderedCollectionClasses._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/9/15.
 */

/**
 * An immutable tree data structure. No edge.
 * @param root The root of the tree to initialize the tree.
 *             Prepare the root with its children and descendants before passing in.
 * @tparam T The type of data. If your node is Node[T], then fill whatever T is here.
 */
class Tree[T](root: Node[T]) {
  /**
   * Traverse the sub-tree starting from a given node.
   * @param node The root of the sub-tree.
   * @param conditionToKeep A condition for the traversal algorithm to decide whether to keep the node in the result list.
   * @param isDepthFirst If true, the resulting list of nodes will be in a depth-first ordering. Otherwise, breadth-first.
   * @return An iterable of all nodes that satisfy the condition to keep.
   */
  private def basicTraverse(node: Node[T], conditionToKeep: Node[T] ⇒ Boolean, isDepthFirst: Boolean): Iterable[Node[T]] = {
    val allNodes = mutable.ListBuffer[Node[T]]()
    val fringe: OrderedCollection[Node[T]] = if (isDepthFirst) mutable.Stack(node) else mutable.Queue(node)
    while (fringe.notEmpty()) {
      val top = fringe.dequeue()
      if (conditionToKeep(top)) allNodes += top
      val successors = childrenOf(top)
      fringe enqueueAll successors.reverse
    }
    allNodes
  }

  /**
   * Get all nodes in the tree by depth-first or breadth-first order.
   * @param isDepthFirst If true, the resulting iterable of nodes will be in a depth-first ordering. Otherwise, breadth-first.
   * @return
   */
  def traverse(isDepthFirst: Boolean = true): Iterable[Node[T]] = basicTraverse(root, n ⇒ true, isDepthFirst)

  /**
   * Get the children of the given node.
   * @param node The node of interest.
   * @return The iterable of children nodes.
   */
  def childrenOf(node: Node[T]) = node.children

  /**
   * Get the parent of the given node.
   * @param node The node of interest.
   * @return The parent node.
   */
  def parentOf(node: Node[T]) = node.parent

  /**
   * Get all leaves of the tree in a depth-first order.
   * @return All leaves, from left to right.
   */
  def leaves(): Iterable[Node[T]] = basicTraverse(root, n ⇒ n.isLeaf, true)

  /**
   * Get all leaves of a sub-tree starting from the given node in a depth-first order.
   * @param node The root of the sub-tree.
   * @return All leaves of the sub-tree, from left to right.
   */
  def leavesOf(node: Node[T]): Iterable[Node[T]] = basicTraverse(node, n ⇒ n.isLeaf, true)

//  def pathBetween(nodeA: Node[T], nodeB: Node[T])
//  def asGraph[TEdge](): Graph[T, TEdge]
}
