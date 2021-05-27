// Copyright (C) 2017 John Maraist
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.graphviz

trait TransitionLabeling[-T] {
  def getLabel(t:T):String
}
object TransitionLabeling {
  given TransitionLabeling[Any] with
    def getLabel(s:Any):String = s.toString()
}
