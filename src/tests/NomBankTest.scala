package tests

import nlpstudio.resources.nombank.NomBank
import nlpstudio.r


/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/11/15.
 */
object NomBankTest {
  def main(args: Array[String]) {

    val nb = NomBank.loadAsFineGrainedEntries(r.nom_bank, r.penn_treebank_wsj)

    val a = nb.map(n ⇒ n.candidateNode.semanticHead)

    val breakpoint = 0
  }
}
