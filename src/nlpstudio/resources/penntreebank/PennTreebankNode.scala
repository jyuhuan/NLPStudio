package nlpstudio.resources.penntreebank


import foundation.math.graph.Node
import foundation.problems.search.{SearchNode, Searcher}
import nlpstudio.tools.headfinders.{NullElementHasNoHeadException, GerberSemanticHeadFinder, CollinsHeadFinder}
import nlpstudio.resources.core.Rule


import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


/**
 * Created by yuhuan on 3/17/15.
 */

/**
 * A node in a PennTreebank parse tree. Provides a wide range of methods good for feature extraction.
 *
 * @param depth Depth of the node in the tree. The root node's depth is 0.
 *              
 * @param content This is either
 *                <ol>
 *                  <li> the syntactic category (without functional labels like `"SBJ"`) for
 *                       internal nodes, or </li>
 *                  <li> surface words for leaf nodes. </li>
 *                </ol>
 *
 * @param functionalTags Functional tags. E.g., `["BNF", "SBJ", "LOC"]`, ...
 *
 * @param parentNode The parent PennTreebankNode. This is different from the `parent` property
 *                   inherited from the trait [[foundation.math.graph.Node Node]]. That `parent`
 *                   returns a `Node[String]` object, while `parentNode` is directly a
 *                   [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]] object,
 *                   saving the effort to do downward type casting.
 *
 * @param childrenNodes The children of this node. This is different from the `children` property
 *                   inherited from the trait [[foundation.math.graph.Node Node]]. That `children`
 *                   returns an immutable array of `Node[String]` objects, while this `childrenNodes`
 *                   is directly a mutable array of
 *                   [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]] objects,
 *                   saving the effort to do downward type casting.
 */
class PennTreebankNode private(var depth: Int,
                               var content: String,
                               var referenceId: Int,
                               var mapId: Int,
                               var functionalTags: Seq[String],
                               var parentNode: PennTreebankNode,
                               var childrenNodes: mutable.ArrayBuffer[PennTreebankNode]) extends Node[String] {

  // Conforming to the trait

  override def data: String = content
  override def parent: Node[String] = parentNode
  override def children: Seq[Node[String]] = childrenNodes
  override def isLeaf: Boolean = childrenNodes.isEmpty
  override def toString = data.toString


  // Actions

  /**
   * Adds a new [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]] to this node.
   * @param newNode A new [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]] node.
   * @return The new node added.
   */
  def addChild(newNode: PennTreebankNode) = {
    newNode.parentNode = this
    this.childrenNodes += newNode
    newNode
  }

  /**
   * Gets the child [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]] at a given index.
   * @param idx The index of the child.
   * @return The child.
   */
  def apply(idx: Int) = childrenNodes(idx)


  // Properties

  /** The part of speech tag of the node. Not `null` only if the node is a leave (which represents a word) */
  var posTag: String = null

  /** The index of this node among all siblings. */
  def index: Int = {
    if (this.parentNode == null) return -1
    else return this.parentNode.children.indexOf(this)
  }

  /** Returns the part of speech tag, if the node is a leave (which represents a word).
    * Otherwise, returns the syntactic category defined in ''Penn Treebank'' (e.g., `"NP"`, `"VP"`).
    */
  def syntacticCategory = if (posTag != null) posTag else this.content

  /**
   * Syntactic head of the node. Does not go all the way to word nodes. Only returns the head that
   * is an immediate child of this node.
   */
  def syntacticHead = CollinsHeadFinder(this)

  /**
   * Syntactic head word of the node. Goes all the way to a word node.
   */
  def syntacticHeadWord = {
    if (isWord) this
    else {
      try {
        var reachedTheBottom = false
        var cur = this
        while (!reachedTheBottom) {
          cur = cur.syntacticHead
          if (cur == null || cur.isWord) reachedTheBottom = true
        }
        cur
      }
      catch {
        case e: NullElementHasNoHeadException ⇒ null
      }
    }
  }

  /** The semantic head of the node. Goes all the way to a word node. Reference: (Gerber, 2011) */
  def semanticHead = GerberSemanticHeadFinder(this)

  /** All leave nodes subsumed by this node.
    * Different from the inherited `leaves` method in that this method returns an iterable of
    * [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]]'s, while the `leaves`
    * method returns one with `Node[String]`'s.
    */
  def wordNodes = this.leaves.map(n ⇒ n.asInstanceOf[PennTreebankNode])

  /** All words subsumed by this node. */
  def words = this.leaves.map(n ⇒ n.data)

  /** The node of the first word subsumed by this node. */
  def firstWordNode = traverse(n ⇒ n.children, n ⇒ n.isLeaf, n ⇒ n.isLeaf, n ⇒ Unit).head.asInstanceOf[PennTreebankNode]

  /** The first word (in [[String]]) subsumed by this node. */
  def firstWord = firstWordNode.content

  /** The part of speech tag of the first word subsumed by this node. */
  def firstPos = firstWordNode.posTag

  /** The node of the last word subsumed by this node. */
  def lastWordNode = traverse(n ⇒ n.children.reverse, n ⇒ n.isLeaf, n ⇒ n.isLeaf, n ⇒ Unit).head.asInstanceOf[PennTreebankNode]

  /** The last word (in [[String]]) subsumed by this node. */
  def lastWord = lastWordNode.content

  /** The part of speech tag of the last word subsumed by this node. */
  def lastPos = lastWordNode.posTag

  /** The context-free grammar rule that expanded the node */
  def rule: Rule = {
    if (this.parent == null) return null
    Rule(this.parentNode.syntacticCategory, this.parentNode.childrenNodes.map(_.syntacticCategory))
  }

  /** Whether the node is a word node. A word node is different from an internal node in that, the
    * `content` field has the surface word, and the `posTag` field has the part of speech tag.
    * An internal node's `content` is the syntactic category, and its `posTag` is `null`.
    */
  def isWord = this.isLeaf

  /** Whether the node subsumes or is itself of category `"NONE"` */
  def isNullElement: Boolean = {
    if (isLeaf) syntacticCategory == SpecialCategories.nullElement
    else childrenNodes.forall(n ⇒ n.isNullElement)
  }

  /** All siblings to the left of this node */
  def leftSiblings: ArrayBuffer[PennTreebankNode] = {
    val indexUnderParent = this.index

    // This node might already be the root:
    if (this.parent == null) return ArrayBuffer[PennTreebankNode]()

    // If the node is not the root:
    val allSiblings = this.parentNode.childrenNodes
    allSiblings.slice(0, indexUnderParent)
  }

  /** The sibling node immediately to the left of this node */
  def leftSibling: PennTreebankNode = {
    val indexUnderParent = this.index

    // This node might already be the root:
    if (indexUnderParent == -1) return null

    // If the node is not the root:
    val allSiblings = this.parentNode.childrenNodes
    if (indexUnderParent <= allSiblings.length - 1 && indexUnderParent >= 1) allSiblings(indexUnderParent - 1)
    else null
  }

  /** All siblings to the right of this node */
  def rightSiblings: ArrayBuffer[PennTreebankNode] = {
    val indexUnderParent = this.index

    // This node might already be the root:
    if (this.parent == null) return ArrayBuffer[PennTreebankNode]()

    // If the node is not the root:
    val allSiblings = this.parentNode.childrenNodes
    allSiblings.slice(indexUnderParent + 1, allSiblings.length)
  }

  /** The sibling node immediately to the right of this node */
  def rightSibling: PennTreebankNode = {

    val indexUnderParent = this.index

    // This node might already be the root:
    if (indexUnderParent == -1) return null

    // If the node is not the root:
    val allSiblings = this.parentNode.childrenNodes
    if (indexUnderParent <= allSiblings.length - 2 && indexUnderParent >= 0) allSiblings(indexUnderParent + 1)
    else null
  }

  def pathTo(target: PennTreebankNode): String = {

    def successorFunc(searchNode: SearchNode[PennTreebankNode, String]): Iterable[SearchNode[PennTreebankNode, String]] = {
      val ptbNode = searchNode.state
      val successors = ArrayBuffer[SearchNode[PennTreebankNode, String]]()
      if (ptbNode.parentNode != null) successors += SearchNode(ptbNode.parentNode, searchNode, "↑")
      ptbNode.childrenNodes.foreach(x ⇒ successors += SearchNode(x, searchNode, "↓"))
      successors
    }

    val result = Searcher.depthFirstSearch(this, (x: PennTreebankNode) ⇒ x == target, successorFunc, "0")
    result.map(r ⇒ r.action + r.state.syntacticCategory).mkString("").substring(1)
  }
}

