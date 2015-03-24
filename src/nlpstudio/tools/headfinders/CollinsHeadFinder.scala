package nlpstudio.tools.headfinders

import nlpstudio.resources.penntreebank.PennTreebankNode

import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/22/15.
 */
object CollinsHeadFinder extends HeadFinder {

  val generalRules = Map[String, (FindDirection, Array[String])](
    "ADJP" → (FindDirection.RightToLeft, Array("NNS", "QP", "NN", "$", "ADVP", "JJ", "VBN", "VBG", "ADJP", "JJR", "NP", "JJS", "DT", "FW", "RBR", "RBS", "SBAR", "RB")),
    "ADVP" → (FindDirection.LeftToRight, Array("RB", "RBR", "RBS", "FW", "ADVP", "TO", "CD", "JJR", "JJ", "IN", "NP", "JJS", "NN")),
    "CONJP" → (FindDirection.LeftToRight, Array("CC", "RB", "IN")),
    "FRAG" → (FindDirection.LeftToRight, Array[String]()),
    "INTJ" → (FindDirection.RightToLeft, Array[String]()),
    "LST" → (FindDirection.LeftToRight, Array("LS", ":")),
    "NAC" → (FindDirection.RightToLeft, Array("NN", "NNS", "NNP", "NNPS", "NP", "NAC", "EX", "$", "CD", "QP", "PRP", "VBG", "JJ", "JJS", "JJR", "ADJP", "FW")),
    "PP" → (FindDirection.LeftToRight, Array("IN", "TO", "VBG", "VBN", "RP", "FW")),
    "PRN" → (FindDirection.RightToLeft, Array[String]()),
    "PRT" → (FindDirection.LeftToRight, Array("RP")),
    "QP" → (FindDirection.RightToLeft, Array("$", "IN", "NNS", "NN", "JJ", "RB", "DT", "CD", "NCD", "QP", "JJR", "JJS")),
    "RRC" → (FindDirection.LeftToRight, Array("VP", "NP", "ADVP", "ADJP", "PP")),
    "S" → (FindDirection.RightToLeft, Array("TO", "IN", "VP", "S", "SBAR", "ADJP", "UCP", "NP")),
    "SBAR" → (FindDirection.RightToLeft, Array("WHNP", "WHPP", "WHADVP", "WHADJP", "IN", "DT", "S", "SQ", "SINV", "SBAR", "FRAG")),
    "SBARQ" → (FindDirection.RightToLeft, Array("SQ", "S", "SINV", "SBARQ", "FRAG")),
    "SINV" → (FindDirection.RightToLeft, Array("VBZ", "VBD", "VBP", "VB", "MD", "VP", "S", "SINV", "ADJP", "NP")),
    "SQ" → (FindDirection.RightToLeft, Array("VBZ", "VBD", "VBP", "VB", "MD", "VP", "SQ")),
    "UCP" → (FindDirection.LeftToRight, Array[String]()),
    "VP" → (FindDirection.RightToLeft, Array("TO", "VBD", "VBN", "MD", "VBZ", "VB", "VBG", "VBP", "AUX", "AUXG", "VP", "ADJP", "NN", "NNS", "NP")),
    "WHADJP" → (FindDirection.RightToLeft, Array("CC", "WRB", "JJ", "ADJP")),
    "WHADVP" → (FindDirection.LeftToRight, Array("CC", "WRB")),
    "WHNP" → (FindDirection.RightToLeft, Array("WDT", "WP", "WP$", "WHADJP", "WHPP", "WHNP")),
    "WHPP" → (FindDirection.LeftToRight, Array("IN", "TO", "FW"))
    )


  private def findFirst(scope: IndexedSeq[PennTreebankNode], keys: IndexedSeq[String], direction: FindDirection): PennTreebankNode = {
    val toBeSearched = if (direction == FindDirection.LeftToRight) scope else scope.reverse
    for (key ← keys) {
      val finding = toBeSearched.find(_.syntacticCategory == key)
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
    if (nonNullElement == None) return null
    //else head = nonNullElement.get

    val childrenNodes = node.childrenNodes


    val category = node.syntacticCategory
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
      if (head == null) head = node.wordNodes.last
    }

    // Not an NP. Try general rules
    else if (generalRules contains category) {
      val directionAndListing = generalRules(category)
      val direction = directionAndListing._1
      val listing = directionAndListing._2
      head = findFirst(childrenNodes, listing, direction)
    }

    // If <constituent, CC, head>, then constituent should be the head.
    val nodeIndex = head.index
    if (nodeIndex >= 2 && node.childrenNodes(nodeIndex - 1).syntacticCategory == "CC")
      head = node.childrenNodes(nodeIndex - 2)
    return head
  }

}
