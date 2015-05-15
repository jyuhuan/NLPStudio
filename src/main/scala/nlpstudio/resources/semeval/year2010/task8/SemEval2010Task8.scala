package nlpstudio.resources.semeval.year2010.task8

import java.io.File

import nlpstudio.core._
import nlpstudio.io.files.TextFile

import nlpstudio.core.ImplicitCodebooks._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
object SemEval2010Task8 {
  private val regexE1String = """<e1>([^<>]+)</e1>"""
  private val regexE2String = """<e2>([^<>]+)</e2>"""
  private val regexE1 = regexE1String r
  private val regexE2 = regexE2String r
  private val regexRel = """[\(\),]"""

  def load(path: String): Array[SemEval2010Task8Entry] = {
    val trainFilePath = path + File.separator + "SemEval2010_task8_training/TRAIN_FILE.TXT"

    val lines = TextFile.readLines(trainFilePath)
    for (lineGroup ← lines grouped 4) yield {
      val sentenceLine = lineGroup(0)
      val relationLine = lineGroup(1)
      val commentLine = lineGroup(2)

      val sentenceLineParts = sentenceLine.split('\t')
      val id = sentenceLineParts(0).toInt
      val sentenceWithTags = sentenceLineParts(1)

      val e1String = regexE1.findAllMatchIn(sentenceWithTags).next().subgroups.head
      val e1 = Phrase(e1String)

      val e2String = regexE2.findAllMatchIn(sentenceWithTags).next().subgroups.head
      val e2 = Phrase(e2String)

      val e1Replaced = sentenceWithTags.replaceAll(regexE1String, e1String)
      val e1and2Replaced = e1Replaced.replaceAll(regexE2String, e2String)

      val relationParts = relationLine.split(regexRel)
      val relationName = relationParts(0)
      val relation = if (relationParts.length <= 1) OrderedRelation[Phrase](e1, "Other", e2)
      else if (relationParts(1) == "e1") OrderedRelation[Phrase](e1, relationName, e2)
      else OrderedRelation[Phrase](e2, relationName, e1)

      val sentenceString = e1and2Replaced.substring(1, e1and2Replaced.length - 1)
      val sentence = Sentence(sentenceString)

      SemEval2010Task8Entry(id, sentence, relation)
    }
  }.toArray
}
