package foundation.problems.search

import scala.collection.mutable
import foundation.sugar.OrderedCollection
import foundation.sugar.OrderedCollectionClasses._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/10/15.
 */

case class SearchNode[TState, TAction](state: TState, parent: SearchNode[TState, TAction], action: TAction) {
  override def toString() = state.toString + ": " + action.toString
}

object Searcher {

  def depthFirstSearch[TState, TAction](startState: TState, isGoal: TState ⇒ Boolean, generateSuccessors: SearchNode[TState, TAction] ⇒ Iterable[SearchNode[TState, TAction]], defaultAction: TAction): Iterable[SearchNode[TState, TAction]] = {
    basicGraphSearch(startState, isGoal, generateSuccessors, defaultAction: TAction, new mutable.Stack[SearchNode[TState, TAction]]())
  }

  def breadthFirstSearch[TState, TAction](startState: TState, isGoal: TState ⇒ Boolean, generateSuccessors: SearchNode[TState, TAction] ⇒ Iterable[SearchNode[TState, TAction]], defaultAction: TAction): Iterable[SearchNode[TState, TAction]] = {
    basicGraphSearch(startState, isGoal, generateSuccessors, defaultAction: TAction, new mutable.Queue[SearchNode[TState, TAction]]())
  }

  private def basicGraphSearch[TState, TAction](startState: TState, isGoal: TState ⇒ Boolean, generateSuccessors: SearchNode[TState, TAction] ⇒ Iterable[SearchNode[TState, TAction]], defaultAction: TAction, fringe: OrderedCollection[SearchNode[TState, TAction]]): Iterable[SearchNode[TState, TAction]] = {
    val startNode = SearchNode[TState, TAction](startState, null, defaultAction)
    fringe.enqueue(startNode)
    val explored = mutable.HashSet[TState]()
    var goalSearchNode: SearchNode[TState, TAction] = null
    while (goalSearchNode == null && fringe.notEmpty()) {
      val top = fringe.dequeue()
      explored.add(top.state)
      if (isGoal(top.state)) {
        goalSearchNode = top
      }
      fringe.enqueueAll(generateSuccessors(top).filter(s ⇒ !explored.contains(s.state)))
    }
    if (goalSearchNode != null) {
      val result = mutable.ArrayBuffer[SearchNode[TState, TAction]]()
      var curNode = goalSearchNode
      while (curNode.parent != null) {
        result += curNode
        curNode = curNode.parent
      }
      result += startNode
      result.reverse
    }
    else {
      Iterable[SearchNode[TState, TAction]]()
    }
  }
}
