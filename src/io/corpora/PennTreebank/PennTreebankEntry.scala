package io.corpora.PennTreebank
import foundation.math.graph.Tree

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/10/15.
 */

/**
 *
 * @param tree
 */
case class PennTreebankEntry(tree: Tree[String]) {
  val sentence = tree.leaves().mkString(" ")
  override def toString() = sentence
}
