package foundation.math.graph

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/9/15.
 */
trait Graph[TVertex, TEdge] {
  //def pathFromTo(fromNode: Node[TVertex], toNode: Node[TVertex]): Path[Node[TVertex], TEdge]
  def outgoingNodesOf(node: Node[TVertex]): Seq[Node[TVertex]]
  def incomingNodesOf(node: Node[TVertex]): Seq[Node[TVertex]]
}
