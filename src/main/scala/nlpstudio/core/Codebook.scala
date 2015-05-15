package nlpstudio.core

import learning.exceptions.NoSuchKeyException

import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 4/12/15.
 */
class Codebook {
  var count = 0
  val valueToIndex = mutable.HashMap[String, Int]()
  val indexToValue = mutable.HashMap[Int, String]()
  var isFrozen = false

  def freeze = isFrozen = true
  def thaw = isFrozen = false

  def apply(value: String): Int = {
    if (valueToIndex contains value) valueToIndex(value)
    else {
      if (isFrozen) Codebook.unknownIndex
      else add(value)
    }
  }

  def apply(index: Int): String = {
    if (indexToValue contains index) indexToValue(index) else Codebook.unknownWord
    //else throw new NoSuchKeyException("The key " + index.toString + " does not exist!")
  }

  private def add(value: String): Int = {
    count += 1
    val newIdx = count
    valueToIndex += value → newIdx
    indexToValue += newIdx → value
    newIdx
  }
}

object Codebook {
  val unknownIndex = 0
  val unknownWord = "UNK"
}

object ImplicitCodebooks {
  implicit val vocabularyCodebook = new Codebook()
}