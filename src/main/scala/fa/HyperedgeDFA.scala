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
import org.maraist.graphviz.{GraphvizOptions,NodeLabeling,TransitionLabeling}

trait HyperedgeDFA[S,T] extends DFA[S,T] with Hyperedge[S] {
  override type Traverser <: HyperedgeDFAtraverser[S,T]

  /** {@inheritDoc} */
  override protected def internalsToDOT(stateList:IndexedSeq[S],
                                        sb:StringBuilder,
                                        nodeLabeling:NodeLabeling[S] =
                                          this.nodeLabeling,
                                        trLabeling:TransitionLabeling[T] =
                                          this.transitionLabeling):Unit = {
    super.internalsToDOT(stateList, sb,nodeLabeling, transitionLabeling)
    // eHyperedgesToDOT(nodeLabeling, stateList, sb)
  }

  override def traverseEdgesFrom(si0:Int, s0:S, stateList:IndexedSeq[S],
                                 theLabels:IndexedSeq[T],
                                 trav:Traverser):Unit = {
    super.traverseEdgesFrom(si0, s0, stateList, theLabels, trav)
    for (targets <- eHyperedgeTargets(s0)) {
      trav.eHyperedge(si0, s0, targets)
    }
  }
}

trait IndexedHyperedgeDFA[S,T] extends IndexedDFA[S,T] with HyperedgeDFA[S,T]
