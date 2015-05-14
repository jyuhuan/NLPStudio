package nlpstudio.tools.parsers

import java.io.StringReader

import edu.stanford.nlp.ling.HasWord
import nlpstudio.resources.penntreebank.PennTreebankNode

import scala.collection.JavaConversions._
import scala.collection.mutable

import edu.stanford.nlp.parser.lexparser.LexicalizedParser
import edu.stanford.nlp.process.DocumentPreprocessor
import edu.stanford.nlp.trees.Tree

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/13/15.
 */
class StanfordConstituentParser {
  val parser = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz")

  private def convert(tree: Tree): PennTreebankNode = {
    // The current working node of the NlpStudio tree
    val rootDepth = 0
    val rootNlpStudioNode = PennTreebankNode(rootDepth, tree.label.toString, -1, -1, null, null, null)

    // The fringe for Stanford tree nodes consists of pairs: (Stanford tree node, depth)
    // The fringe for NlpStudio tree nodes consists of only PennTreebank nodes.
    // Here, the stacks allow us to traverse the Stanford tree in a depth-first manner.
    val stanfordFringe = mutable.Stack[(Tree, Int)]((tree, rootDepth))
    val nlpStudioFringe = mutable.Stack[PennTreebankNode](rootNlpStudioNode)

    var curWordIndex = 0

    // Convert the Stanford tree to an NlpStudio tree during depth-first traversal.
    while (stanfordFringe.nonEmpty) {
      // Get the depth and node of the Stanford node on the top of fringe
      val topStanfordSearchNode = stanfordFringe.pop()
      val curStanfordNode = topStanfordSearchNode._1
      val curDepth = topStanfordSearchNode._2

      // Get the node of the NlpStudio node of the top of fringe
      val topNlpStudioNode = nlpStudioFringe.pop()
      println(topNlpStudioNode.toString)

      // Create the children nodes of the NlpStudio node
      for (child ← curStanfordNode.children()) {
        if (child.isPreTerminal) {
          val nlpStudioWordNode = PennTreebankNode(curDepth, child.firstChild().label().toString, 0, 0, null, null, null)
          nlpStudioWordNode.posTag = child.label().toString
          nlpStudioWordNode.wordIndex = curWordIndex
          curWordIndex += 1
          topNlpStudioNode.childrenNodes += nlpStudioWordNode
        }
        else {
          val nlpStudioChild = PennTreebankNode(curDepth, child.label().toString, 0, 0, null, null, null)
          topNlpStudioNode.childrenNodes += nlpStudioChild
          nlpStudioFringe.push(nlpStudioChild)
          stanfordFringe.push((child, curDepth + 1))
        }
      }
    }
    if (rootNlpStudioNode.childrenNodes.nonEmpty) rootNlpStudioNode.childrenNodes.head else null
  }

  def parse(sentence: String) = convert(parser.parse(sentence))

  def parseDocument(document: String): Array[PennTreebankNode] = {
    val stringReader = new StringReader(document)
    val documentPreprocessor = new DocumentPreprocessor(stringReader)
    documentPreprocessor.map(sentence ⇒ convert(parser.parseTree(sentence))).toArray
  }

}
