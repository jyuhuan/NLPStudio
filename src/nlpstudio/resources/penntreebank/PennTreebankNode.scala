package nlpstudio.resources.penntreebank


import foundation.math.graph.Node
import nlpstudio.resources.core.Rule


import scala.collection.mutable
import scala.collection.mutable.ListBuffer


/**
 * Created by yuhuan on 3/17/15.
 */
class PennTreebankNode private(var depth: Int,
                               var data: String,
                               var labels: Seq[String],
                               var parentNode: PennTreebankNode,
                               var childrenNodes: mutable.Buffer[PennTreebankNode]) extends Node[String] {

  override def parent: Node[String] = parentNode
  override def children: Seq[Node[String]] = childrenNodes

  override def isLeaf: Boolean = childrenNodes.isEmpty

  def addChildren(newNode: PennTreebankNode) = {
    newNode.parentNode = this
    this.childrenNodes += newNode
  }

  override def toString = data.toString

//  override def toString = {
//    if (isLeaf) data.toString
//    data.toString + " => " + leaves.map(n ⇒ n.asInstanceOf[PennTreebankNode].data).mkString(" ")
//  }

  var posTag: String = null

  def syntacticCategory = this.data
  def head = ???
  def headWord = ???
  def semanticHead = ???
  def firstWord = ???
  def lastWord = ???
  def words = this.leaves
  def rule = Rule(this.syntacticCategory, this.childrenNodes.map(_.syntacticCategory))
  def isWord = this.children.length == 0
  def firstPos = ???
  def lastPos = ???
  def isNullElement: Boolean = {
    if (isLeaf) syntacticCategory == SpecialCategories.nullElement
    else childrenNodes.forall(n ⇒ n.isNullElement)
  }
}

object PennTreebankNode {
  def apply(depth: Int,
            data: String,
            labels: Seq[String],
            parent: PennTreebankNode,
            children: mutable.Buffer[PennTreebankNode]) = {

    if (children == null) {
      new PennTreebankNode(depth, data, labels, parent, new ListBuffer[PennTreebankNode])
    }
    else {
      new PennTreebankNode(depth, data, labels, parent, children)
    }

  }
}