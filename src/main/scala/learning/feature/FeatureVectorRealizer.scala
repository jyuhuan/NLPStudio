package learning.feature

import foundation.math.linearalgebra.core.SparseVector
import me.tongfei.progressbar.ProgressBar
import nlpstudio.core.Codebook
import scala.collection.mutable
/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
object FeatureVectorRealizer {
  val codebooks = mutable.HashMap[Int, Codebook]()

  def realize(featureVectors: Array[FeatureVector], freezeCodebooks: Boolean): Array[SparseVector] = {

    val vectorCount = featureVectors.length
    val featureCount = featureVectors.head.count

    if (!freezeCodebooks) {
      // Initialize codebooks
      for (i ← 0 until featureCount) {
        val firstFeature = featureVectors.head
        if (firstFeature.features(i).isInstanceOf[CategoricalFeature]) {
          val newCb = new Codebook()
          codebooks += (firstFeature.features(i).name → newCb)
          for (j ← 0 until vectorCount) {
            newCb(featureVectors(j)(i).valueAsString)
          }
        }
      }
    }

    codebooks.values.foreach(c ⇒ c.freeze)


    // The result sparse vectors
    val sparseVectors = Array.ofDim[SparseVector](vectorCount)
    for (i ← 0 until vectorCount) {
      sparseVectors(i) = SparseVector.ofDim(0)
    }

    val pgb = new ProgressBar("SparseVector Gen", featureCount * vectorCount)
    pgb.start()

    // count indices for each dimension
    for (i ← 0 until featureCount) {
      val firstFeature = featureVectors.head
      if (firstFeature.features(i).isInstanceOf[CategoricalFeature]) {
        // Handle categorical feature
        //val codebook = featureVectors.map(fv ⇒ fv.features(i).asInstanceOf[CategoricalFeature].value).toSet.zipWithIndex.toMap[String, Int]
        //val featureName = firstFeature.features(i).na

        val featureName = firstFeature(i).name
        val codebook = codebooks(firstFeature(i).name)

        for (j ← 0 until vectorCount) {
          pgb.step()
          val featureValue = featureVectors(j)(i).asInstanceOf[CategoricalFeature].value
          val featureValuePos = codebook(featureValue) - 1

          val sparseVector = SparseVector.ofDim(codebook.count)
          if (featureValuePos != -1) sparseVector(featureValuePos) = 1

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
