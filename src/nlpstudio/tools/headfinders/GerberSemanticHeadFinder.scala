package nlpstudio.tools.headfinders

import nlpstudio.resources.penntreebank.PennTreebankNode

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/22/15.
 */
object GerberSemanticHeadFinder extends HeadFinder {
  val shiftRules = Map[String, FindDirection](
    ("IN", FindDirection.LeftToRight),
    ("TO", FindDirection.LeftToRight),
    ("POS", FindDirection.RightToLeft),
    ("DT", FindDirection.LeftToRight),
    ("PDT", FindDirection.LeftToRight)
  )

  override def find(ptbNode: PennTreebankNode): PennTreebankNode = {
    try {
      var semanticHead = ptbNode.syntacticHeadWord
      if (semanticHead != null && semanticHead.parent != null) {
        if (shiftRules contains semanticHead.syntacticCategoryOrPosTag) {
          val dir = shiftRules(semanticHead.syntacticCategoryOrPosTag)
          if ((dir == FindDirection.LeftToRight && semanticHead.rightSiblings.length > 0) ||
            (dir == FindDirection.RightToLeft && semanticHead.leftSiblings.length > 0)) {
            val siblings = if (dir == FindDirection.LeftToRight) semanticHead.rightSiblings else semanticHead.leftSiblings.reverse

            try {
              semanticHead = siblings.find(n ⇒ !n.isNullElement && n.semanticHeadWord != null).get.semanticHeadWord
            }
            catch {
              case e: NoSuchElementException ⇒ {

              }
            }
          }
        }
      }
      semanticHead
    }
    catch {
      case e: NullElementHasNoHeadException ⇒ { null }
    }
  }
}
