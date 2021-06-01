// Copyright (C) 2021 John Maraist
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.fa
import scala.collection.mutable.{Builder, HashMap, HashSet}

sealed trait AnyBuilders[S,T]
case class AddState[S,T](state: S) extends AnyBuilders[S,T]
case class RemoveState[S,T](state: S) extends AnyBuilders[S,T]
case class RemoveFinalState[S,T](state: S) extends AnyBuilders[S,T]
case class RemoveTransition[S,T](state1: S, trans: T, state2: S)
    extends AnyBuilders[S,T]

sealed trait NonProbBuilders[S,T]
case class AddFinalState[S,T](state: S) extends AnyBuilders[S,T]
case class AddTransition[S,T](state1: S, trans: T, state2: S)
    extends NonProbBuilders[S,T]

sealed trait SingleInitialStateBuilders[S]
case class SetInitialState[S](state: S) extends SingleInitialStateBuilders[S]

sealed trait MultipleInitialStateBuilders[S]
case class AddInitialState[S](state: S) extends MultipleInitialStateBuilders[S]
case class RemoveInitialState[S](state: S)
    extends MultipleInitialStateBuilders[S]

sealed trait NDFABuilders[S,T]
case class AddETransition[S,T](state1: S, state2: S) extends NDFABuilders[S,T]
case class RemoveETransition[S,T](state1: S, state2: S)
    extends NDFABuilders[S,T]

sealed trait ProbBuilders[S,T]
case class AddProbFinalState[S,T](state: S, prob: Double)
    extends ProbBuilders[S,T]
case class AddProbTransition[S,T](state1: S, trans: T, state2: S, prob: Double)
    extends ProbBuilders[S,T]
case class AddProbETransition[S,T](state1: S, state2: S, prob: Double)
    extends ProbBuilders[S,T]
case class RemoveProbETransition[S,T](state1: S, state2: S, prob: Double)
    extends ProbBuilders[S,T]

object Builders {
  type DFAelements[S, T] =
    SingleInitialStateBuilders[S] & NonProbBuilders[S,T] & AnyBuilders[S,T]
  type NDFAelements[S, T] =
    MultipleInitialStateBuilders[S] & NDFABuilders[S,T] & NonProbBuilders[S,T]
     & AnyBuilders[S,T]
  type PFAelements[S, T] =
    SingleInitialStateBuilders[S] & ProbBuilders[S,T] & AnyBuilders[S,T]

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

  given HasBuilder[
    HashSet, HashMap, NDFAelements, [X,Y] =>> NDFA[X, Y, IndexedDFA[Set[X], Y]]
  ] with {
    override def build[S,T]():
      Builder[NDFAelements[S, T], NDFA[S, T, IndexedDFA[Set[S], T]]] =
        new HashNDFABuilder[S, T]
  }

  given HasBuilder[HashSet, HashMap, PFAelements, PFA] with {
    override def build[S,T](): Builder[PFAelements[S, T], PFA[S, T]] =
        new HashPFABuilder[S, T]
  }
}
