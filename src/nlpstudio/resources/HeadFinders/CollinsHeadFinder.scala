package nlpstudio.resources.HeadFinders

import nlpstudio.resources.penntreebank.PennTreebankNode

import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/22/15.
 */
object CollinsHeadFinder extends HeadFinder {

  val generalRules = Map[String, (FindDirection, Array[String])](
    "ADJP" → (FindDirection.Left, Array("NNS", "QP", "NN", "$", "ADVP", "JJ", "VBN", "VBG", "ADJP", "JJR", "NP", "JJS", "DT", "FW", "RBR", "RBS", "SBAR", "RB")),
    "ADVP" → (FindDirection.Right, Array("RB", "RBR", "RBS", "FW", "ADVP", "TO", "CD", "JJR", "JJ", "IN", "NP", "JJS", "NN")),
    "CONJP" → (FindDirection.Right, Array("CC", "RB", "IN")),
    "FRAG" → (FindDirection.Right, Array[String]()),
    "INTJ" → (FindDirection.Left, Array[String]()),
    "LST" → (FindDirection.Right, Array("LS", ":")),
    "NAC" → (FindDirection.Left, Array("NN", "NNS", "NNP", "NNPS", "NP", "NAC", "EX", "$", "CD", "QP", "PRP", "VBG", "JJ", "JJS", "JJR", "ADJP", "FW")),
    "PP" → (FindDirection.Right, Array("IN", "TO", "VBG", "VBN", "RP", "FW")),
    "PRN" → (FindDirection.Left, Array[String]()),
    "PRT" → (FindDirection.Right, Array("RP")),
    "QP" → (FindDirection.Left, Array("$", "IN", "NNS", "NN", "JJ", "RB", "DT", "CD", "NCD", "QP", "JJR", "JJS")),
    "RRC" → (FindDirection.Right, Array("VP", "NP", "ADVP", "ADJP", "PP")),
    "S" → (FindDirection.Left, Array("TO", "IN", "VP", "S", "SBAR", "ADJP", "UCP", "NP")),
    "SBAR" → (FindDirection.Left, Array("WHNP", "WHPP", "WHADVP", "WHADJP", "IN", "DT", "S", "SQ", "SINV", "SBAR", "FRAG")),
    "SBARQ" → (FindDirection.Left, Array("SQ", "S", "SINV", "SBARQ", "FRAG")),
    "SINV" → (FindDirection.Left, Array("VBZ", "VBD", "VBP", "VB", "MD", "VP", "S", "SINV", "ADJP", "NP")),
    "SQ" → (FindDirection.Left, Array("VBZ", "VBD", "VBP", "VB", "MD", "VP", "SQ")),
    "UCP" → (FindDirection.Right, Array[String]()),
    "VP" → (FindDirection.Left, Array("TO", "VBD", "VBN", "MD", "VBZ", "VB", "VBG", "VBP", "AUX", "AUXG", "VP", "ADJP", "NN", "NNS", "NP")),
    "WHADJP" → (FindDirection.Left, Array("CC", "WRB", "JJ", "ADJP")),
    "WHADVP" → (FindDirection.Right, Array("CC", "WRB")),
    "WHNP" → (FindDirection.Left, Array("WDT", "WP", "WP$", "WHADJP", "WHPP", "WHNP")),
    "WHPP" → (FindDirection.Right, Array("IN", "TO", "FW"))
    )


  override def find(node: PennTreebankNode): PennTreebankNode = {
    // The head of a leave node is itself.
    if (node.isLeaf) return node

    // The head of a node of "-NONE-" category has no head.
    if (node.isNullElement) return null

    // Find the default head
    val head = node.childrenNodes.find(n ⇒ !n.isNullElement)

    // If there is no node that is not null, then there is no head.
    if (head == None) return null

    val childrenNodes = node.childrenNodes

    // Deal with NP
    if (node.syntacticCategory == "NP") {
      //if (childrenNodes.last == "")
    }


    return null
  }

}
