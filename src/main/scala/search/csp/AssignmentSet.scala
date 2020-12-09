// Copyright (C) 2020 John Maraist
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.search.csp
import org.maraist.search.Searcher
import org.maraist.search.SearchFailureException

/**
 * A set of (possibly partial) variable-value bindings associated
 * with a particular CSP solution.
 *
 * @tparam Var Type representing variables in the CSP.
 *
 * @tparam Val Type representing values in the CSP.
 *
 * @tparam AS Actual implementation type for this trait.
 */
trait AssignmentSet[Var, Val, AS <: AssignmentSet[Var, Val, AS]] {

  /**
   *  Returns {@code true} if this set corresponds to search failure.
   */
  def isFailure: Boolean

  /**
   * Returns {@code true} if the given variable-value assignment
   * would be consistent.
   */
  def isConsistent(variable: Var, value: Val): Boolean

  /**
   *  Returns the {@code AssignmentSet} obtained by making the given
   *  variable-value binding to this set.
   */
  def add(variable: Var, value: Val): AS

  /**
   *  Returns the {@code AssignmentSet} obtained by removing the given
   *  variable-value binding from this set.
   */
  def remove(variable: Var, value: Val): Unit
}
