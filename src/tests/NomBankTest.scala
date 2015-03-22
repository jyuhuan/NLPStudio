package tests

import nlpstudio.resources.nombank.NomBank
import nlpstudio.resources.penntreebank.PennTreebank
import nlpstudio.r

import scala.collection.mutable.ArrayBuffer


/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/11/15.
 */
object NomBankTest {
  def main(args: Array[String]) {

    val nb = NomBank.load(r.nom_bank, r.penn_treebank_wsj)




    val breakpoint = 0
  }
}
