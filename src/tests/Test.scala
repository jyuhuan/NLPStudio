package tests

import nlpstudio.resources.HeadFinders.{CollinsHeadFinder, FindDirection}
import nlpstudio.resources.penntreebank.PennTreebankNode

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/6/15.
 */



object Test {



  def main(args: Array[String]) {
    val i = PennTreebankNode(2, "I", Seq[String](), null, ArrayBuffer[PennTreebankNode]())
    i.posTag = "NN"
    val love = PennTreebankNode(2, "love", Seq[String](), null, ArrayBuffer[PennTreebankNode]())
    love.posTag = "V"
    val np = PennTreebankNode(1, "NP", Seq[String](), null, ArrayBuffer[PennTreebankNode](i))
    val vp = PennTreebankNode(1, "VP", Seq[String](), null, ArrayBuffer[PennTreebankNode](love))
    val s = PennTreebankNode(0, "S", Seq[String](), null, ArrayBuffer[PennTreebankNode](np, vp))

    

    val bp = 0
  }
}
