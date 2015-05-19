package nlpstudio.core

import foundation.graph.Graph
import scala.collection.mutable
/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
class DependencyParse extends Graph[DependencyParseNode, WordDependency] {
  val nodes = mutable.Map[Int, DependencyParseNode]()

  def add(dependency: WordDependency) = {
    val governor = dependency.governor
    val dependent = dependency.dependent

    val govIdx = governor.index
    val depIdx = dependent.index
    if (!this.nodes.contains(govIdx)) nodes += (govIdx → governor)
    if (!this.nodes.contains(depIdx)) nodes += (depIdx → dependent)

    this.addDirectedEdge(governor, dependency, dependent)
  }

  def dependentsOf(node: DependencyParseNode): Array[DependencyParseNode] = this.childTable(node).map(_._1).toArray

  def governorOf(node: DependencyParseNode): DependencyParseNode = {
    val graphParents = this.parentTable(node)
    if (graphParents.isEmpty)  null
    else graphParents.head._1
  }

  def apply(index: Int): DependencyParseNode = {
    if (!this.nodes.contains(index)) {
      val b = 0
    }
    this.nodes(index)
  }
}

object DependencyParse {
  def apply(dependencies: TraversableOnce[WordDependency]): DependencyParse = {
    val result = new DependencyParse()
    dependencies.foreach(dep ⇒ result.add(dep))
    result
  }
}