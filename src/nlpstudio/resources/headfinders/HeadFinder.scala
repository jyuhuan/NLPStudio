package nlpstudio.resources.headfinders

import nlpstudio.resources.penntreebank.PennTreebankNode

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/22/15.
 */
trait HeadFinder {
  def find(ptbNode: PennTreebankNode): PennTreebankNode
  def apply(ptbNode: PennTreebankNode): PennTreebankNode = find(ptbNode)
}
