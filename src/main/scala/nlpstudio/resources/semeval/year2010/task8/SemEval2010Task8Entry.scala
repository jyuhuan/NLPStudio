package nlpstudio.resources.semeval.year2010.task8

import nlpstudio.core._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/15/15.
 */
case class SemEval2010Task8Entry(id: Int,
                                 sentence: Sentence,
                                 e1StartPos: Int,
                                 e1EndPos: Int,
                                 e2StartPos: Int,
                                 e2EndPos: Int,
                                 annotation: OrderedRelation[Phrase])
