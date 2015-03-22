package nlpstudio.resources.core

/**
 * Created by yuhuan on 3/21/15.
 */
case class Rule(head: String, body: Seq[String]) {
  override def toString = head + " -> " + body.mkString(" ")
}
