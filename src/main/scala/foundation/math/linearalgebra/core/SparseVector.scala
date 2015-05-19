package foundation.math.linearalgebra.core

import scala.collection.mutable
import scala.collection.JavaConversions._

import java.util.TreeMap

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
class SparseVector(var dimension: Int) {
  val data = new TreeMap[Int, Double]()

  def apply(idx: Int) = if (data contains idx) data(idx) else 0.0

  def update(idx: Int, newVal: Double) = {
    if (idx >= this.dimension) this.dimension = idx + 1
    data(idx) = newVal
  }

  def append(after: SparseVector) = {
    val offset = this.dimension
    after.data.keys.foreach(k ⇒ this.data(k + offset) = after.data(k))
    this.dimension += after.dimension
  }

  def append(value: Double) = {
    this.data(this.dimension) = value
    this.dimension += 1
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
  override def toString = {
    var result = ArrayBuffer[String]()
    for (e ← this.data.entrySet()) {
      val value = e.getValue
      if (value != 0) {
        result += e.getKey + ":" + value
      }
    }
    result.mkString(" ")
  }

  def toMatlabString = {
    var result = ArrayBuffer[String]()
    for (e ← this.data.entrySet()) {
      val value = e.getValue
      if (value != 0) {
        result += e.getKey + 1 + ":" + value
      }
    }
    result.mkString(" ")
  }
}

object SparseVector {
  def ofDim(dimension: Int) = new SparseVector(dimension)
}
