package foundation.problems.search

import foundation.exceptions.GoalNotFoundException

import scala.collection.mutable
import foundation.sugar.OrderedCollection
import foundation.sugar.OrderedCollectionClasses._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/25/15.
 */

case class SimpleSearchNode[TState](state: TState, parent: SimpleSearchNode[TState])

object SimpleSearcher {

  def depthFirstSearch[TState](startState: TState, isGoal: TState ⇒ Boolean, generateSuccessors: TState ⇒ Iterable[TState]): TState = {
    basicGraphSearch(startState, isGoal, generateSuccessors, new mutable.Stack[SimpleSearchNode[TState]]())
  }

  def breadthFirstSearch[TState](startState: TState, isGoal: TState ⇒ Boolean, generateSuccessors: TState ⇒ Iterable[TState]): TState = {
    basicGraphSearch(startState, isGoal, generateSuccessors, new mutable.Queue[SimpleSearchNode[TState]]())
  }

  private def basicGraphSearch[TState](startState: TState,
                                       isGoal: TState ⇒ Boolean,
                                       generateSuccessors: TState ⇒ Iterable[TState],
                                       fringe: OrderedCollection[SimpleSearchNode[TState]]): TState = {
    val startNode = SimpleSearchNode[TState](startState, null)
    fringe.enqueue(startNode)
    val explored = mutable.HashSet[TState]()
    var goalSearchNode: SimpleSearchNode[TState] = null
    while (goalSearchNode == null && fringe.notEmpty()) {
      val top = fringe.dequeue()
      explored.add(top.state)
      if (isGoal(top.state)) {
        goalSearchNode = top
      }
      fringe.enqueueAll(generateSuccessors(top.state).filter(s ⇒ !explored.contains(s)).map(x => SimpleSearchNode(x, top)))
    }
    if (goalSearchNode != null) {
      goalSearchNode.state
    }
    else {
      throw new GoalNotFoundException("The search fails to find the goal. ")
    }
  }
}
