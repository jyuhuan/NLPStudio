package io.corpora.PennTreebank

import foundation.math.graph.{Node, Tree}
import io.files.TextFile
import nlpstudio.utilities.RegularExpressions

import scala.collection.mutable.ArrayBuffer
import io.corpora.PennTreebank.PennTreebankEntry

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/5/15.
 */
object PennTreebank {

  def parseSingleTreebankSentence(lines: Seq[String]) = {
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
    Tree(curNode)
  }

  def readTrees(path: String) = {
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

    for (group ← groups.filter(g ⇒ g.length > 0)) yield PennTreebankEntry(parseSingleTreebankSentence(group))
  }
}
