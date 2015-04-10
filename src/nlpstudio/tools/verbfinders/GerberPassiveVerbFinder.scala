package nlpstudio.tools.verbfinders

import nlpstudio.resources.penntreebank.PennTreebankNode

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */

/**
 * Determines if a Penn Treebank leave node (word node) is a passive verb.
 * From Matthew Gerber's C# project on http://ptl.sys.virginia.edu/ptl/members/matthew-gerber
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