object PennTreebankNode {

  /**
   * Creates a node in PennTreebank parse tree.
   * @param depth Depth of the node in the tree. The root node's depth is 0.
   *
   * @param content This is either
   *                <ol>
   *                  <li> the syntactic category (without functional labels like `"SBJ"`) for
   *                       internal nodes, or </li>
   *                  <li> surface words for leaf nodes. </li>
   *                </ol>
   *
   * @param functionalTags Functional tags. E.g., `["BNF", "SBJ", "LOC"]`, ...
   *
   * @param parentNode The parent PennTreebankNode. This is different from the `parent` property
   *                   inherited from the trait [[foundation.math.graph.Node Node]]. That `parent`
   *                   returns a `Node[String]` object, while `parentNode` is directly a
   *                   [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]] object,
   *                   saving the effort to do downward type casting.
   *
   * @param childrenNodes The children of this node. This is different from the `children` property
   *                   inherited from the trait [[foundation.math.graph.Node Node]]. That `children`
   *                   returns an immutable array of `Node[String]` objects, while this `childrenNodes`
   *                   is directly a mutable array of
   *                   [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]] objects,
   *                   saving the effort to do downward type casting.
   *                   
   * @return
   */
  def apply(depth: Int,
            content: String,
            referenceId: Int,
            mapId: Int,
            functionalTags: Seq[String],
            parentNode: PennTreebankNode,
            childrenNodes: mutable.ArrayBuffer[PennTreebankNode]) = {

    if (childrenNodes == null) {
      new PennTreebankNode(depth, content, referenceId, mapId, functionalTags, parentNode, new ArrayBuffer[PennTreebankNode])
    }
    else {
      new PennTreebankNode(depth, content, referenceId, mapId, functionalTags, parentNode, childrenNodes)
    }

  }
}