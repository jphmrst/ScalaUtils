package org.maraist.search.graph

import java.util.Comparator

/**
 * Specialization of the generic {@link GraphSearcher} to use a queue
 * for its frontier, and thus erform breadth-first search.
 *
 * @tparam State Type representing elements of the search space.
 *
 * @tparam Node Type representing nodes in the search tree.  Each
 * node typically contains a reference to a State element.
 *
 * @param goalCheckerFactory The
 * {@link java.util.function.Supplier#get get} method of this object must
 * return a predicate on tree nodes used to tell if they are goal
 * nodes.
 *
 * @param exploredSetFactory Structure used to manage adding
 * elements to the frontier, in particular for avoiing duplication.
 *
 * @param initializer Creates an initial tree node from a search
 * space element.
 *
 */
class BreadthFirstSearcher[State, Node <: SearchTreeNode[Node,State]](
  goalCheckerFactory: () => GoalChecker[Node],
  exploredSetFactory: Frontier.Queue[Node] => ExploredSet[Node],
  initializer: State => Node
)
extends GraphSearcher[State, Node, Frontier.Queue[Node]](
  goalCheckerFactory, Frontier.queueFactory[Node],
  exploredSetFactory, initializer
) {

  /**
   *  Constructor which defaults to a {@link java.util.HashSet
   *  HashSet} on state elements for detecting previously-explored
   *  nodes.
   *
   * @param goalCheckerFactory The
   * {@link java.util.function.Supplier#get get} method of this object must
   * return a predicate on tree nodes used to tell if they are goal
   * nodes.
   *
   * @param initializer Creates an initial tree node from a search
   * space element.
   */
  def this(goalCheckerFactory: () => GoalChecker[Node],
           init: State => Node) =
    this(goalCheckerFactory,
         ExploredSet.trackStateByHashSet[Frontier.Queue[Node], State, Node],
         init)

  /**
   *  Constructor which uses a
   *  {@linkplain java.util.function.Predicate predicate}
   *  on goals for checking
   *  success of a tree node, and which defaults to a
   *  {@link java.util.HashSet HashSet} on state elements for detecting
   *  previously-explored nodes.
   *
   * @param stateChecker Success predicate on state elements.
   *
   * @param initializer Creates an initial tree node from a search
   * space element.
   */
  def this(stateChecker: State => Boolean,
           initializer: State => Node) =
    this(GoalChecker.goalCheckerFactory[State, Node](stateChecker),
         initializer)
}

object BreadthFirstSearcher {
  def build[S](
    stateChecker: S => Boolean, expander: S => Iterable[S]
  ): BreadthFirstSearcher[S, Nodes.SimpleTreeNode[S]] =
    new BreadthFirstSearcher[S, Nodes.SimpleTreeNode[S]](
      GoalChecker.goalCheckerFactory[S, Nodes.SimpleTreeNode[S]](stateChecker),
      Nodes.SimpleTreeNode.initializer[S](expander)
    )
}
