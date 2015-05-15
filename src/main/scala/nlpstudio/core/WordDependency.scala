package nlpstudio.core

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
class WordDependency(val governor: DependencyParseNode, val dependencyName: String, val dependent: DependencyParseNode) extends DirectedLink[Int] {
  override def name: String = dependencyName
  override def from: Int = governor.word
  override def to: Int = dependent.word
  override def toString: String = dependencyName
}
