package learning.feature

import foundation.math.linearalgebra.core.SparseVector
import me.tongfei.progressbar.ProgressBar
import scala.collection.mutable
/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
object FeatureVectorRealizer {
  def realize(featureVectors: Array[FeatureVector]): Array[SparseVector] = {
    val vectorCount = featureVectors.length
    val featureCount = featureVectors.head.count

    // The result sparse vectors
    val sparseVectors = Array.ofDim[SparseVector](vectorCount)
    for (i ← 0 until vectorCount) {
      sparseVectors(i) = SparseVector.ofDim(0)
    }

    val pgb = new ProgressBar("SparseVector Gen", featureCount * vectorCount)
    pgb.start()

    // count indices for each dimension
    for (i ← 0 until featureCount) {
      if (featureVectors.head.features(i).isInstanceOf[CategoricalFeature]) {
        // Handle categorical feature
        val codebook = featureVectors.map(fv ⇒ fv.features(i).asInstanceOf[CategoricalFeature].value).toSet.zipWithIndex.toMap[String, Int]
        for (j ← 0 until vectorCount) {
          pgb.step()
          val featureValue = featureVectors(j)(i).asInstanceOf[CategoricalFeature].value
          val featureValuePos = codebook(featureValue)

          val sparseVector = SparseVector.ofDim(codebook.size)
          sparseVector(featureValuePos) = 1

          sparseVectors(j).append(sparseVector)
          val bp = 0
        }
      }
      else {
        // Handle numeric feature
        for (j ← 0 until vectorCount) {
          pgb.step()
          val featureValue = featureVectors(j)(i).asInstanceOf[NumericalFeature].value
          sparseVectors(j).append(featureValue)
        }
      }
    }
    pgb.stop()

    sparseVectors
  }
}
