package nlpstudio.resources.nombank

import java.io.PrintWriter

import nlpstudio.io.files.TextFile
import nlpstudio.resources.penntreebank.{PennTreebank, PennTreebankEntry, PennTreebankNode}

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
   * Parses "4:0*9:1-ARG1-PRD" to a NomBankAnnotation object.
   * @param parseTree The parse tree this annotation annotates on.
   * @param annotation The annotation in the form of "4:0*9:1-ARG1-PRD".
   */
  private def parseAnnotations(parseTree: PennTreebankEntry, annotation: String): NomBankAnnotation = {
    val pointersAndTags = annotation.split("-") // = ["4:0*9:1", "ARG1", "PRD"]
    val pointerStrings = pointersAndTags(0)
    val tagStrings = pointersAndTags(1)

    // Determine whether the pointers are connected with "*", meaning that they are coreferences; or
    // they are connected with "-", meaning they are simply concatenation due to not being under
    // one constituent.
    val pointerType = if (pointerStrings contains '*') {
      PointerType.Coreference
    }
    else if (pointerStrings contains ',') {
      PointerType.NotAConstituent
    }
    else PointerType.Single

    val tokens = parseTree.wordNodes

    val pointers = if (pointerType == PointerType.Coreference) {
      pointerStrings.split('*')
    }
    else if (pointerType == PointerType.NotAConstituent) {
      pointerStrings.split(',')
    }
    else Array[String](pointerStrings)

    val affectedNodes = pointers.map(s ⇒ {
      val tokenAndSteps = s.split(':')
      val tokenId = tokenAndSteps(0).toInt
      val steps = tokenAndSteps(1).toInt
      var curNode = tokens(tokenId)
      for (i ← 0 until steps) {
        curNode = curNode.parentNode
      }
      curNode
    })

    val labelAndFunctionTags = tagStrings.split('-')
    val label = labelAndFunctionTags(0)
    val functionTags = labelAndFunctionTags.slice(1, labelAndFunctionTags.length)
    if (affectedNodes(0) == null) {
      println("found")
      val bp = 0
    }
    NomBankAnnotation(affectedNodes, pointerType, label, functionTags)
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
  private def loadSingleEntry(entry: String, parseTree: PennTreebankEntry): NomBankEntry = {

    val fields = entry.split(' ')
    val treeRelativePath = fields(0)
    val pathParts = treeRelativePath.split('/')
    val sectionId = pathParts(1).toInt
    val mrgFileId = pathParts(2).substring(6, 8).toInt
    val treeId = fields(1).toInt
    val tokenId = fields(2).toInt
    val predicateBaseForm = fields(3)
    val senseId = fields(4).toInt
    val annotationStrings = fields.slice(5, fields.length)

//    try {
      val annotations = annotationStrings.map(a ⇒ parseAnnotations(parseTree, a))
//    }
//    catch {
//      case e: Exception ⇒ {
//        val breakpoint = 0
//      }
//    }

    NomBankEntry(sectionId, mrgFileId, treeId, parseTree.wordNodes(tokenId), predicateBaseForm, senseId, annotations, parseTree)
  }


  /**
   * Loads NomBank 1.0. Users can iterate over the return value by NomBankEntry's.
   * @param pathToNomBank Path to the file "nombank.1.0"
   * @param pathToPennTreebank Path to the directory "parsed/mrg/wsj"
   * @return A sequence of NomBankEntry's.
   */
  def load(pathToNomBank: String, pathToPennTreebank: String): Array[NomBankEntry] = {
    val ptb = PennTreebank.load(pathToPennTreebank)


    val entries = for (line ← TextFile.readLines(pathToNomBank)) yield {
      val fields = line.split(' ')
      val treeRelativePath = fields(0)
      val pathParts = treeRelativePath.split('/')
      val sectionId = pathParts(1).toInt
      val mrgFileId = pathParts(2).substring(6, 8).toInt
      val treeId = fields(1).toInt
      val parseTree = ptb(sectionId)(mrgFileId)(treeId)

      loadSingleEntry(line, parseTree)
    }

    entries.toArray
  }

  def loadAsFineGrainedEntries(pathToNomBank: String, pathToPennTreebank: String): Array[NomBankFineGrainedEntry] = {
    val coarseEntries = load(pathToNomBank, pathToPennTreebank)
    coarseEntries.flatMap(e ⇒ e.annotations.flatMap(a ⇒ a.nodes.map(n ⇒ {
      if (n == null) {
        val bp = 0
      }
      NomBankFineGrainedEntry(e.sectionId, e.mrgFileId, e.treeId, e.predicateNode, e.stemmedPredicate, e.senseId, n, a.label, a.functionTags, e.parseTree)
    })))
  }

}
