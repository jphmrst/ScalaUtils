package org.maraist.search.graph

import java.util.Comparator

/**
 *  Extension of {@link AStarFrontierSearcher} to fix the frontier
 *  structure with a minimal priority queue implementation.
 *
 * @tparam State Type representing elements of the search space.
 *
 * @tparam Node Type representing nodes in the search tree.  Each
 * node typically contains a reference to a State element.
 *
 * @param goalTest A boolean-returning function checking whether a
 * tree node contains a goal state.
 *
 * @param heuristic Heuristic function for this search application.
 *
 * @param exploredSetFactory Structure used to manage adding
 * elements to the frontier, in particular for avoiing duplication.
 * Passed as-is to the primary constructor for this class, and
 * thence to the {@linkplain GraphSearcher parent} constructor.
 *
 * @param initializer Creates an initial tree node from a search
 * space element.  Passed as-is to the primary constructor for this
 * class, and thence to the {@linkplain GraphSearcher parent}
 * constructor.
 */
class AStarSearcher[
  State, Node <: SearchTreeNode[Node,State] with KnowsOwnCost
](
  goalTest: GoalChecker[Node],
  heuristic: (Node) => Double,
  exploredSetFactory: (Frontier.PriorityQueue[Node]) => ExploredSet[Node],
  initializer: (State) => Node)
extends AStarFrontierSearcher[State, Node, Frontier.PriorityQueue[Node]](
  goalTest,
  heuristic,
  (cmp) => Frontier.priorityQueueFactory(cmp),
  exploredSetFactory,
  initializer
) {

//  /**
//   * Constructor for this class which does not maintain an explored
//   * set.
//   *
//   * @param goalTest A boolean-returning function checking whether a
//   * tree node contains a goal state.
//   *
//   * @param heuristic Heuristic function for this search application.
//   *
//   * @param initializer Creates an initial tree node from a search
//   * space element.  Passed as-is to the primary constructor for this
//   * class, and thence to superclasses.
//   */
//  def this(goalTest: GoalChecker[Node],
//           heuristic: (Node) => Double,
//           initializer: (State) => Node) =
//             this(goalTest, heuristic,
//                  (cmp: Comparator[Node]) =>
//                    () =>
//                      new Frontier.PriorityQueue[Node](cmp),
//                  initializer)
}

// object AStarSearcher {
//
//   /**
//    *  A specialization of {@link AStarSearcher} to use a minimal
//    *  implementation of unrelated search tree nodes (with a state and
//    *  accumulated cost only), with the frontier implementation still
//    *  exposed as a type parameter.
//    *
//    * @tparam State Type representing elements of the search space.
//    *
//    * @param stateTest A boolean-returning function checking whether
//    * a state space element is a goal state.
//    *
//    * @param heuristic Heuristic function for this search application.
//    *
//    * @param hashArtifactBuilder Generates a hashable object from a
//    * state element.
//    *
//    * @param expander Generates the successor states from some state,
//    * each associated with a cost.
//    *
//    */
//   class SimpleNodes<State>(
//     stateTest: Predicate<State>,
//     heuristic: Function<State,Double>,
//     hashArtifactBuilder: Function<State,Object>,
//     expander: Function<State,Iterable<Nodes.CostAndStep<State>>>
//   )
//   extends AStarSearcher<State, Nodes.SimpleTreeCostNode<State>>(
//     GoalCheckers.liftPredicate(stateTest),
//     Nodes.liftHeuristic(heuristic),
//     ExploredSets.trackGeneratedByArtifactHashSet
//     ((n) -> hashArtifactBuilder.apply(n.getState())),
//     Nodes.SimpleTreeCostNode.initializer(expander)
//   ) {
//
//     /**
//      * Constructor for this class which does not maintain an explored
//      * set.
//      *
//      * @param stateTest A boolean-returning function checking whether
//      * a state space element is a goal state.
//      *
//      * @param heuristic Heuristic function for this search application.
//      *
//      * @param expander Generates the successor states from some state,
//      * each associated with a cost.
//      */
//     def this(
//       stateTest: Predicate<State>,
//       heuristic: Function<State,Double>,
//       expander: Function<State,Iterable<Nodes.CostAndStep<State>>>
//     ) =
//       this(GoalCheckers.liftPredicate(stateTest),
//            Nodes.liftHeuristic(heuristic),
//            Nodes.SimpleTreeCostNode.initializer(expander))
//   }
//
//   /**
//    *  A specialization of {@link AStarSearcher} to use a minimal
//    *  implementation of hierarchical search tree nodes (with a state,
//    *  accumulated cost, and pointer to a parent tree node), with the
//    *  frontier implementation still exposed as a type parameter.
//    *
//    * @tparam State Type representing elements of the search space.
//    */
//   class PathNodes<State>
//   extends AStarSearcher<State, Nodes.SimpleTreePathCostNode<State>> {
//     /**
//      * Constructor for this class which does not maintain an explored
//      * set.
//      *
//      * @param stateTest A boolean-returning function checking whether
//      * a state space element is a goal state.
//      *
//      * @param heuristic Heuristic function for this search application.
//      *
//      * @param expander Generates the successor states from some state,
//      * each associated with a cost.
//      */
//     public PathNodes
//         (stateTest: Predicate<State>,
//          heuristic: Function<State,Double>,
//          expander: Function<State,Iterable<Nodes.CostAndStep<State>>>) {
//       super(GoalCheckers.liftPredicate(stateTest),
//             Nodes.liftHeuristic(heuristic),
//             Nodes.SimpleTreePathCostNode.initializer(expander));
//     }
//
//     /**
//      * Constructor for this class which maintains an explored
//      * set using a hashing of the state representations.
//      *
//      * @param stateTest A boolean-returning function checking whether
//      * a state space element is a goal state.
//      *
//      * @param heuristic Heuristic function for this search application.
//      *
//      * @param hashArtifactBuilder Generates a hashable object from a
//      * state element.
//      *
//      * @param expander Generates the successor states from some state,
//      * each associated with a cost.
//      */
//     public PathNodes
//         (stateTest: Predicate<State>,
//          heuristic: Function<State,Double>,
//          hashArtifactBuilder: Function<State,Object>,
//          expander: Function<State,Iterable<Nodes.CostAndStep<State>>>) {
//       super(GoalCheckers.liftPredicate(stateTest),
//             Nodes.liftHeuristic(heuristic),
//             ExploredSets.trackGeneratedByArtifactHashSet
//                 ((n) -> hashArtifactBuilder.apply(n.getState())),
//             Nodes.SimpleTreePathCostNode.initializer(expander));
//     }
//   }
// }
//
