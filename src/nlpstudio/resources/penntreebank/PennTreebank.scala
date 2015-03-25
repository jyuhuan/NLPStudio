package nlpstudio.resources.penntreebank

import nlpstudio.io.files.{directory, TextFile}
import nlpstudio.utilities.RegularExpressions

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by yuhuan on 3/17/15.
 */

object PennTreebank {

  //def apply(pathToPennTreebankWsjDir: String) = new PennTreebank(pathToPennTreebankWsjDir)

  /**
   * Breaks "NP-SBJ-BNF" to ("NP", ["SBJ", "BNF"]).
   * @param s A string in the form of "NP-SBJ-BNF"
   * @return A tuple. _1 is "NP". _2 is ["SBJ", "BNF"].
   */
  def splitCompoundSyntacticCategory(s: String): (String, Int, Int, Array[String]) = {
    if (s(0) == '-') return (s, -1, -1, Array[String]())
    else {
      var mapId: Int = -1
      var withoutMapId: String = s
      val equalSignIdx = s.indexOf('=')
      if (equalSignIdx != -1) {
        mapId = s(equalSignIdx + 1).toString.toInt
        withoutMapId = s.substring(0, s.indexOf('=')) + s.substring(s.indexOf('=') + 2)
      }
      val parts = withoutMapId.split('-')
      if ("0123456789" contains parts.last(0))
        return (parts(0), parts.last.toInt, mapId, parts.slice(1, parts.length - 1))
      else return (parts(0), -1, mapId, parts.slice(1, parts.length))
    }
  }

  def parseEntry(corpusName: String,
                 sectionId: Int,
                 mrgFileId: Int,
                 sentenceId: Int,
                 lines: Seq[String]): PennTreebankEntry = {
    //new PennTreebankEntry(corpusName, sectionId, mrgFileId, sentenceId, new PennTreebankNode(0, "S", Seq("BNF")))

    val all = lines mkString ""

    var processedString = RegularExpressions.leftParenthesis.replaceAllIn(all, " ( ")
    processedString = RegularExpressions.rightParenthesis.replaceAllIn(processedString, " ) ")
    processedString = RegularExpressions.whitespaces.replaceAllIn(processedString, " ").trim

    val rawTokens = processedString.split(" ")
    val tokens = rawTokens.slice(2, rawTokens.length - 2)

    val firstToken = tokens(0)
    val catAndLabel = splitCompoundSyntacticCategory(firstToken)
    var curNode = PennTreebankNode(0, catAndLabel._1, catAndLabel._2, catAndLabel._3, catAndLabel._4, null, null)

    var tokenCounter = 0

    for (token ← tokens.slice(1, tokens.length)) {
      if (token == "(") {
        val newChild = PennTreebankNode(curNode.depth + 1, "", -1, -1, Seq[String](), curNode, null)
        curNode.childrenNodes += newChild
        curNode = newChild
      }
      else if (token == ")") {
        curNode = curNode.parentNode
      }
      else {
        if (curNode.data.length > 0) {
          // a word node
          curNode.posTag = curNode.data
          curNode.content = token
          curNode.wordIndex = tokenCounter
          tokenCounter += 1
        }
        else {
          val newCatAndLabel = splitCompoundSyntacticCategory(token)
          curNode.content = newCatAndLabel._1
          curNode.referenceId = newCatAndLabel._2
          curNode.mapId = newCatAndLabel._3
          curNode.functionalTags = newCatAndLabel._4
        }
      }
    }



    PennTreebankEntry(corpusName, sectionId, mrgFileId, sentenceId, curNode)
  }

  /**
   * Reads all sentences from a *.mrg file.
   * @param path A path to a *.mrg file.
   * @return A collection of PennTreebankEntry, each representing a parsed sentence.
   */
  def parseMrgFile(corpusName: String, sectionId: Int, mrgFileId: Int, lines: TraversableOnce[String]): Array[PennTreebankEntry] = {
    val groups = new ArrayBuffer[ArrayBuffer[String]]
    groups += new ArrayBuffer[String]()
    var cur = 0

    for (line ← lines) {
      if (line.length > 0) {
        if (line(0) == '(') {
          groups += new ArrayBuffer[String]()
          cur = cur + 1
        }
        groups(cur) += line
      }
    }

    var sentenceIdCounter = -1
    (for (group ← groups if group.length > 0) yield {
      sentenceIdCounter += 1
      parseEntry(corpusName, sectionId, mrgFileId, sentenceIdCounter, group)
    }).toArray
  }


  def parseSection(dir: String): Array[Array[PennTreebankEntry]] = {
    val allFiles = directory.allFilesWithExtension(dir, "mrg")
    (for (mrgFile ← allFiles) yield {
      val path = mrgFile.getAbsolutePath
      val pathSplits = path.split('/')
      val lastPartOfPath = pathSplits.last // wsj_0016.mrg
      val sectionId = lastPartOfPath.substring(4, 6).toInt
      val mrgFileId = lastPartOfPath.substring(6, 8).toInt
      val corpusName = pathSplits(pathSplits.length - 3) // wsj
      val lines = TextFile.readLines(path)
      parseMrgFile(corpusName, sectionId, mrgFileId, lines)
    }).toArray
  }


  def load(pathToCorpusDir: String): Array[Array[Array[PennTreebankEntry]]] = {
    (for (subDir <- directory.allSubdirectories(pathToCorpusDir)) yield parseSection(subDir.getAbsolutePath)).toArray
  }

}