package nlpstudio.resources.penntreebank


import foundation.exceptions.GoalNotFoundException
import foundation.graph.Node
import foundation.problems.search.{SimpleSearcher, SearchNode, Searcher}
import nlpstudio.exceptions.{NotAWordNodeException, NoMoreWordsException, LowestCommonAncestorNotExistsException}
import nlpstudio.tools.headfinders.{NullElementHasNoHeadException, GerberSemanticHeadFinder, CollinsHeadFinder}
import nlpstudio.resources.core.Rule
import nlpstudio.tools.verbfinders.GerberPassiveVerbFinder


import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


/**
 * Created by yuhuan on 3/17/15.
 */

/**
 *
 * @param content Either surface word or syntactic category.
 *                Set only the syntactic category label. Put the functional labels in
 *                `functionalLabels`, co-indices (either inward or outward) in `coIndex`,
 *                and gap-mapping indices in `mapIndex`.
 * @param depth The depth of the node in the tree.
 *              The root node (usually S) has depth 0.
 *              The depth of a child is always 1 greater than the parent.
 * @param parentNode The parent node.
 *                   If no parent, set to `null`.
 * @param childrenNodes The children nodes.
 *                      If no children, set to an empty ArrayBuffer[PennTreebankNode] rather than
 *                      `null`.
 * @param coIndex The co-index. (See documentation for what it is)
 * @param mapIndex The mapping index for gapping. (See documentation for what it is)
 * @param functionalLabels The functional labels.
 */
