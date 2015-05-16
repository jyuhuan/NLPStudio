package nlpstudio.tools.word2vec

import foundation.math.linearalgebra.core.DenseVector
import nlpstudio.core._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/16/15.
 */
class VectorizableWord(word: Int) {
  def vectorize: DenseVector = vectorizer.vectorOf(word)
}

class VectorizablePhrase(phrase: Phrase) {
  def vectorize: DenseVector = vectorizer.vectorOf(phrase)
}

object Vectorizables {
  implicit def wordIsVectorizable(word: Int): VectorizableWord = new VectorizableWord(word)
  implicit def phraseIsVectorizable(phrase: Phrase): VectorizablePhrase = new VectorizablePhrase(phrase)
}

