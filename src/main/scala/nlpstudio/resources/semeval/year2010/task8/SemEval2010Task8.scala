package nlpstudio.resources.semeval.year2010.task8

import java.io.File

import nlpstudio.core._
import nlpstudio.io.files.TextFile

import nlpstudio.tools.tokenizers.StanfordTokenizer

import scala.collection.mutable.ArrayBuffer
import nlpstudio.core.GlobalCodebooks._

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
      //val commentLine = lineGroup(2) TODO: the comments are basically useless for my current applications

      // Split the sentence id and sentence content
      val sentenceLineParts = sentenceLine.split('\t')
      // Get the id of the sentence
      val id = sentenceLineParts(0).toInt
      // Get the sentence content
      val sentenceWithTags = sentenceLineParts(1)
      // Remove the quotes at the beginning and ending
      val sentenceWithTagsNoQuote = sentenceWithTags.substring(1, sentenceWithTags.length - 1)

      // Tokenize the sentence
      val tokensWithTags = StanfordTokenizer.tokenize(sentenceWithTagsNoQuote)

      val tokens = ArrayBuffer[Int]()
      var e1BeginPos = -1
      var e1EndPos = -1
      var e2BeginPos = -1
      var e2EndPos = -1
      for ((word, idx) ← tokensWithTags.zipWithIndex) {
        if (word == cb("<e1>")) {
          e1BeginPos = idx
        }
        else if (word == cb("</e1>")) {
          e1EndPos = idx - 1
        }
        else if (word == cb("<e2>")) {
          e2BeginPos = idx - 2
        }
        else if (word == cb("</e2>")) {
          e2EndPos = idx - 3
        }
        else {
          tokens += word
        }
      }


      val e1 = Phrase(tokens.slice(e1BeginPos, e1EndPos))
      val e2 = Phrase(tokens.slice(e2BeginPos, e2EndPos))

      val relationParts = relationLine.split(regexRel)
      val relationName = relationParts(0)
      val relation = if (relationParts.length <= 1) OrderedRelation[Phrase](e1, "Other", e2)
      else if (relationParts(1) == "e1") OrderedRelation[Phrase](e1, relationName, e2)
      else OrderedRelation[Phrase](e2, relationName, e1)

      val sentence = Sentence(tokens)
      SemEval2010Task8Entry(
        id,
        sentence,
        e1BeginPos,
        e1EndPos,
        e2BeginPos,
        e2EndPos,
        relation
      )
    }
  }.toArray
}
