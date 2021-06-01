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
import scala.collection.mutable.Builder
import org.maraist.fa.Builders.DFAelements

/** Builders for deterministic finite automata (DFAs)
  * @tparam S The type of all states of the automaton
  * @tparam T The type of labels on (non-epsilon) transitions of the automaton
  */
trait DFABuilder[S,T]
    extends DFA[S,T]
    with DFABuilderWriter[S,T]
    with Builder[DFAelements[S,T], DFA[S,T]]{
  /** Returns the (possibly immutable) [[org.maraist.fa.DFA DFA]]
    * described to this builder */
  type ThisDFA <: DFA[S,T]

  def result(): ThisDFA

  /** @deprecated Use {@link #result} */
  def toDFA: ThisDFA
}
