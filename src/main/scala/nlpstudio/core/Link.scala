package nlpstudio.core

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
trait Link[X, Y] {
  def name: String
  def x: X
  def y: Y
}
