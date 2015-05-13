package learning.feature

import foundation.math.linearalgebra.core.SparseVector

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
case class NumericalFeature(name: Int, value: Double) extends Feature {
  override def toString = name + ": " + value
}
