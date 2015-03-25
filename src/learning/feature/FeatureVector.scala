package learning.feature

import scala.collection.mutable
/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/24/15.
 */
class FeatureVector(var features: mutable.ArrayBuffer[Feature]) {
  def add(newFeature: Feature) = {
    this.features += newFeature
  }

  override def toString = this.features.mkString(", ")
}

object FeatureVector {
  def apply(features: Feature*) = new FeatureVector(mutable.ArrayBuffer[Feature](features: _*))
}