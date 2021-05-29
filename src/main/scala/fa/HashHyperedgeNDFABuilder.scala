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
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

class HashHyperedgeNDFABuilder[S,T]
extends AbstractHashNDFABuilder[S,T,ArrayHyperedgeDFA[Set[S],T],
                                ArrayHyperedgeNDFA[S,T]]
with HyperedgeNDFABuilder[S,T,ArrayHyperedgeDFA[Set[S],T],
                          ArrayHyperedgeNDFA[S,T]]
{
  val hyperedgeMap: HashMap[S,HashSet[Set[S]]] = new HashMap[S,HashSet[Set[S]]]
  def eHyperedgeTargets(s:S): Set[Set[S]] = hyperedgeMap.get(s) match {
    case Some(hashSet) => hashSet.toSet
    case None => Set.empty[Set[S]]
  }
  def addEHyperedge(s:S, ss:Set[S]): Unit = {
    if (!hyperedgeMap.contains(s))  hyperedgeMap += (s -> new HashSet[Set[S]])
    hyperedgeMap(s) += ss
  }
  protected def assembleNDFA(statesSeq:IndexedSeq[S], initials:Set[Int],
                             finals:Set[Int], transitionsSeq: IndexedSeq[T],
                             labelsArray:Array[Array[HashSet[Int]]],
                             epsilonsArray:Array[HashSet[Int]]): ArrayHyperedgeNDFA[S,T] = {
    val hyperedgeIndexMap: HashMap[Int,Set[Set[Int]]] =
      new HashMap[Int,Set[Set[Int]]]
    for((startNode,targetSets) <- hyperedgeMap) {
      val startNodeIndex = statesSeq.indexOf(startNode)
      val thisSet = new HashSet[Set[Int]]
      for(targetSet <- targetSets)
        thisSet += targetSet.map(statesSeq.indexOf(_))
      hyperedgeIndexMap(startNodeIndex) = thisSet.toSet
    }
    new ArrayHyperedgeNDFA[S,T](
      statesSeq, initials, finals, transitionsSeq, labelsArray,
      epsilonsArray.map(_.toSet),
      hyperedgeIndexMap.toMap)
  }
  override def toDFA: ArrayHyperedgeDFA[Set[S],T] = this.toNDFA.toDFA
}
