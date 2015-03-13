package nlpstudio.io.corpora.penntreebank

import foundation.math.graph.{Node, Tree}
import nlpstudio.io.files.TextFile
import nlpstudio.io.files.directory
import nlpstudio.utilities.RegularExpressions

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/5/15.
 */

/**
 * A reader for Penn Treebank Third Version
 */
object PennTreebank {

  /**
   * Parse a bracket-syntax for one single sentence to a Penn Treebank entry object.
   * @param lines the lines for the bracket-syntax. The syntax should be exactly the same as the mrg file.
   *              More or less brackets on the outside will cause error!
   * @return A Penn Treebank Entry.
   */
  def parseSingleTreebankSentence(sectionId: Int, mrgFileId: Int, sentenceId: Int, lines: Seq[String]): PennTreebankEntry = {
    val all = lines mkString ""

    var processedString = RegularExpressions.leftParenthesis.replaceAllIn(all, " ( ")
    processedString = RegularExpressions.rightParenthesis.replaceAllIn(processedString, " ) ")
    processedString = RegularExpressions.whitespaces.replaceAllIn(processedString, " ").trim

    val rawTokens = processedString.split(" ")
    val tokens = rawTokens.slice(2, rawTokens.length - 2)

    var curNode = new Node(tokens(0), null, new ArrayBuffer[Node[String]]())

    for (token ← tokens.slice(1, tokens.length)) {
      if (token == "(") {
        curNode = curNode --> ""
      }
      else if (token == ")") {
        curNode = curNode.parent
      }
      else {
        if (curNode.data.length > 0) {
          curNode --> token
        }
        else curNode.data = token
      }
    }
    PennTreebankEntry(sectionId, mrgFileId, sentenceId,Tree(curNode))
  }

  /**
   * Reads all sentences from a *.mrg file.
   * @param path A path to a *.mrg file.
   * @return A collection of PennTreebankEntry, each representing a parsed sentence.
   */
  def loadFromSingleMrgFile(path: String): Array[PennTreebankEntry] = {
    // obtain the section, mrg file, and sentence ids.
    val lastPartOfPath = path.split('/').last // wsj_0016.mrg
    val sectionId = lastPartOfPath.substring(4, 6).toInt
    val mrgFileId = lastPartOfPath.substring(6, 8).toInt

    // split tree by tree in this one single mrg file.
    val lines = TextFile.readLines(path)
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

    val parseTrees = ArrayBuffer[PennTreebankEntry]()
    var sentenceIdCounter = 0
    for (group ← groups) {
      if (group.length > 0) {
        parseTrees += parseSingleTreebankSentence(sectionId, mrgFileId, sentenceIdCounter, group)
        sentenceIdCounter += 1
      }
    }
    parseTrees.toArray
    //(for (group ← groups.filter(g ⇒ g.length > 0)) yield parseSingleTreebankSentence(group)).toArray
  }

  /**
   * Reads all *.mrg files from a section (e.g., wsj/01/).
   * @param dir The path to the directory that contains *.mrg files.
   * @return A collection of collection of parsed trees.
   */
  def loadSection(dir: String): Array[Array[PennTreebankEntry]] = {
    val allFiles = directory.allFilesWithExtension(dir, "mrg")
    (for (mrgFile ← allFiles) yield loadFromSingleMrgFile(mrgFile.getAbsolutePath)).toArray
  }


  /**
   * Reads all parse trees of WSJ.
   * @param dirToWsj Path to the directory of WSJ (e.g., penn-tree-bank-3/parsed/wsj/)
   * @return Suppose you store the return value is a variable, wsj,
   *         accessing the second tree located in the mrg file 0103,
   *         at section 01 is done by
   *         wsj[01][03][1]
   */
  def load(dirToWsj: String): Array[Array[Array[PennTreebankEntry]]] = {
    (for (subDir <- directory.allSubdirectories(dirToWsj)) yield loadSection(subDir.getAbsolutePath)).toArray
  }
}
