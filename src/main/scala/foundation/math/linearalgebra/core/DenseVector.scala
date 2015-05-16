package foundation.math.linearalgebra.core

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/16/15.
 */
class DenseVector(val data: Array[Double]) {
  val dimension = data.length

  def add(that: DenseVector): DenseVector = {
    val thisData = this.data
    val thatData = that.data
    val newData = Array.ofDim[Double](dimension)
    for (i ← 0 until dimension) {
      newData(i) = thisData(i) + thatData(i)
    }
    new DenseVector(newData)
  }
  def +(that: DenseVector): DenseVector = add(that)
}

object DenseVector {
  def apply(array: Array[Double]): DenseVector = ofArray(array)
  def ofDim(dimension: Int): DenseVector = new DenseVector(Array.ofDim[Double](dimension))
  def ofArray(array: Array[Double]): DenseVector = new DenseVector(array)
}
