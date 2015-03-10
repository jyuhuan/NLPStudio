package io.corporafiles

import io.files.TextFile
import nlpstudio.utilities.RegularExpressions
import foundation.math.graph.Node

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/5/15.
 */
object PennTreebankFile {

  def parseSingleTreebankSentence(lines: Seq[String]) = {
    val all = lines mkString ""

    var processedString = RegularExpressions.leftParenthesis.replaceAllIn(all, " ( ")
    processedString = RegularExpressions.rightParenthesis.replaceAllIn(processedString, " ) ")
    processedString = RegularExpressions.whitespaces.replaceAllIn(processedString, " ").trim

    val rawTokens = processedString.split(" ")
    val tokens = rawTokens.slice(2, rawTokens.length - 2)

    var curNode = new Node("S", null, new ArrayBuffer[Node[String]]())

    for (token ← tokens) {
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
    curNode
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

    for (group ← groups) yield parseSingleTreebankSentence(group)
  }
}
