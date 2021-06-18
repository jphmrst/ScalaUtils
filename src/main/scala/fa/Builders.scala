// Copyright (C) 2021 John Maraist
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.fa
import scala.collection.mutable.{Builder, HashMap, HashSet}
import org.maraist.fa.DFA.IndexedDFA
import org.maraist.fa.impl.{HashDFABuilder}

/**
  * @group General
  */
object Builders {
  case class AddState[S,T](state: S)
  case class RemoveState[S,T](state: S)
  case class RemoveFinalState[S,T](state: S)
  case class RemoveTransition[S,T](state1: S, trans: T, state2: S)
  type AnyBuilders[S,T] =
    AddState[S,T] | RemoveState[S,T] | RemoveFinalState[S,T] | RemoveTransition[S,T]

  case class AddFinalState[S,T](state: S)
  case class AddTransition[S,T](state1: S, trans: T, state2: S)
  type NonProbBuilders[S,T] = AddFinalState[S,T] | AddTransition[S,T]

  case class SetInitialState[S](state: S)
  type SingleInitialStateBuilders[S] = SetInitialState[S]

  case class AddInitialState[S](state: S)
  case class RemoveInitialState[S](state: S)
  type MultipleInitialStateBuilders[S] = AddInitialState[S] | RemoveInitialState[S]

  type DFAelements[S, T] =
    SingleInitialStateBuilders[S] | NonProbBuilders[S,T] | AnyBuilders[S,T]

  trait HasBuilder[Setter[_], Mapper[_,_], Elements[_,_], Res[_,_]] {
    def build[S,T](): Builder[Elements[S,T], Res[S,T]]
  }
  trait HasBuilderWithInit[Setter[_], Mapper[_,_], Elements[_,_], Res[_,_]] {
    def build[S,T](init: S): Builder[Elements[S,T], Res[S,T]]
  }

  given HasBuilderWithInit[HashSet, HashMap, DFAelements, DFA] with {
    override def build[S,T](init: S): Builder[DFAelements[S, T], DFA[S, T]] =
        new HashDFABuilder[S, T](init)
  }
}
