package nlpstudio.io.corpora.penntreebank
import foundation.math.graph.{Node, Graph, Tree}
import foundation.sugar.OrderedCollection
import nlpstudio.utilities.UniqueNamer

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/10/15.
 */


/**
 * An entry of Penn Treebank 3 corpus.
 * Each entry corresponds to one parse tree.
 * @param tree The parse tree.
 */
case class PennTreebankEntry(sectionId: Int, mrgFileId: Int, sentenceId: Int, tree: Tree[String]) {

  override def toString() = leaves.mkString(" ")

  def leaves = tree.leaves

  def asGraph(): Graph[String, String] = {
    val adjacencyList = mutable.HashMap[String, Seq[(String, String)]]()
    val uniqueNamer = new UniqueNamer()
    val fringe = mutable.Queue((tree.root, tree.root.data))
    while (fringe.nonEmpty) {
      val top = fringe.dequeue()
      val left = top._2
      val successors = top._1.children.map(n ⇒ (n, uniqueNamer(n.data)))
      if (successors.length > 0) adjacencyList.put(left, successors.map(t ⇒ (t._2, "-")))
      fringe ++= successors
    }

    new Graph[String, String](adjacencyList, edge ⇒ 0)
  }
}
