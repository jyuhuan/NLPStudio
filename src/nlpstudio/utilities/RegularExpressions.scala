package nlpstudio.utilities

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/5/15.
 */
object RegularExpressions {
  def whitespaces = """[ \t]+""" r
  def leftParenthesis = """\(""" r
  def rightParenthesis = """\)""" r
  def allBrackets = """(\()|(\))""" r
}
