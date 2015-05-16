package nlpstudio.tools.word2vec

import foundation.math.linearalgebra.core.DenseVector
import nlpstudio.core._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/16/15.
 */
class VectorizableWord(word: Int)(implicit cb: Codebook) {
  def vectorize: DenseVector = vectorizer.vectorOf(word)
}

class VectorizablePhrase(phrase: Phrase)(implicit cb: Codebook) {
  def vectorize: DenseVector = vectorizer.vectorOf(phrase)
}

object Vectorizables {
  implicit def wordIsVectorizable(word: Int)(implicit cb: Codebook): VectorizableWord = new VectorizableWord(word)
  implicit def phraseIsVectorizable(phrase: Phrase)(implicit cb: Codebook): VectorizablePhrase = new VectorizablePhrase(phrase)
}

