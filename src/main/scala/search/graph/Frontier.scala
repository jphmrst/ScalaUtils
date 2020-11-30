package org.maraist.search.graph
import java.util.Comparator

/**
 *  Methods required of a representation of a search tree frontier.
 *
 *  The {@link Frontiers} class contains some standard implementations
 *  and utilities of this interface.
 *
 * @param <Node> The type of tree nodes stored in the frontier.
 *
 * @see GraphSearcher#search
 * @see Frontiers
 */
trait Frontier[Node] {

  /**
   *  Adds a (usually newly-generated) tree node to the frontier.
   *
   * @param n The new node
   */
  def add(n: Node): Unit

  /**
   *  Checks whether any tree nodes remain in the frontier
   *
   * @return <tt>false</tt> when the frontier is empty, which
   * generally indicates that the search has failed.
   */
  def isEmpty(): Boolean

  /**
   *  Removes one tree node from the frontier, and returns it.
   *
   * @return The dequeued tree node
   *
   * @throws IllegalStateException when this method is called but the
   * frontier is empty; the exception may contain a cause if the
   * exception was generated by some other data structure.
   */
  def pop(): Node

  /**
   *  Returns the number of open nodes sitting unexpanded in the
   *  frontier.
   *
   * @return The number of open nodes
   */
  def countOpen(): Int

  /**
   *  Print debugging information about the frontier.  By default,
   *  does nothing.
   */
  def debugDisplayFrontier(): Unit = { }

}

object Frontier {
  abstract class DebuggingFrontier[Node] extends Frontier[Node] {
    override def debugDisplayFrontier(): Unit = { }
  }

  class PriorityQueue[Node](val prioritizer: Comparator[Node])
  extends DebuggingFrontier[Node] {
    // Have to use the Java version; the Scala PriorityQueue does not
    // support out-of-priority-order removal from a PriorityQueue.
    protected val queue: java.util.PriorityQueue[Node] =
      new java.util.PriorityQueue[Node](prioritizer)

    override def add(n: Node): Unit = queue.add(n)
    override def isEmpty(): Boolean = queue.isEmpty
    override def pop(): Node = {
      val result: Node = queue.poll()
      if (result == null) { throw new FrontierEmptyException }
      result
    }
    override def countOpen(): Int = queue.size()
    override def debugDisplayFrontier(): Unit =
      super.debugDisplayFrontier()
  }

  def priorityQueueFactory[Node](prioritizer: Comparator[Node])(): PriorityQueue[Node] =
    new PriorityQueue[Node](prioritizer)

  // =================================================================

  class StateKeyedPriorityQueue[S, N <: SearchTreeNode[N,S]](
    prioritizer: Comparator[N]
  )
  extends PriorityQueue[N](prioritizer) {
    import scala.collection.mutable.HashMap
    val bestPath = new HashMap[S, N]
    override def add(node: N): Unit = {
      val state: S = node.state
      bestPath.get(state) match {
        case Some(previousBest) => {
          if (prioritizer.compare(previousBest,node) > 0) {
            queue.remove(previousBest)
            bestPath.put(state, node)
            super.add(node)
          }
        }
        case None => {
          bestPath.put(state, node)
          super.add(node)
        }
      }
    }
  }

  def stateKeyedPriorityQueueFactory[
    State, Node <: SearchTreeNode[Node,State]
  ](prioritizer: Comparator[Node])():
  StateKeyedPriorityQueue[State,Node] =
    new StateKeyedPriorityQueue[State,Node](prioritizer)

  // =================================================================

  class DebuggablePriorityQueue[S, N <: SearchTreeNode[N,S]](
    protected val prioritizer: Comparator[N])
  extends DebuggingFrontier[N] {
    protected val queue = new java.util.PriorityQueue[N](prioritizer)
    import scala.collection.mutable.HashMap
    protected val bestPath: HashMap[S,N] = new HashMap[S,N]()

    override def add(node: N): Unit = {
      val state: S = node.state
      bestPath.get(state) match {
        case Some(previousBest) => {
          if (prioritizer.compare(previousBest,node) > 0) {
            queue.remove(previousBest)
            bestPath.put(state, node)
            queue.add(node)
          }
        }
        case None => {
          bestPath.put(state, node)
          queue.add(node)
        }
      }
    }

    override def isEmpty(): Boolean = queue.isEmpty()

    override def pop(): N = {
      val result: N = queue.poll()
      if (result == null) {
        throw new FrontierEmptyException()
      }
      result
    }

    override def countOpen(): Int = queue.size()
    override def debugDisplayFrontier(): Unit =
      super.debugDisplayFrontier()
  }

  def debuggablePriorityQueueFactory[
    State, Node <: SearchTreeNode[Node,State]
  ](prioritizer: Comparator[Node])(
  ): DebuggablePriorityQueue[State,Node] =
    new DebuggablePriorityQueue[State,Node](prioritizer)

  // =================================================================

  class Queue[Node] extends FrontierCheckingStructure[Node] {
    val queue = new java.util.LinkedList[Node]();

    override def add(n: Node): Unit =queue.offer(n)
    override def isEmpty(): Boolean = queue.isEmpty()
    override def pop(): Node =
      try {
        return queue.remove();
      } catch {
        case (cause: NoSuchElementException) =>
          throw new FrontierEmptyException(cause);
      }
    override def countOpen(): Int = queue.size()
    override def contains(n: Node): Boolean = queue.contains(n)
  }

  def queueFactory[Node](): Queue[Node] = new Queue[Node]()

  // =================================================================

}
