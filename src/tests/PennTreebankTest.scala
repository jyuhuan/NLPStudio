package tests

import nlpstudio.io.files.TextFile
import nlpstudio.resources.penntreebank.{PennTreebankNode, PennTreebankEntry, PennTreebank}

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/5/15.
 */

object PennTreebankTest extends App {

  val testMrgFile = "/Users/yuhuan/work/data/penn-tree-bank-3/parsed/mrg/wsj/00/wsj_0006.mrg"

  val lines = TextFile.readLines(testMrgFile)

  val ptb = PennTreebank.parseMrgFile("wsj", 0, 6, lines)

  val parseTree = ptb(0).tree

  val node1 = parseTree(1)(1)(1)
  val node2 = parseTree(0)(1)

  val path = node1.pathTo(node2)

  val breakpoint = 0

}
