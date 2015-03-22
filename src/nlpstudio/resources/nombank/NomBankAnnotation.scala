package nlpstudio.resources.nombank

import nlpstudio.resources.penntreebank.PennTreebankNode

/**
 * Created by yuhuan on 3/21/15.
 */
// num:num    num:num,num     num


/**
 * One annotation in NomBank.
 * For example, "4:0*9:1-ARG1-PRD" for the predicate "consensus" in wsj/24/wsj_2429.mrg would be:
 *   NomBankAnnotation([c1, c2], PointerType.NotAConstituent, ["ARG1"-"PRD"])
 *   TODO: find out what c1 and c2 are
 * @param nodes Nodes being annotated on.
 * @param pointerType The type of the pointer in this annotation.
 * @param label The label of this annotation. Can be one of:
 *                { REL, SUPPORT, ARGM, ARG[0-9] }
 * @param functionTags The function tags for this annotation.
 */
case class NomBankAnnotation(nodes: Seq[PennTreebankNode], pointerType: PointerType, label:String, functionTags: Seq[String])
