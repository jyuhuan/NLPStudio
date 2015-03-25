package learning.feature

import foundation.math.linearalgebra.core.SparseVector

import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
class FeatureValueIndexBase private(featureVectors: Seq[FeatureVector]) {
  val possibleValues = mutable.HashMap[String, mutable.Set[String]]()

  featureVectors.foreach(v ⇒ {
    v.features.filter(f ⇒ f.isInstanceOf[CategoricalFeature]).foreach(f ⇒ {
      val catFeature = f.asInstanceOf[CategoricalFeature]
      if (possibleValues contains catFeature.name) possibleValues(catFeature.name) += catFeature.value
      else possibleValues.put(catFeature.name, mutable.Set[String](catFeature.value))
    })
  })

  val indexWithinSparseVectorLookup = possibleValues.map(x ⇒ (x._1, x._2.zipWithIndex.map(y ⇒ y._1 → y._2).toMap))

  def vectorOf(featureName: String, featureValue: String): SparseVector = {
    val vector = SparseVector.ofDim(possibleValues(featureName).size)
    if (featureValue == null) vector else {
      vector(indexWithinSparseVectorLookup(featureName)(featureValue)) = 1.0
      vector
    }
  }
}

object FeatureValueIndexBase {
  def apply(featureVectors: Seq[FeatureVector]) = new FeatureValueIndexBase(featureVectors)
}