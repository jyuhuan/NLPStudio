package nlpstudio.visualization

import foundation.graph.Graph
import foundation.problems.search.SimpleSearcher
import nlpstudio.resources.penntreebank.PennTreebankNode
import nlpstudio.tools.parsers.StanfordConstituentParser
import nlpstudio.visualization.core.GraphViewer
import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
object ConstituentTreeViewer extends App {
  val parser = new StanfordConstituentParser()
  while (true) {
    println("Type your sentence here: ")
    val text = scala.io.StdIn.readLine()
    val parseTree = parser.parse(text)

    val graph = new Graph[String, String]()
    val fringe = mutable.Stack(parseTree.root)
    while (fringe.nonEmpty) {
      val top = fringe.pop()
      for (child ← top.childrenNodes) {
        graph.addDirectedEdge(top.content, "", child.content)
        // TODO: does not display leaf nodes.
        // TODO: if the sentence contains two or more words that are the same, the graph will not be correct
      }
    }
    val viewer = new GraphViewer(graph)
    viewer.showWindow()
  }
}
