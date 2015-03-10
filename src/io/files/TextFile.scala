package io.files

import scala.io.Source

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/5/15.
 */
object TextFile {
  def readLines(path: String) = Source.fromFile(path).getLines()
  //def writeLines(path: String, lines: Iterator[String]) = { }
}
