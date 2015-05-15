package nlpstudio.core

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
class WordDependency(val governor: DependencyParseNode, val dependencyName: String, val dependent: DependencyParseNode) extends Link[Word, Word] {
  override def name: String = dependencyName
  override def x: Word = governor.word
  override def y: Word = dependent.word
  override def toString: String = dependencyName
}
