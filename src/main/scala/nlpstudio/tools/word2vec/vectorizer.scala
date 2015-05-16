package nlpstudio.tools.word2vec

import foundation.math.linearalgebra.core.DenseVector
import nlpstudio.core._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/16/15.
 */
object vectorizer {
  val model = new Word2Vec()
  model.load("/Users/yuhuan/work/apps/word2vec/vectors.bin")
  val dimension = model.vectorSize

  def vectorOf(word: Int)(implicit cb: Codebook): DenseVector = DenseVector(model.vector(cb(word)))

  def vectorOf(phrase: Phrase)(implicit cb: Codebook): DenseVector = {
    phrase.words.map(w ⇒ vectorOf(w)).foldLeft(DenseVector.zeroes(dimension))((v1, v2) ⇒ v1 + v2)
  }
}
