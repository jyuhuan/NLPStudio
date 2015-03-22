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

  val parseTree = ptb(0).root



  val breakpoint = 0

}
