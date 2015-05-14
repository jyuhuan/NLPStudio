package nlpstudio.tools.parsers

import java.io.StringReader

import edu.stanford.nlp.ling._
import edu.stanford.nlp.parser.nndep.DependencyParser
import edu.stanford.nlp.process
import edu.stanford.nlp.process._
import edu.stanford.nlp.tagger.maxent.MaxentTagger
import edu.stanford.nlp.trees.GrammaticalStructure
import foundation.graph.Graph
import nlpstudio.resources.core.DependencyParseTree

import scala.collection.JavaConversions._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
class StanfordDependencyParser {
  val tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger")
  val parser = DependencyParser.loadFromModelFile(DependencyParser.DEFAULT_MODEL)

  private def parseTaggedSentence(taggedSentence: java.util.List[TaggedWord]): GrammaticalStructure = parser.predict(taggedSentence)

  private def convert(gs: GrammaticalStructure): DependencyParseTree = {
    val tree = new DependencyParseTree()
    for (typedDependency <- gs.allTypedDependencies()) {
      val govWord = typedDependency.gov().word()
      val govWordIndex = typedDependency.gov().index()
      val depWord = typedDependency.dep().word()
      val depWordIndex = typedDependency.dep().index()
      val relation = typedDependency.reln().toString
      tree.addDirectedEdge("[" + govWordIndex + "] " + govWord, relation, "[" + depWordIndex + "] " + depWord)
    }
    tree
  }

  def parse(sentence: String): DependencyParseTree = {
    val docPreprocessor = new DocumentPreprocessor(new StringReader(sentence))
    val words = docPreprocessor.head
    val taggedSentence = tagger.tagSentence(words)
    convert(parseTaggedSentence(taggedSentence))
  }

  def parseDocument(document: String): Array[DependencyParseTree] = {
    val docPreprocessor = new DocumentPreprocessor(new StringReader(document))
    (for (sentence ← docPreprocessor) yield {
      val taggedSentence = tagger.tagSentence(sentence)
      convert(parseTaggedSentence(taggedSentence))
    }).toArray
  }
}
