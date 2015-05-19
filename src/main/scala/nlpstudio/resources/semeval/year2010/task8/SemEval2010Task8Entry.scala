package nlpstudio.resources.semeval.year2010.task8

import nlpstudio.core._

import nlpstudio.core.GlobalCodebooks.cb

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
case class SemEval2010Task8Entry(id: Int,
                                 sentence: Sentence,
                                 e1StartPos: Int,
                                 e1EndPos: Int,
                                 e2StartPos: Int,
                                 e2EndPos: Int,
                                 isReversed: Boolean,
                                 annotation: OrderedRelation[Phrase]) {
  def toText = {

    val sb = new StringBuilder()

    for (i ← 0 until sentence.size) {
      if (i == e1StartPos) {
        sb.append("<e1>")
        sb.append(cb(sentence(i)))
      }
      else if (i == e1EndPos) {
        sb.append("</e1> ")
      }
      else if (i == e2StartPos) {
        sb.append("<e2>")
        sb.append(cb(sentence(i)))
      }
      else if (i == e2EndPos) {
        sb.append("</e2> ")
      }
      else {
        sb.append(cb(sentence(i)))
        sb.append(" ")
      }
    }
    sb.append("\"\n")

    val taggedSentence = sb.toString()
    val splits = taggedSentence.split(' ')
    val noStars = splits.filterNot(w ⇒ w == "0" || w.contains("*")).mkString(" ")
    val withProperFrontQuote = noStars.replace("``", "“")
    val withProperBackQuote = withProperFrontQuote.replace("''", "”")
    val cleanSentence = withProperBackQuote

    val sb2 = new StringBuilder()

    if (annotation.relationName == "Other") {
      sb2.append("Other\n")
    }
    else if (annotation.relationName == "Cause-Effect") {
      if (isReversed) sb2.append("Cause-Effect(e2,e1)\n")
      else sb2.append("Cause-Effect(e1,e2)\n")
    }

    sb2.append("Comment:\n")
    val s = sb2.toString()
    id + "\t\"" + cleanSentence + sb2.toString()
  }
}
