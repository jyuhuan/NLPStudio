package learning.feature

import foundation.math.linearalgebra.core.SparseVector
import scala.collection.mutable
/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
object FeatureVectorRealizer {
  def realize(featureVectors: Seq[FeatureVector], indexBase: FeatureValueIndexBase): Array[SparseVector] = {

    val f = featureVectors.map(v ⇒ {
      v.features.map(f ⇒ {
        if (f.isInstanceOf[CategoricalFeature]) {
          val catF = f.asInstanceOf[CategoricalFeature]
          indexBase.vectorOf(catF.name, catF.value)
        }
        else {
          val numF = f.asInstanceOf[NumericalFeature]
          val vector = SparseVector.ofDim(1)
          vector(0) = numF.value
          vector
        }
      })
    })

    val sparseVectors = f.map(a ⇒ {
      val concat = a.head
      a.tail.foreach(x ⇒ concat.append(x))
      concat
    })

    sparseVectors.toArray
  }
}
