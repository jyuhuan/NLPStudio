package nlpstudio.resources.penntreebank


import foundation.math.graph.Node
import nlpstudio.resources.HeadFinders.CollinsHeadFinder
import nlpstudio.resources.core.Rule


import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer}


/**
 * Created by yuhuan on 3/17/15.
 */
class PennTreebankNode private(var depth: Int,
                               var data: String,
                               var labels: Seq[String],
                               var parentNode: PennTreebankNode,
                               var childrenNodes: mutable.ArrayBuffer[PennTreebankNode]) extends Node[String] {

  override def parent: Node[String] = parentNode
  override def children: Seq[Node[String]] = childrenNodes

  override def isLeaf: Boolean = childrenNodes.isEmpty

  def addChildren(newNode: PennTreebankNode) = {
    newNode.parentNode = this
    this.childrenNodes += newNode
  }

  def apply(idx: Int) = childrenNodes(idx)

  override def toString = data.toString

//  override def toString = {
//    if (isLeaf) data.toString
//    data.toString + " => " + leaves.map(n ⇒ n.asInstanceOf[PennTreebankNode].data).mkString(" ")
//  }

  var posTag: String = null

  def index = this.parentNode.children.indexOf(this)

  def syntacticCategory = if (posTag != null) posTag else this.data
  def syntaxHead = CollinsHeadFinder(this)
  def syntaxHeadWord = {
    if (isWord) this
    else {
      var reachedTheBottom = false
      var cur = this
      while (!reachedTheBottom) {
        cur = cur.syntaxHead
        if (cur.isWord) reachedTheBottom = true
      }
      cur
    }
  }
  def semanticHead = ???
  def firstWord = ???
  def lastWord = ???
  def wordNodes = this.leaves.map(n ⇒ n.asInstanceOf[PennTreebankNode])
  def rule = Rule(this.syntacticCategory, this.childrenNodes.map(_.syntacticCategory))
  def isWord = this.isLeaf
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
            children: mutable.ArrayBuffer[PennTreebankNode]) = {

    if (children == null) {
      new PennTreebankNode(depth, data, labels, parent, new ArrayBuffer[PennTreebankNode])
    }
    else {
      new PennTreebankNode(depth, data, labels, parent, children)
    }

  }
}