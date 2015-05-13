package nlpstudio.utilities

import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/11/15.
 */
class UniqueNamer {
  val namesAndIndices = mutable.HashMap[String, Int]()
  def apply(n: String): String = {
    if (!namesAndIndices.contains(n)) {
      namesAndIndices.put(n, 0)
      n
    }
    else {
      val cur = namesAndIndices(n)
      namesAndIndices(n) += 1
      n + (cur + 1)
    }
  }
}
