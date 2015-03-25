package nlpstudio.tools.verbfinders

import nlpstudio.resources.penntreebank.PennTreebankNode

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
object GerberPassiveVerbFinder {
  val beVerbs = Set("am", "are", "is", "was", "were", "been", "being", "become", "became")
  def isPassive(node: PennTreebankNode): Boolean = {
    if (!node.isWord) false
    else if (node.posTag != "VBN") false
    else if (node.wordIndex == 0) false
    else {
      try {
        val prevWord = node.prevWordNode.content.toLowerCase
        if (beVerbs contains prevWord) true
        else false
      }
      catch {
        case e: Exception => false
      }
    }
  }
}
