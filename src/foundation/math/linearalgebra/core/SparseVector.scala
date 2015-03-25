package foundation.math.linearalgebra.core

import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
class SparseVector(var dimension: Int) {
  val data = mutable.Map[Int, Double]()

  def apply(idx: Int) = if (data contains idx) data(idx) else 0.0

  def update(idx: Int, newVal: Double) = data(idx) = newVal

  def append(after: SparseVector) = {
    val offset = this.dimension
    after.data.keys.foreach(k ⇒ this.data(k + offset) = after.data(k))
    this.dimension += after.dimension
  }

  def toDense: mutable.ArrayBuffer[Double] = {
    val scalars = mutable.ArrayBuffer[Double]()
    for (i ← 0 until this.dimension) {
      if (this.data contains i) scalars += this.data(i)
      else scalars += 0
    }
    scalars
  }

  //override def toString = "[" + toDense.mkString(", ") + "]"
  override def toString = this.data.map(p => p._1 + ":" + p._2).mkString(" ")
}

object SparseVector {
  def ofDim(dimension: Int) = new SparseVector(dimension)
}
