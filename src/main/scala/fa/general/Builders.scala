// Copyright (C) 2021 John Maraist
// See the LICENSE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.fa.general
import scala.collection.mutable.{Builder, HashMap, HashSet}
import org.maraist.fa.DFA.IndexedDFA

/**
  * @group General
  */
object Builders {
  trait HasBuilder[Setter[_], Mapper[_,_], Elements[_,_], Res[_,_]] {
    def build[S,T](): Builder[Elements[S,T], Res[S,T]]
  }
  trait HasBuilderWithInit[Setter[_], Mapper[_,_], Elements[_,_], Res[_,_]] {
    def build[S,T](init: S): Builder[Elements[S,T], Res[S,T]]
  }
}