class PennTreebankNode private(
                                var content: String,
                                var depth: Int,
                                var parentNode: PennTreebankNode,
                                var childrenNodes: mutable.ArrayBuffer[PennTreebankNode],
                                var coIndex: Int,
                                var mapIndex: Int,
                                var functionalLabels: Seq[String]
                                ) extends Node[String] {

  //region Conforming to the trait

  override def data: String = content
  override def parent: Node[String] = parentNode
  override def children: Seq[Node[String]] = childrenNodes
  override def isLeaf: Boolean = childrenNodes.isEmpty
  override def toString = data.toString

  //endregion

  //region Detectors
  /**
   * Whether the node is a word node.
   * If true, only the general properties and special properties for words are meaningful.
   * If false, only the general properties and special properties for constituents are meaningful.
   * This library does not perform this check when properties are accessed, because these properties
   * are often frequently accessed in a loop, and the user often knows what type of node he/she is
   * working with.
   */
  def isWord = this.isLeaf

  /** Whether the node subsumes or is itself of category `-NONE-` */
  def isNullElement: Boolean = {
    if (isLeaf) syntacticCategoryOrPosTag == "-NONE-"
    else childrenNodes.forall(n ⇒ n.isNullElement)
  }



  //endregion

  //region Special properties for a word node
  /**
   * The surface form of this word.
   * If this node is a constituent, then the syntactic category will be returned.
   */
  def surface = content

  /**
   * The part of speech tag of the node if this node is a word node.
   * If this node is a constituent, then `null`
   */
  var posTag: String = null

  /**
   * The index of the word in the sentence, starting form 0.
   * If this node is a constituent, then -1.
   */
  var wordIndex: Int = -1

  /**
   * Whether this word node represents a passive verb.
   * If this node is a constituent, `false` will be returned directly.
   */
  def isPassive: Boolean = GerberPassiveVerbFinder.isPassive(this)

  //endregion

  //region Special properties for an internal node

  /**
   * The syntactic category of this constituent.
   * If this node is a word, then the surface form of this word is shown.
   * @return
   */
  def syntacticCategory = this.content

  /**
   * All nodes subsumed under this node, including both constituents and words.
   * @return A collection of all nodes.
   */
  def allNodes = {
    this.traverse().map(x ⇒ x.asInstanceOf[PennTreebankNode])
  }

  /**
   * All constituent nodes (i.e., internal nodes in the tree).
   * @return
   */
  def constituentNodes = this.internalNodes.map(n ⇒ n.asInstanceOf[PennTreebankNode])

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

  def firstWordIndex = firstWordNode.wordIndex
  def lastWordIndex = lastWordNode.wordIndex

  //endregion

  //region General Properties (meaningful to both words and constituents)

  /** Returns the part of speech tag, if the node is a leave (which represents a word).
    * Otherwise, returns the syntactic category defined in ''Penn Treebank'' (e.g., `"NP"`, `"VP"`).
    */
  def syntacticCategoryOrPosTag = if (posTag != null) posTag else this.content

  /** The index of this node among all siblings. */
  def siblingIndex: Int = {
    if (this.parentNode == null) return -1
    else return this.parentNode.children.indexOf(this)
  }

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
  def semanticHeadWord = GerberSemanticHeadFinder(this)

  /** The context-free grammar rule that expanded the node */
  def rule: Rule = {
    if (this.parent == null) return null
    Rule(this.parentNode.syntacticCategoryOrPosTag, this.parentNode.childrenNodes.map(_.syntacticCategoryOrPosTag))
  }

  /** All siblings to the left of this node */
  def leftSiblings: ArrayBuffer[PennTreebankNode] = {
    val indexUnderParent = this.siblingIndex

    // This node might already be the root:
    if (this.parent == null) return ArrayBuffer[PennTreebankNode]()

    // If the node is not the root:
    val allSiblings = this.parentNode.childrenNodes
    allSiblings.slice(0, indexUnderParent)
  }

  /** The sibling node immediately to the left of this node */
  def leftSibling: PennTreebankNode = {
    val indexUnderParent = this.siblingIndex

    // This node might already be the root:
    if (indexUnderParent == -1) return null

    // If the node is not the root:
    val allSiblings = this.parentNode.childrenNodes
    if (indexUnderParent <= allSiblings.length - 1 && indexUnderParent >= 1) allSiblings(indexUnderParent - 1)
    else null
  }

  /** All siblings to the right of this node */
  def rightSiblings: ArrayBuffer[PennTreebankNode] = {
    val indexUnderParent = this.siblingIndex

    // This node might already be the root:
    if (this.parent == null) return ArrayBuffer[PennTreebankNode]()

    // If the node is not the root:
    val allSiblings = this.parentNode.childrenNodes
    allSiblings.slice(indexUnderParent + 1, allSiblings.length)
  }

  /** The sibling node immediately to the right of this node */
  def rightSibling: PennTreebankNode = {

    val indexUnderParent = this.siblingIndex

    // This node might already be the root:
    if (indexUnderParent == -1) return null

    // If the node is not the root:
    val allSiblings = this.parentNode.childrenNodes
    if (indexUnderParent <= allSiblings.length - 2 && indexUnderParent >= 0) allSiblings(indexUnderParent + 1)
    else null
  }

  def root: PennTreebankNode = {
    var cur = this
    var done = false
    while (!done) {
      if (cur.parentNode == null) done = true
      else cur = cur.parentNode
    }
    cur
  }

  def prevWordNode: PennTreebankNode = {
    val firstIdx = this.firstWordNode.wordIndex
    if (firstIdx == 0) return null //throw new NoMoreWordsException("This word is already at the left end. No more previous word. ")
    //if (!this.isWord) throw new NotAWordNodeException("prevWord is only defined on word nodes. ")
    root.wordNodes(firstIdx - 1)
  }

  def nextWordNode: PennTreebankNode = {
    val lastIdx = this.lastWordNode.wordIndex
    val all = root.wordNodes
    if (lastIdx == all.length - 1) return null //throw new NoMoreWordsException("This word is already at the right end. No more next word. ")
    //if (!this.isWord) throw new NotAWordNodeException("nextWord is only defined on word nodes. ")
    all(lastIdx + 1)
  }

  def ancestors: ArrayBuffer[PennTreebankNode] = {
    val result = ArrayBuffer[PennTreebankNode]()
    var cur = this
    var done = false
    while (!done) {
      cur = cur.parentNode
      if (cur == null) done = true
      else result += cur
    }
    result
  }

  //endregion

  //region Methods

  /**
   * Gets the child [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]] at a given index.
   * @param idx The index of the child.
   * @return The child.
   */
  def apply(idx: Int) = childrenNodes(idx)

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
   * Finds the right most node that satisfies the given goal test.
   * @param isGoal The goal test. The algorithm thinks it has found the goal if this returns true.
   * @return The right most node that satisfies the given goal test.
   */
  def rightMost(isGoal: PennTreebankNode ⇒ Boolean): PennTreebankNode = {
    def succ(n: PennTreebankNode): Iterable[PennTreebankNode] = {
      val successors = ArrayBuffer[PennTreebankNode]()
      successors ++= n.childrenNodes
      successors
    }
    try {
      SimpleSearcher.depthFirstSearch(this, isGoal, succ)
    }
    catch {
      case e: GoalNotFoundException ⇒ null
    }
  }

  /**
   * Finds the lowest common ancestor (LCA) of this node and another node.
   * @param that The node to be found LCA together with.
   * @return The lowest common acnestor of this node and `that` node in the tree.
   */
  def lowestCommonAncestor(that: PennTreebankNode): PennTreebankNode = {
    val myAncestors = this.ancestors.toSet
    val otherAncestors = that.ancestors.toSet
    if (myAncestors.isEmpty) return this
    else if (myAncestors.isEmpty) return that
    else {
      val intersect = myAncestors intersect otherAncestors
      intersect.maxBy(_.depth)
    }
  }


  /**
   * Finds a node that both
   *   (1) is right adjacent to this node, and
   *   (2) satisfies the condition.
   * E.g., an NP that is right adjacent to this node.
   * @param condition
   * @return
   */
  def rightAdjacentNode(condition: PennTreebankNode ⇒ Boolean): PennTreebankNode = {
    val nextWordNodeOfThisNode = this.nextWordNode
    if (nextWordNodeOfThisNode == null) return null
    // go up
    var cur = nextWordNodeOfThisNode
    var done = false
    while (!done) {
      if (condition(cur) && cur.firstWordNode == nextWordNodeOfThisNode) {
        done = true
        return cur
      }
      if (cur.parentNode == null) {
        done = true
      }
      cur = cur.parentNode
    }
    null // no adjacent node that meets the condition is found
  }

  /**
   * Finds a node that both
   *   (1) is left adjacent to this node, and
   *   (2) satisfies the condition.
   * E.g., an NP that is left adjacent to this node.
   * @param condition
   * @return
   */
  def leftAdjacentNode(condition: PennTreebankNode ⇒ Boolean): PennTreebankNode = {
    val prevWordNodeOfThisNode = this.prevWordNode
    if (prevWordNodeOfThisNode == null) return null
    // go up
    var cur = prevWordNodeOfThisNode
    var done = false
    while (!done) {
      if (condition(cur) && cur.lastWordNode == prevWordNodeOfThisNode) {
        done = true
        return cur
      }
      if (cur.parentNode == null) {
        done = true
      }
      cur = cur.parentNode
    }
    null // no adjacent node that meets the condition is found
  }

  /**
   * Test if the node lies before (not necessarily immediately before) that node.
   * @param that
   * @return
   */
  def isBefore(that: PennTreebankNode): Boolean = {
    val thisFirstWordNodeIdx = this.firstWordNode.wordIndex
    val thatFirstWordNodeIdx = that.firstWordNode.wordIndex
    thisFirstWordNodeIdx < thatFirstWordNodeIdx
  }

  /**
   * Finds the path to the node that makes the given predicate true.
   * The path is a string of the form: `"NNP↑NP↑S↓VP↓VP↓VB"`.
   * @param isGoal
   * @return
   */
  def pathTo(isGoal: PennTreebankNode => Boolean): String = {

    def successorFunc(searchNode: SearchNode[PennTreebankNode, String]): Iterable[SearchNode[PennTreebankNode, String]] = {
      val ptbNode = searchNode.state
      val successors = ArrayBuffer[SearchNode[PennTreebankNode, String]]()
      if (ptbNode.parentNode != null) successors += SearchNode(ptbNode.parentNode, searchNode, "↑")
      ptbNode.childrenNodes.foreach(x ⇒ successors += SearchNode(x, searchNode, "↓"))
      successors
    }

    val result = Searcher.breadthFirstSearch(this, isGoal, successorFunc, "0")
    if (result.size <= 1) return null
    result.map(r ⇒ r.action + r.state.syntacticCategoryOrPosTag).mkString("").substring(1)
  }

  //endregion
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
   *                   inherited from the trait [[foundation.graph.Node Node]]. That `parent`
   *                   returns a `Node[String]` object, while `parentNode` is directly a
   *                   [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]] object,
   *                   saving the effort to do downward type casting.
   *
   * @param childrenNodes The children of this node. This is different from the `children` property
   *                   inherited from the trait [[foundation.graph.Node Node]]. That `children`
   *                   returns an immutable array of `Node[String]` objects, while this `childrenNodes`
   *                   is directly a mutable array of
   *                   [[nlpstudio.resources.penntreebank.PennTreebankNode PennTreebankNode]] objects,
   *                   saving the effort to do downward type casting.
   *                   
   * @return
   */
  def apply(depth: Int,
            content: String,
            coindex: Int,
            mapId: Int,
            functionalTags: Seq[String],
            parentNode: PennTreebankNode,
            childrenNodes: mutable.ArrayBuffer[PennTreebankNode]) = {

    if (childrenNodes == null) {
      new PennTreebankNode(content, depth, parentNode, new ArrayBuffer[PennTreebankNode], coindex, mapId, functionalTags)
    }
    else {
      new PennTreebankNode(content, depth, parentNode, childrenNodes, coindex, mapId, functionalTags)
    }

  }
}