package nlpstudio.visualization


/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 2/7/15.
 */

import nlpstudio.tools.parsers.StanfordDependencyParser
import nlpstudio.visualization.core.GraphViewer

object DependencyTreeViewer extends App {
  val parser = new StanfordDependencyParser()
  while (true) {
    println("Type your sentence here: ")
    val text = scala.io.StdIn.readLine()
    val parseTree = parser.parse(text)
    val viewer = new GraphViewer(parseTree)
    viewer.showWindow()
  }
}
