package tests

import foundation.math.graph.{Node, Tree}

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/10/15.
 */
object NaryTreeTest {
  def main(args: Array[String]) {
    val n1 = Node("n1")
    val n2 = Node("n2")
    val n3 = Node("n3")
    val n4 = Node("n4")
    val n5 = Node("n5")
    val n6 = Node("n6")
    val n7 = Node("n7")
    val n8 = Node("n8")
    val n9 = Node("n9")
    val n10 = Node("n10")
    val l1 = Node("l1")
    val l2 = Node("l2")
    val l3 = Node("l3")
    val l4 = Node("l4")
    val l5 = Node("l5")
    val l6 = Node("l6")

    n1 --> n2
    n1 --> n3
    n2 --> n4
    n2 --> n5
    n3 --> n6
    n3 --> n7
    n3 --> n8
    n4 --> l1
    n5 --> n9
    n5 --> n10
    n6 --> l2
    n7 --> l3
    n8 --> l4
    n9 --> l5
    n10 --> l6

    val tree = new Tree(n1)
    val allNodes = tree.traverse()
    val allLeaves = tree.leaves()
    val breakpoint = 1
  }
}
