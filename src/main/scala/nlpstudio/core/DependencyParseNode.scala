package nlpstudio.core

import foundation.graph.Node

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
class DependencyParseNode(val word: Word, val index: Int, parse: DependencyParse) extends Node[Word] {
  override def data: Word = word

  override def children: Seq[Node[Word]] = parse.dependentsOf(this)

  override def parent: Node[Word] = parse.governorOf(this)

  override def toString: String = "[" + index + "] " + word.toString

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
