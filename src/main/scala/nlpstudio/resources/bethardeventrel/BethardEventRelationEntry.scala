package nlpstudio.resources.bethardeventrel

import nlpstudio.resources.penntreebank._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/17/15.
 */
case class BethardEventRelationEntry(
                                 id: Int,
                                 causalLabel: BethardCausalLabels,
                                 temporalLabel: BethardTemporalLabels,
                                 parseTree: PennTreebankEntry,
                                 e1Node: PennTreebankNode,
                                 e2Node: PennTreebankNode
                                 )