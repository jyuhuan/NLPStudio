package nlpstudio.utilities

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 4/9/15.
 */
object StringUtilities {
  def isDigit(c: Char) = Character.isDigit(c)
  def areAllDigits(s: String) = s.forall(c ⇒ Character.isDigit(c))
}
