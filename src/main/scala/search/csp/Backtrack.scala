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
 *  Topmost class encapsulating backtracking search for a CSP.  The
 *  {#code search} methods implement the Backtrack algorithm of
 *  Russell and Norvig (2nd ed., Figure 6.5, p. 215), with
 *  customizable behavior provided as constructor arguments.
 *
 * @tparam Assignments Implementation of a set of variable-value
 * assignments.
 *
 * @tparam Var Type representing variables in the CSP.
 *
 * @tparam Val Type representing values in the CSP.
 *
 * @tparam Inf Type representing inferences to be applied to an
 * assignment set.  For side-effecting assignment sets, this type
 * may be the same as {@code Assignments}.
 *
 * @tparam CSP Representation of the particular CSP problem we are
 * solving.
 *
 * @param initial Given a CSP problem, returns an initial set of
 * assignments.
 *
 * @param isComplete Predicate on {@code Assignments} which returns
 * {@code true} when all variables of the problem have been assigned.
 *
 * @param selectNextVariable Function on {@code Assignments} which
 * returns the next {@code Variable} which should be given an
 * assignment.
 *
 * @param domainValues Function returning the possible value bindings
 * for an assignment.
 *
 * @param drawInferences Function returning additional restrictions
 * to variable domains arising from a new variable-value binding.
 *
 * @param retractInferences Function removing additional restrictions
 * to variable domains inferred by a {@link #drawInferences} call.
 *
 * @param withInferences Function committing an {@code Assignments
 * instance} to a result of a call to {@link #drawInferences}.
 *
 * @param failure Thunk returning a representation of failure.
 *
 * @return The resulting assignment set.
 */
class Backtrack[
  Assignments <: AssignmentSet[Var, Val, Assignments],
  Var, Val, Inf <: Inferences[Var, Val, Assignments],
  CSP <: Problem[Var, Val]
](
  val initial: CSP => Assignments,
  val isComplete: Assignments => Boolean,
  val selectNextVariable: Assignments => Var,
  val domainValues: (Assignments, Var, CSP) => Iterable[Val],
  val drawInferences: (Assignments, Var) => Inf,
  val retractInferences: (Assignments, Var, Val, Inf) => Unit,
  val withInferences: (Assignments, Inf) => Assignments,
  val failure: () => Assignments
) // extends trait Searcher[Assignments]
{

  def search(csp: CSP): Assignments = {
    search(csp, initial(csp))
  }

  def search(csp: CSP, given: Assignments): Assignments = {
    if (isComplete(given)) {
      return given
    }

    val variable: Var = selectNextVariable(given)
    for(value <- domainValues(given, variable, csp)) {
      if (given.isConsistent(variable, value)) {
        val added = given.add(variable, value)

        val inferences = drawInferences(added, variable)
        if (!inferences.isFailure) {
          val next = withInferences(added, inferences)
          val result = search(csp, next)
          if (!result.isFailure) {
            return result
          }
        }

        retractInferences(added, variable, value, inferences)
      }

      given.remove(variable, value)
    }
    return failure()
  }

}

trait AssignmentSet[Var, Val, AS <: AssignmentSet[Var, Val, AS]] {
  def isFailure: Boolean
  def isConsistent(variable: Var, value: Val): Boolean
  def add(variable: Var, value: Val): AS
  def remove(variable: Var, value: Val): Unit
}

trait Problem[Var, Val] {
}

