package nlpstudio.resources.nombank

import nlpstudio.resources.penntreebank.{PennTreebankEntry, PennTreebankNode}

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */

/**
 * This differs from NomBankEntry. A NomBankEntry corresponds to a PennTreebank entry (one tree),
 * and since a PennTreebank tree can contain more than one predicate, and many arguments, then a
 * NomBankEntry contains more than one annotation.
 *
 * This class is made in case you need a (predicate, candidate) style entry for training. Each
 * [[nlpstudio.resources.nombank.NomBankFineGrainedEntry]] has only one annotation.
 *
 * @param sectionId
 * @param mrgFileId
 * @param treeId
 * @param predicateNode
 * @param stemmedPredicate
 * @param senseId
 * @param candidateNode
 * @param label
 * @param functionTags
 * @param parseTree
 */
case class NomBankFineGrainedEntry(sectionId: Int,
                                   mrgFileId: Int,
                                   treeId: Int,
                                   predicateNode: PennTreebankNode,
                                   stemmedPredicate: String,
                                   senseId: Int,
                                   candidateNode: PennTreebankNode,
                                   label: String,
                                   functionTags: Seq[String],
                                   parseTree: PennTreebankEntry)
