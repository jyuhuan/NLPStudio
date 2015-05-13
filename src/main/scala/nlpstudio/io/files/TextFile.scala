package nlpstudio.io.files

import scala.io.Source
import java.io._
import java.nio.file._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/5/15.
 */
object TextFile {
  def readLines(path: String) = Source.fromFile(path).getLines()
  def writeLines(fn: String, s: TraversableOnce[String]) = {
    val bw = Files.newBufferedWriter(Paths.get(fn))
    for (line ← s) bw.write(line + "\n")
    bw.close()
  }
}
