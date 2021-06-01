// Copyright (C) 2017, 2021 John Maraist
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.fa

/** Mutation methods for building nondeterministic finite automata (NDFAs).
  * This trait ''excludes'' DFA/NDFA export for the sake of a simple type
  * API.
  * @tparam S The type of all states of the automaton
  * @tparam T The type of labels on (non-epsilon) transitions of the automaton
  */
trait NDFABuilderWriter[S,T] {
  /** Adds a state to the automaton */
  def addState(s:S):Unit
  /** Removes a state from the automaton */
  def removeState(s:S):Unit
  /** Adds an initial state to the automaton */
  def addInitialState(s:S):Unit
  /** Causes a state not to be considered an initial state, but does
    * ''not'' remove it from the automaton */
  def removeInitialState(s:S):Unit
  /** Adds a final state to the automaton */
  def addFinalState(s:S):Unit
  /** Causes a state not to be considered a final state, but does
    * ''not'' remove it from the automaton */
  def removeFinalState(s:S):Unit

  /** Adds a transition labelled `t` from `s1` to `s2` */
  def addTransition(s1:S, t:T, s2:S): Unit
  /** Adds an &epsilon;-transition from `s1` to `s2` */
  def addETransition(s1:S, s2:S): Unit
  /** Removes any transition labelled `t` from `s1` to `s2` */
  def removeTransition(s1:S, t:T, s2:S): Unit
  /** Removes any &epsilon;-transition from `s1` to `s2` */
  def removeETransition(s1:S, s2:S): Unit

  /** Dispatch steps for a Builder-pattern implementation.  */
  def dispatchBuilder(builder: NDFABuilders[S,T] & MultipleInitialStateBuilders[S] & NonProbBuilders[S,T] & AnyBuilders[S,T]): Unit = builder match {
    case AddState(s) => addState(s)
    case RemoveState(state) => removeState(state)
    case AddFinalState(state) => addFinalState(state)
    case RemoveFinalState(state) => removeFinalState(state)
    case AddTransition(state1, trans, state2) =>
      addTransition(state1, trans, state2)
    case RemoveTransition(state1, trans, state2) =>
      removeTransition(state1, trans, state2)
    case AddInitialState(state) => addInitialState(state)
    case RemoveInitialState(state) => removeInitialState(state)
    case AddETransition(state1, state2) => addETransition(state1, state2)
    case RemoveETransition(state1, state2) => removeETransition(state1, state2)
  }
}
