package nlpstudio.utilities

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/5/15.
 */
object RegularExpressions {
  val whitespaces = """[ \t]+""" r
  val leftParenthesis = """\(""" r
  val rightParenthesis = """\)""" r
  val allBrackets = """(\()|(\))""" r

  val betweenTags = """>[^<>]+</""".r
}
