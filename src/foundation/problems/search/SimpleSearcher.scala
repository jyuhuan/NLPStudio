package foundation.problems.search

import foundation.exceptions.GoalNotFoundException

import scala.collection.mutable
import foundation.sugar.OrderedCollection
import foundation.sugar.OrderedCollectionClasses._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/25/15.
 */

case class SimpleSearchNode[S](state: S, parent: SimpleSearchNode[S])

object SimpleSearcher {

  def depthFirstSearch[S](startState: S,
                               isGoal: S ⇒ Boolean,
                               generateSuccessors: S ⇒ Iterable[S]): S = {
    basicGraphSearch(startState, isGoal, generateSuccessors, new mutable.Stack[SimpleSearchNode[S]]())
  }

  def breadthFirstSearch[S](startState: S,
                                 isGoal: S ⇒ Boolean,
                                 generateSuccessors: S ⇒ Iterable[S]): S = {
    basicGraphSearch(startState, isGoal, generateSuccessors, new mutable.Queue[SimpleSearchNode[S]]())
  }

  private def basicGraphSearch[S](startState: S,
                                       isGoal: S ⇒ Boolean,
                                       generateSuccessors: S ⇒ Iterable[S],
                                       fringe: OrderedCollection[SimpleSearchNode[S]]): S = {
    val startNode = SimpleSearchNode[S](startState, null)
    fringe.enqueue(startNode)
    val explored = mutable.HashSet[S]()
    var goalSearchNode: SimpleSearchNode[S] = null
    while (goalSearchNode == null && fringe.notEmpty()) {
      val top = fringe.dequeue()
      explored.add(top.state)
      if (isGoal(top.state)) {
        goalSearchNode = top
      }
      fringe.enqueueAll(generateSuccessors(top.state).filter(s ⇒ !explored.contains(s)).map(x => SimpleSearchNode(x, top)))
    }
    if (goalSearchNode != null) goalSearchNode.state
    else throw new GoalNotFoundException("The search fails to find the goal. ")
  }
}
