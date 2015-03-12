package nlpstudio.io.corpora.nombank
import foundation.math.graph._
import nlpstudio.io.corpora.penntreebank.PennTreebankEntry

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/11/15.
 */

// num:num    num:num,num     num
/**
 * One annotation in NomBank. Consists of (1) which nodes are annotated, and (2) what the label is.
 * This corresponds to "1:0*12:1-ARG0"
 * @param nodes Nodes annotated.
 * @param glueType How are these nodes connected. 1 means "1:0,3:1,6:2". 2 means "1:0*3:1*6:2"
 * @param labels The labels of these nodes.
 */
case class NomBankAnnotationUnit(nodes: Seq[Node[String]], glueType: Int, labels: Seq[String])

case class NomBankEntry(predicate: String, stemmedPredicate: String, annotations: Seq[NomBankAnnotationUnit], parseTree: PennTreebankEntry)
