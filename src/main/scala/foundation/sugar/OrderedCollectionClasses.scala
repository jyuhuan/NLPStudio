package foundation.sugar

import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 3/10/15.
 */
trait OrderedCollection[TItem] {
  def enqueue(item: TItem): Unit
  def dequeue(): TItem
  def enqueueAll(items: Traversable[TItem])
  def notEmpty(): Boolean
}

object OrderedCollectionClasses {
  implicit def StackIsOrderedCollection[T](stack: mutable.Stack[T]): OrderedCollection[T] = new OrderedCollection[T] {
    override def enqueue(item: T) = stack.push(item)
    override def dequeue() = stack.pop()
    override def enqueueAll(items: Traversable[T]) = stack.pushAll(items)
    override def notEmpty() = stack.nonEmpty
  }
  implicit def QueueIsOrderedCollection[T](queue: mutable.Queue[T]): OrderedCollection[T] = new OrderedCollection[T] {
    override def enqueue(item: T) = queue.enqueue(item)
    override def dequeue() = queue.dequeue()
    override def enqueueAll(items: Traversable[T]) = queue ++= items
    override def notEmpty() = queue.nonEmpty
  }
  implicit def PriorityQueueIsOrderedCollection[T](heap: mutable.PriorityQueue[T]): OrderedCollection[T] = new OrderedCollection[T] {
    override def enqueue(item: T) = heap.enqueue(item)
    override def dequeue() = heap.dequeue()
    override def enqueueAll(items: Traversable[T]) = heap ++= items
    override def notEmpty() = heap.nonEmpty
  }
}
