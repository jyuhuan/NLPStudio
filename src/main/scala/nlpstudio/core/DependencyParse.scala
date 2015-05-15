package nlpstudio.core

import foundation.graph.Graph

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
class DependencyParse extends Graph[DependencyParseNode, WordDependency] {
  def add(dependency: WordDependency) = this.addDirectedEdge(dependency.governor, dependency, dependency.dependent)
  def dependentsOf(node: DependencyParseNode): Array[DependencyParseNode] = this.childTable(node).map(_._1).toArray
  def governorOf(node: DependencyParseNode): DependencyParseNode = {
    val graphParents = this.parentTable(node)
    if (graphParents.isEmpty)  null
    else graphParents.head._1
  }
}

object DependencyParse {
  def apply(dependencies: TraversableOnce[WordDependency]): DependencyParse = {
    val result = new DependencyParse()
    dependencies.foreach(dep ⇒ result.add(dep))
    result
  }
}