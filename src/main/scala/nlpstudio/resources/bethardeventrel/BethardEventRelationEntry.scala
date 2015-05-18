package nlpstudio.resources.bethardeventrel

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/17/15.
 */
case class BethardEventRelationEntry(
                                 id: Int,
                                 causalLabel: BethardCausalLabels,
                                 temporalLabel: BethardTemporalLabels,
                                 e1SectionId: Int, e1FileId: Int, e1SentenceId: Int, e1WordId: Int,
                                 e2SectionId: Int, e2FileId: Int, e2SentenceId: Int, e2WordId: Int
                                 )