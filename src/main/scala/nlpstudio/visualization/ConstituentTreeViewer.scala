package nlpstudio.visualization

import foundation.problems.search.SimpleSearcher
import nlpstudio.resources.penntreebank.PennTreebankNode
import nlpstudio.tools.parsers.StanfordConstituentParser
import nlpstudio.visualization.core.GraphViewer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
object ConstituentTreeViewer extends App {
  val parser = new StanfordConstituentParser()
  while (true) {
    println("Type your sentence here: ")
    val text = scala.io.StdIn.readLine()
    val parseTree = parser.parse(text)

    val searcher = SimpleSearcher.depthFirstSearch(parseTree.root, x ⇒ false, (n: PennTreebankNode) ⇒ n.childrenNodes)


    //val viewer = new GraphViewer(parseTree)
    //viewer.showWindow()
  }
}
