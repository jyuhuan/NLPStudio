package learning.feature

import foundation.math.linearalgebra.core.SparseVector

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
trait Feature {
  def name: Int

  def valueAsString = {
    if (this.isInstanceOf[CategoricalFeature]) {
      this.asInstanceOf[CategoricalFeature].value
    }
    else {
      this.asInstanceOf[NumericalFeature].value.toString
    }
  }
}
