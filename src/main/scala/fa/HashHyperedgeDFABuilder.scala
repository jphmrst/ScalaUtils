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

class HashHyperedgeDFABuilder[S,T](initialState: S)
      extends AbstractHashDFABuilder[S,T](initialState)
      with HyperedgeDFA[S,T] with HyperedgeBuilder[S] {
  val hyperedgeMap: HashMap[S,HashSet[Set[S]]] = new HashMap[S,HashSet[Set[S]]]
  def eHyperedgeTargets(s:S): Set[Set[S]] = hyperedgeMap.get(s) match {
    case Some(set) => set.toSet
    case None => Set.empty[Set[S]]
  }
  def addEHyperedge(s:S, ss:Set[S]): Unit = {
    if (!hyperedgeMap.contains(s))  hyperedgeMap += (s -> new HashSet[Set[S]])
    hyperedgeMap(s) += ss
  }

  type Traverser = HyperedgeDFAtraverser[S,T]
  protected def dotTraverser(sb:StringBuilder,stateList:IndexedSeq[S]) =
    new DotTraverseHyperedgeDFA[S,T](graphvizOptions, sb, nodeLabeling,
                                     transitionLabeling, stateList,
                                     initialState)

  type ThisDFA = ArrayHyperedgeDFA[S,T]
  protected def assembleDFA(statesSeq: IndexedSeq[S],
                            initialIdx: Int,
                            finalStateIndices: HashSet[Int],
                            transitionsSeq: IndexedSeq[T],
                            idxLabels: Array[Array[Int]]): ThisDFA = {
    val hyperedgeIndicesMap = new HashMap[Int,HashSet[Set[Int]]]
    for((node,nodeSets) <- hyperedgeMap) {
      val nodeIdx = statesSeq.indexOf(node)
      val thisSet = new HashSet[Set[Int]]
      for(nodeSet <- nodeSets)
        thisSet += nodeSet.map(statesSeq.indexOf(_))
      hyperedgeIndicesMap(nodeIdx) = thisSet
    }
    new ArrayHyperedgeDFA[S,T](
      statesSeq, initialIdx, finalStateIndices.toSet,
      transitionsSeq, idxLabels,
      hyperedgeIndicesMap.map({case (k, v) => (k, v.toSet)}).toMap)
  }
}