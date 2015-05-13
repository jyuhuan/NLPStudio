package learning.feature

import scala.collection.mutable
/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
class FeatureVector(var features: mutable.ArrayBuffer[Feature]) {
  def add(newFeature: Feature) = {
    this.features += newFeature
  }

  def apply(idx: Int) = this.features(idx)

  def count = this.features.length

  override def toString = this.features.mkString(", ")
}

object FeatureVector {
  def apply(features: Seq[Feature]) = new FeatureVector(mutable.ArrayBuffer[Feature](features: _*))
  def apply() = new FeatureVector(mutable.ArrayBuffer[Feature]())
}