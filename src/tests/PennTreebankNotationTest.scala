package tests

import nlpstudio.io.corpora.penntreebank.PennTreebank
import nlpstudio.io.files.TextFile
import nlpstudio.utilities.RegularExpressions
import nlpstudio.visualization.core.GraphViewer

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/5/15.
 */

object PennTreebankNotationTest {
  def main(args: Array[String]) {

    //val s = PennTreebank.load("/Users/yuhuan/work/data/penn-tree-bank-3/parsed/mrg/wsj")
    val s = PennTreebank.loadFromSingleMrgFile("/Users/yuhuan/work/data/penn-tree-bank-3/parsed/mrg/wsj/05/wsj_0524.mrg")
    //val graph = s(32).asGraph()

    //val viewer = new GraphViewer(graph)
    //viewer.showWindow()

    val parse = s(52)
    val leaves = parse.tree.leaves
    val a = leaves(1)
    val b = leaves(12).parent.parent
    val rel = leaves(4)


    val breakpoint = 0
  }
}
