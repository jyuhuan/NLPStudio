package nlpstudio.core

import foundation.graph.Node

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
class DependencyParseNode(val word: Int, val index: Int, parse: DependencyParse)(implicit cb: Codebook) extends Node[Int] {
  override def data: Int = word

  override def children: Seq[Node[Int]] = parse.dependentsOf(this)

  override def parent: Node[Int] = parse.governorOf(this)

  override def toString: String = "[" + index + "] " + cb(word)

  override def equals(that: Any): Boolean = {
    that match {
      case that: DependencyParseNode ⇒ (this.index == that.index) && (this.word == that.word)
      case _ ⇒ false
    }
  }

  override def hashCode: Int = {
    var hashCode = 17
    hashCode = hashCode * 23 + this.index
    hashCode = hashCode * 23 + this.word.hashCode
    hashCode
  }
}
