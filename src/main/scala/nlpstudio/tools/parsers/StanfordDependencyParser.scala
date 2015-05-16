package nlpstudio.tools.parsers

import java.io.StringReader

import edu.stanford.nlp.ling._
import edu.stanford.nlp.parser.nndep.DependencyParser
import edu.stanford.nlp.process
import edu.stanford.nlp.process._
import edu.stanford.nlp.tagger.maxent.MaxentTagger
import edu.stanford.nlp.trees.GrammaticalStructure
import foundation.graph.Graph
import nlpstudio._
import nlpstudio.core._

import scala.collection.JavaConversions._
import nlpstudio.core.GlobalCodebooks._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
class StanfordDependencyParser {
  val tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger")
  val parser = DependencyParser.loadFromModelFile(DependencyParser.DEFAULT_MODEL)

  private def parseTaggedSentence(taggedSentence: java.util.List[TaggedWord]): GrammaticalStructure = parser.predict(taggedSentence)

  private def convert(gs: GrammaticalStructure): DependencyParse = {
    val parse = new DependencyParse()
    for (typedDependency <- gs.allTypedDependencies()) {
      //val govWord1 = typedDependency.gov().word()
      val govWord = cb(typedDependency.gov().word())
      val govWordIndex = typedDependency.gov().index() - 1
      //val depWord = typedDependency.dep().word()
      val depWord = cb(typedDependency.dep().word())
      val depWordIndex = typedDependency.dep().index() - 1
      val relation = typedDependency.reln().toString
      //dependencyParse.addDirectedEdge("[" + govWordIndex + "] " + govWord, relation, "[" + depWordIndex + "] " + depWord)
      parse.add(new WordDependency(new DependencyParseNode(govWord, govWordIndex, parse), relation, new DependencyParseNode(depWord, depWordIndex, parse)))
    }
    parse
  }

  def parse(sentence: String): DependencyParse = {
    val docPreprocessor = new DocumentPreprocessor(new StringReader(sentence))
    val words = docPreprocessor.head
    val taggedSentence = tagger.tagSentence(words)
    convert(parseTaggedSentence(taggedSentence))
  }

  def parseDocument(document: String): Array[DependencyParse] = {
    val docPreprocessor = new DocumentPreprocessor(new StringReader(document))
    (for (sentence ← docPreprocessor) yield {
      val taggedSentence = tagger.tagSentence(sentence)
      convert(parseTaggedSentence(taggedSentence))
    }).toArray
  }
}
