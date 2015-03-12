package tests

import nlpstudio.io.corpora.nombank._
import nlpstudio.io.corpora.penntreebank.PennTreebank
import nlpstudio.utilities.PathHandler
import nlpstudio.r

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/11/15.
 */
object NomBankTest {
  def main(args: Array[String]) {
    val item = NomBank.load("/Users/yuhuan/work/data/nombank/nombank.1.0")
    val breakpoint = 0
  }
}
