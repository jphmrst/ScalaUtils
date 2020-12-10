// Copyright (C) 2020 John Maraist
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.search.local
import scala.collection.immutable.HashMap
import scala.collection.mutable.TreeSet
import scala.util.Random
import org.maraist.search.Searcher
import org.maraist.search.SearchFailureException
import org.maraist.search.Debug.debugOn

class StochasticBeamBuilder[S](
  val generation: Int,
  val stateCompare: Ordering[S],
  val retainProbability: S => Double,
  val beamLength: StochasticBeamBuilder[S] => Int,
  val retainOnOrder: StochasticBeamBuilder[S] => Int,
  val randoms: Random
)
extends BeamBuilder[S, StochasticBeam[S]] {
  def this(sb: StochasticBeam[S]) =
    this(1 + sb.generation, sb.stateCompare, sb.retainProbability,
         sb.beamLength, sb.retainOnOrder, sb.randoms)

  val store = TreeSet[S]()(stateCompare)
  var size: Int = 0
  def add(state: S): Unit = {
    store += state
    size = size + 1
  }
  def length: Int = size
  def toBeam = new StochasticBeam[S](this)
}

class StochasticBeam[S](val generation: Int,
                        val stateCompare: Ordering[S],
                        val retainProbability: S => Double,
                        val beamLength: StochasticBeamBuilder[S] => Int,
                        val retainOnOrder: StochasticBeamBuilder[S] => Int,
                        val randoms: Random,
                        val store: Seq[S])
extends Beam[S] {
  val length: Int = store.size

  def this(src: StochasticBeamBuilder[S]) =
    this(src.generation, src.stateCompare, src.retainProbability,
         src.beamLength, src.retainOnOrder, src.randoms, {
           val storeBuilder = Seq.newBuilder[S]

           val keepTop: Int = src.retainOnOrder(src)
           storeBuilder ++= src.store.takeRight(keepTop)

           val chooseLen: Int = src.size - keepTop
           val others = src.store.take(chooseLen)
           val scores: HashMap[S, Double] = {
             val builder = HashMap.newBuilder[S, Double]
             for (other <- others)
               builder += ((other,
                            src.retainProbability(other)
                             * src.randoms.nextDouble()))
             builder.result()
           }
           val othersSorted = new TreeSet[S]()(new Ordering[S] {
             override def compare(x: S, y: S): Int =
               scores(x).compare(scores(y))
           })
           othersSorted ++= others
           storeBuilder ++= othersSorted.takeRight(src.beamLength(src)
                                                   - keepTop)

           storeBuilder.result().sorted(src.stateCompare)
         })

  override def apply(i: Int): S = store(i)
  override def iterator: Iterator[S] = store.iterator
}

object StochasticBeam {
  import BeamSearcher._

  def extractor[S]: ExtractorFn[S, StochasticBeam[S]] = (b => b.store.last)

  def firstBeam[S](
    stateCompare: Ordering[S],
    retainProbability: S => Double,
    beamLength: StochasticBeamBuilder[S] => Int,
    retainOnOrder: StochasticBeamBuilder[S] => Int,
    randoms: Random
  ): FirstBeamFn[S, StochasticBeam[S]] =
    state =>
      new StochasticBeam[S](0, stateCompare, retainProbability, beamLength,
                            retainOnOrder, randoms, Seq[S](state))

  type StocNextBeamFn[S] = NextBeamFn[S, StochasticBeam[S],
                                      StochasticBeamBuilder[S]]

  def quitAfterGenerations[S](limit: Int): StocNextBeamFn[S] =
    sb => if (sb.generation > limit) None
          else Some(new StochasticBeamBuilder[S](sb))

  def quitAtGoal[S](isGoal: S => Boolean): StocNextBeamFn[S] =
    sb => if (isGoal(sb.store.last)) None
          else Some(new StochasticBeamBuilder[S](sb))
}

class StochasticBeamSearcher[S](
  nextBeam: StochasticBeam[S] => Option[StochasticBeamBuilder[S]],
  successors: S => Iterable[Option[S]],
  stateCompare: Ordering[S],
  retainProbability: S => Double,
  beamLength: StochasticBeamBuilder[S] => Int,
  retainOnOrder: StochasticBeamBuilder[S] => Int,
  randoms: Random
) extends BeamSearcher[S, StochasticBeam[S]](
  StochasticBeam.firstBeam[S](stateCompare, retainProbability, beamLength,
                              retainOnOrder, randoms),
  nextBeam, successors, StochasticBeam.extractor[S]) {
}
