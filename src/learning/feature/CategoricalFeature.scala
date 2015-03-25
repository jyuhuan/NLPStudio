package learning.feature

import foundation.math.linearalgebra.core.SparseVector

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
case class CategoricalFeature(name: String, value: String) extends Feature {
  override def toString = name + ": " + value
}
