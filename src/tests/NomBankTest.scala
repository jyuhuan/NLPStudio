package tests

import nlpstudio.io.corpora.nombank._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/11/15.
 */
object NomBankTest {
  def main(args: Array[String]) {
    val item = NomBank.load("/Users/yuhuan/work/data/nombank/nombank.1.0", "/Users/yuhuan/work/data/penn-tree-bank-3/parsed/mrg/wsj/")
    val breakpoint = 0
  }
}
