package nlpstudio.resources.nomlexplus

import nlpstudio.io.files.TextFile

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 4/12/15.
 */
object NomLexPlus {
  def load(path: String): Set[String] = {
    val nomlexPlusFile = path + "/NOMLEX-plus-clean.1.0"
    TextFile.readLines(nomlexPlusFile).toSet
  }
}
