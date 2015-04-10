package nlpstudio.tools.headfinders

import nlpstudio.resources.penntreebank.PennTreebankNode

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/22/15.
 */
object CollinsHeadFinder extends HeadFinder {

  val generalRules = Map[String, (FindDirection, Array[String])](
    "ADJP" → (FindDirection.LeftToRight, Array("NNS", "QP", "NN", "$", "ADVP", "JJ", "VBN", "VBG", "ADJP", "JJR", "NP", "JJS", "DT", "FW", "RBR", "RBS", "SBAR", "RB")),
    "ADVP" → (FindDirection.RightToLeft, Array("RB", "RBR", "RBS", "FW", "ADVP", "TO", "CD", "JJR", "JJ", "IN", "NP", "JJS", "NN")),
    "CONJP" → (FindDirection.RightToLeft, Array("CC", "RB", "IN")),
    "FRAG" → (FindDirection.RightToLeft, Array[String]()),
    "INTJ" → (FindDirection.LeftToRight, Array[String]()),
    "LST" → (FindDirection.RightToLeft, Array("LS", ":")),
    "NAC" → (FindDirection.LeftToRight, Array("NN", "NNS", "NNP", "NNPS", "NP", "NAC", "EX", "$", "CD", "QP", "PRP", "VBG", "JJ", "JJS", "JJR", "ADJP", "FW")),
    "PP" → (FindDirection.RightToLeft, Array("IN", "TO", "VBG", "VBN", "RP", "FW")),
    "PRN" → (FindDirection.LeftToRight, Array[String]()),
    "PRT" → (FindDirection.RightToLeft, Array("RP")),
    "QP" → (FindDirection.LeftToRight, Array("$", "IN", "NNS", "NN", "JJ", "RB", "DT", "CD", "NCD", "QP", "JJR", "JJS")),
    "RRC" → (FindDirection.RightToLeft, Array("VP", "NP", "ADVP", "ADJP", "PP")),
    "S" → (FindDirection.LeftToRight, Array("TO", "IN", "VP", "S", "SBAR", "ADJP", "UCP", "NP")),
    "SBAR" → (FindDirection.LeftToRight, Array("WHNP", "WHPP", "WHADVP", "WHADJP", "IN", "DT", "S", "SQ", "SINV", "SBAR", "FRAG")),
    "SBARQ" → (FindDirection.LeftToRight, Array("SQ", "S", "SINV", "SBARQ", "FRAG")),
    "SINV" → (FindDirection.LeftToRight, Array("VBZ", "VBD", "VBP", "VB", "MD", "VP", "S", "SINV", "ADJP", "NP")),
    "SQ" → (FindDirection.LeftToRight, Array("VBZ", "VBD", "VBP", "VB", "MD", "VP", "SQ")),
    "UCP" → (FindDirection.RightToLeft, Array[String]()),
    "VP" → (FindDirection.LeftToRight, Array("TO", "VBD", "VBN", "MD", "VBZ", "VB", "VBG", "VBP", "AUX", "AUXG", "VP", "ADJP", "NN", "NNS", "NP")),
    "WHADJP" → (FindDirection.LeftToRight, Array("CC", "WRB", "JJ", "ADJP")),
    "WHADVP" → (FindDirection.RightToLeft, Array("CC", "WRB")),
    "WHNP" → (FindDirection.LeftToRight, Array("WDT", "WP", "WP$", "WHADJP", "WHPP", "WHNP")),
    "WHPP" → (FindDirection.RightToLeft, Array("IN", "TO", "FW")),
    "NX" → (FindDirection.LeftToRight, Array[String]()),
    "X" → (FindDirection.LeftToRight, Array[String]())
    )


  private def findFirst(scope: IndexedSeq[PennTreebankNode], keys: IndexedSeq[String], direction: FindDirection): PennTreebankNode = {
    val toBeSearched = if (direction == FindDirection.LeftToRight) scope else scope.reverse
    for (key ← keys) {
      val finding = toBeSearched.find(_.syntacticCategoryOrPosTag == key)
      if (finding != None) return finding.get
    }
    null
  }

  override def find(node: PennTreebankNode): PennTreebankNode = {
    // The head of a leave node is itself.
    if (node.isLeaf) return node

    // The head of a node of "-NONE-" category has no head.
    //else if (node.isNullElement) return null

    var head: PennTreebankNode = null

    // Find the default head
    val nonNullElement = node.childrenNodes.find(n ⇒ !n.isNullElement)

    // If there is no node that is not null, then there is no head.
    if (nonNullElement == None) throw new NullElementHasNoHeadException("The children nodes are all null elements.")
    //else head = nonNullElement.get

    val childrenNodes = node.childrenNodes


    val category = node.syntacticCategoryOrPosTag
    // Deal with NP
    if (category == "NP") {

      // First rule: If the last child is a word, and this word's POS tag is "POS",
      // (so annoying, you annotators!!!! How could you abbreviate "possessive ending" as "POS"?!
      //  Aren't there enough ambiguities already?!!)
      // the last child is the head.
      val lastChild = childrenNodes.last
      if (lastChild.isWord && lastChild.posTag == "POS") head = lastChild

      // Second rule
      if (head == null) head = findFirst(childrenNodes, Array("NN", "NNP", "NNPS", "NNS", "NX", "POS", "JJR"), FindDirection.RightToLeft)
      if (head == null) head = findFirst(childrenNodes, Array("NP"), FindDirection.LeftToRight)
      if (head == null) head = findFirst(childrenNodes, Array("$", "ADJP", "PRN"), FindDirection.RightToLeft)
      if (head == null) head = findFirst(childrenNodes, Array("CD"), FindDirection.RightToLeft)
      if (head == null) head = findFirst(childrenNodes, Array("JJ", "JJS", "RB", "QP"), FindDirection.RightToLeft)
      if (head == null) return node.wordNodes.last
    }

    // Not an NP. Try general rules
    else if (generalRules contains category) {
      val directionAndListing = generalRules(category)
      val direction = directionAndListing._1
      val listing = directionAndListing._2
      head = findFirst(childrenNodes, listing, direction)
      if (head == null) {
        head = childrenNodes.head
      }
    }

    if (head == null) {
      val a = 0
    }


    // If <constituent, CC, head>, then constituent should be the head.
    val nodeIndex = head.siblingIndex

    try {
      if (nodeIndex >= 2 && node.childrenNodes(nodeIndex - 1).syntacticCategoryOrPosTag == "CC")
        head = node.childrenNodes(nodeIndex - 2)
    }
    catch {
      case e: IndexOutOfBoundsException ⇒ {
        val a = 0
      }
    }

    if (nodeIndex >= 2 && node.childrenNodes(nodeIndex - 1).syntacticCategoryOrPosTag == "CC")
      head = node.childrenNodes(nodeIndex - 2)
    return head
  }

}
