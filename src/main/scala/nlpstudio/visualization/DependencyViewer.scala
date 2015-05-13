package nlpstudio.visualization

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 2/7/15.
 */

import java.io.StringReader

import edu.stanford.nlp.parser.nndep.DependencyParser
import edu.stanford.nlp.process.DocumentPreprocessor
import edu.stanford.nlp.tagger.maxent.MaxentTagger
import edu.stanford.nlp.trees.TypedDependency

import scala.collection.JavaConverters._

import nlpstudio.visualization.core.GraphViewer

import foundation.math.graph.Graph


object DependencyViewer {
  def main(args: Array[String]) {


    val parserModelPath = DependencyParser.DEFAULT_MODEL
    val taggerModelPath = "lib/models/english-left3words-distsim.tagger"


    val tagger = new MaxentTagger(taggerModelPath)
    val parser = DependencyParser.loadFromModelFile(parserModelPath)

    while (true) {
      println("Type your sentence here: ")
      val graph = new Graph[String, String]()
      val text = scala.io.StdIn.readLine()
      val tokenizer = new DocumentPreprocessor(new StringReader(text))
      for (sentence ← tokenizer.asScala) {
        val tagged = tagger.tagSentence(sentence)
        val grammaticalStructure = parser.predict(tagged)

        for (typedDependency <- grammaticalStructure.allTypedDependencies().asScala) {
          val govWord = typedDependency.gov().word()
          val govWordIndex = typedDependency.gov().index()

          val depWord = typedDependency.dep().word()
          val depWordIndex = typedDependency.dep().index()

          val relation = typedDependency.reln().toString

          graph.addDirectedEdge("[" + govWordIndex + "] " + govWord, relation, "[" + depWordIndex + "] " + depWord)
        }
      }

      val viewer = new GraphViewer(graph)
      viewer.showWindow()

    }


    val breakpoint = 0
  }
}
