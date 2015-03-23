package nlpstudio.resources.penntreebank

/**
 * Created by yuhuan on 3/17/15.
 */
class PennTreebankEntry private(var corpusName: String,
                        var sectionId: Int,
                        var mrgFileId: Int,
                        var sentenceId: Int,
                        var tree: PennTreebankNode) {

  def wordNodes = tree.wordNodes

}

object PennTreebankEntry {
  def apply(corpusName: String,
            sectionId: Int,
            mrgFileId: Int,
            sentenceId: Int,
            rootPtbNode: PennTreebankNode) = new PennTreebankEntry(corpusName, sectionId, mrgFileId, sentenceId, rootPtbNode)
}