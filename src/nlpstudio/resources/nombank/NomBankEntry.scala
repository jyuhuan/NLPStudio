package nlpstudio.resources.nombank

import foundation.math.graph._
import nlpstudio.resources.penntreebank.{PennTreebankNode, PennTreebankEntry}

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/11/15.
 */


/**
 * An entry of NomBank 1.0.
 * @param sectionId Section in the WSJ. E.g.: 0 for "00", 1 for "01", ..., 24 for "24".
 * @param mrgFileId The id of *.mrg file in the section. E.g., 1 for "wsj_0801" in section 08.
 *                  Important notice: Since the section 00 does not have wsj_0000.mrg (it starts with
 *                  wsj_0001.mrg, which is very very very very annoying!!!), you should create a fake
 *                  wsj_0000.mrg under wsj/00/. And since this file is to be parsed by this library,
 *                  you should put at least one bracket-style parse tree in it. A simple way to do this,
 *                  is to make a copy of any wsj_????.mrg file that's already in wsj/00/, and rename it
 *                  to wsj_0000.mrg.
 * @param treeId The index (starting form 0) of sentence in a section.
 * @param predicateNode The node of the predicate.
 * @param stemmedPredicate The stemmed form of the predicate.
 * @param senseId Which sense of the predicate is used in the sentence.
 * @param annotations Annotations for the predicate.
 * @param parseTree The parse tree that these annotations annotate over.
 */
case class NomBankEntry(sectionId: Int, mrgFileId: Int, treeId: Int, predicateNode: PennTreebankNode, stemmedPredicate: String, senseId: Int, annotations: Seq[NomBankAnnotation], parseTree: PennTreebankEntry)
