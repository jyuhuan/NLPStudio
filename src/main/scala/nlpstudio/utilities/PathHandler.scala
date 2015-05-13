package nlpstudio.utilities

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/11/15.
 */
object PathHandler {
  def concat(partA: String, partB: String): String = {
    if (partA.endsWith("/")) {
      partA + partB
    }
    else {
      partA + "/" + partB
    }
  }
}
