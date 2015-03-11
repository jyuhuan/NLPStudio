package nlpstudio.io.corpora.PennTreebank
import foundation.math.graph.Tree

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/10/15.
 */

/**
 * An entry of Penn Treebank 3 corpus.
 * Each entry corresponds to one parse tree.
 * @param tree The parse tree.
 */
case class PennTreebankEntry(tree: Tree[String]) {
  override def toString() = tree.leaves().mkString(" ")
}
