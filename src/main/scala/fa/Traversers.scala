// Copyright (C) 2017, 2021 John Maraist
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.fa

/**
 * Methods for traversing the structure of a
 * {@link org.maraist.fa.HyperedgeDFA HyperedgeDFA}. Use with the
 * {@link org.maraist.fa.HyperedgeDFA#traverse HyperedgeDFA.traverse} method.
 * By default, all methods are empty.
 *
 *  @tparam S The type of all states of the automaton
 *  @tparam T The type of labels on transitions of the automaton
 *
 * @group DFA
 */
trait HyperedgeDFAtraverser[S,-T] extends DFA.DFAtraverser[S,T] {
  def eHyperedge(sourceIndex:Int, source:S, targets:Set[S]): Unit
}
