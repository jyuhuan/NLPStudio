package nlpstudio.io.corpora.nombank

import foundation.math.graph.Node
import nlpstudio.io.corpora.nombank.NomBankEntry
import nlpstudio.io.corpora.penntreebank.{PennTreebank, PennTreebankEntry}
import nlpstudio.io.files.TextFile
import nlpstudio.r
import nlpstudio.utilities.PathHandler

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/11/15.
 */
object NomBank {

  private def parseAnnotations(tokenNodes: Seq[Node[String]], annotation: String) = {
    val fields = annotation.split("-")

    val pointers = fields(0)

    var glueType = 1

    var nodesAffected: Seq[Node[String]] = null

    if (pointers.contains('*')) {
      glueType = 2
      val pointerFields = pointers.split('*')
      nodesAffected = pointerFields.map(s ⇒ {
        val tokenIdAndUpLevel = s.split(':')
        val tokenId = tokenIdAndUpLevel(0).toInt
        val upLevel = tokenIdAndUpLevel(1).toInt

        var token = tokenNodes(tokenId)
        for (i ← 0 until upLevel) {
          token = token.parent
        }
        token
      })
    }
    else {
      val pointerFields = pointers.split(',')
      nodesAffected = pointerFields.map(s ⇒ {
        val tokenIdAndUpLevel = s.split(':')
        val tokenId = tokenIdAndUpLevel(0).toInt
        val upLevel = tokenIdAndUpLevel(1).toInt

        var token = tokenNodes(tokenId)
        for (i ← 0 until upLevel) {
          token = token.parent
        }
        token
      })
    }
    NomBankAnnotationUnit(nodesAffected, glueType, fields.slice(1, fields.length))
  }



  // <no this part> wsj/05/wsj_0524.mrg 52 </no this part> 4 account 03 1:0*12:1-ARG0 2:0,3:0-Support 4:0-rel 5:2-ARG1
  def loadSingleEntry(fields: Seq[String], parseTree: PennTreebankEntry): NomBankEntry = {

    // Get each field
    val predicateTokenId = fields(0).toInt
    val stemmedPredicate = fields(1)
    val senseId = fields(2).toInt
    val rawAnnotations = fields.slice(3, fields.length)

    // Obtain leaves of the parse tree
    val tokenNodes = parseTree.leaves

    // Annotations (contains the predicate)
    val parsedAnnotations = rawAnnotations.map(a ⇒ parseAnnotations(tokenNodes, a))

    NomBankEntry(tokenNodes(predicateTokenId).data, stemmedPredicate, parsedAnnotations, parseTree)
  }

  def load(path: String): Seq[NomBankEntry] = {
    val ptb = PennTreebank.load(PathHandler.concat(r.pennTreebankDir, "wsj/"))


    val nomBankEntries = ArrayBuffer[NomBankEntry]()
    for (line ← TextFile.readLines(path)) {
      val fields = line.split(' ')
      val treeRelativePath = fields(0)
      val pathParts = treeRelativePath.split('/')
      val sectionId = pathParts(1).toInt
      val mrgFileId = pathParts(2).substring(6, 8).toInt
      val treeId = fields(1).toInt
      val parseTree = ptb(sectionId)(mrgFileId)(treeId)
      nomBankEntries += loadSingleEntry(fields.slice(2, fields.length), parseTree)
    }
    nomBankEntries
  }

}
