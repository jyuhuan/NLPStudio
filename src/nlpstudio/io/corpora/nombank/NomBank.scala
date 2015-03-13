package nlpstudio.io.corpora.nombank

import foundation.math.graph.Node
import nlpstudio.io.corpora.penntreebank.{PennTreebank, PennTreebankEntry}
import nlpstudio.io.files.TextFile

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/11/15.
 */

/**
 * A reader for NomBank 1.0.
 * Important notice: Since the section 00 does not have wsj_0000.mrg (it starts with
 * wsj_0001.mrg, which is very very very very annoying!!!), you should create a fake
 * wsj_0000.mrg under wsj/00/. And since this file is to be parsed by this library,
 * you should put at least one bracket-style parse tree in it. A simple way to do this,
 * is to make a copy of any wsj_????.mrg file that's already in wsj/00/, and rename it
 * to wsj_0000.mrg.
 */
object NomBank {

  /**
   * Turns "1:0*12:1-ARG0 2:0,3:0-Support 4:0-rel 5:2-ARG1" to NomBankAnnotationUnit's
   * @param tokenNodes The leaves of the parse tree that these annotations annotate over.
   * @param annotation The annotation string in NomBank.
   *                   Has the form of "1:0*12:1-ARG0 2:0,3:0-Support 4:0-rel 5:2-ARG1"
   * @return An iterable of NomBankAnnotationUnit's.
   */
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
        for (i ← 0 until upLevel + 1) {
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




  /**
   * Loads a single entry of NomBank.
   * @param sectionId Section in the WSJ. E.g.: 0 for "00", 1 for "01", ..., 24 for "24".
   * @param mrgFileId The id of *.mrg file in the section. E.g., 1 for "wsj_0801" in section 08.
   *                  Important notice: Since the section 00 does not have wsj_0000.mrg (it starts with
   *                  wsj_0001.mrg, which is very very very very annoying!!!), you should create a fake
   *                  wsj_0000.mrg under wsj/00/. And since this file is to be parsed by this library,
   *                  you should put at least one bracket-style parse tree in it. A simple way to do this,
   *                  is to make a copy of any wsj_????.mrg file that's already in wsj/00/, and rename it
   *                  to wsj_0000.mrg.
   * @param sentenceId The index (starting form 0) of sentence in a section.
   * @param fields A sequence of fields. Has the form:
   *               "4 account 03 1:0*12:1-ARG0 2:0,3:0-Support 4:0-rel 5:2-ARG1"
   * @param parseTree The parse tree that the annotation contained in this entry annotate over.
   * @return
   */
  private def loadSingleEntry(sectionId: Int, mrgFileId: Int, sentenceId: Int, fields: Seq[String], parseTree: PennTreebankEntry): NomBankEntry = {

    // Get each field
    val predicateTokenId = fields(0).toInt
    val stemmedPredicate = fields(1)
    val senseId = fields(2).toInt
    val rawAnnotations = fields.slice(3, fields.length)

    // Obtain leaves of the parse tree
    val tokenNodes = parseTree.leaves

    // Annotations (without the predicate)
    val parsedAnnotations = rawAnnotations.map(a ⇒ parseAnnotations(tokenNodes, a)).filterNot(u ⇒ u.labels.contains("rel"))

    NomBankEntry(sectionId, mrgFileId, sentenceId, tokenNodes(predicateTokenId), stemmedPredicate, senseId, parsedAnnotations, parseTree)
  }


  /**
   * Loads NomBank 1.0. Users can iterate over the return value by NomBankEntry's.
   * @param pathToNomBank Path to the file "nombank.1.0"
   * @param pathToPennTreebank Path to the directory "parsed/mrg/wsj"
   * @return A sequence of NomBankEntry's.
   */
  def load(pathToNomBank: String, pathToPennTreebank: String): Seq[NomBankEntry] = {
    val ptb = PennTreebank.load(pathToPennTreebank)


    val nomBankEntries = ArrayBuffer[NomBankEntry]()
    for (line ← TextFile.readLines(pathToNomBank)) {
      val fields = line.split(' ')
      val treeRelativePath = fields(0)
      val pathParts = treeRelativePath.split('/')
      val sectionId = pathParts(1).toInt
      val mrgFileId = pathParts(2).substring(6, 8).toInt
      val treeId = fields(1).toInt
      val parseTree = ptb(sectionId)(mrgFileId)(treeId)
      nomBankEntries += loadSingleEntry(sectionId, mrgFileId, treeId, fields.slice(2, fields.length), parseTree)
    }
    nomBankEntries
  }

}
