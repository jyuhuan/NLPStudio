package foundation.math.graph

import scala.collection._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 2/4/15.
 */
class Graph[TVertex, TEdge] {

  val childTable = mutable.ListMap[TVertex, mutable.ListMap[TVertex, TEdge]]()
  val parentTable = mutable.ListMap[TVertex, mutable.ListMap[TVertex, TEdge]]()

  def this(adjacencyList: mutable.HashMap[TVertex, Seq[(TVertex, TEdge)]], evaluateEdge: TEdge ⇒ Double) {
    this()
    for (vertexAndChildren ← adjacencyList) {
      val vertex = vertexAndChildren._1
      val children = vertexAndChildren._2
      for (childAndEdge ← children) {
        val child = childAndEdge._1
        val edge = childAndEdge._2
        addDirectedEdge(vertex, edge, child)
      }
    }
  }

  def this(edges: Seq[(TVertex, TEdge, TVertex)]) {
    this()
    for (edge ← edges) {
      addDirectedEdge(edge._1, edge._2, edge._3)
    }
  }

  def addDirectedEdge(parentNode: TVertex, edge: TEdge, childNode: TVertex) {
    if (childTable contains parentNode) {
      childTable(parentNode).put(childNode, edge)
    }
    else {
      childTable.put(parentNode, mutable.ListMap(childNode → edge))
    }

    if (parentTable contains childNode) {
      parentTable(childNode).put(parentNode, edge)
    }
    else {
      parentTable.put(childNode, mutable.ListMap(parentNode → edge))
    }
  }

}

