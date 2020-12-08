// Copyright (C) 2017 John Maraist
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.util

protected trait TrackTree {
  def expanded():Boolean
}
protected case class TrackItem(index:Int,
                               var present:TrackTree, var absent:TrackTree)
     extends TrackTree{
  def hasPresent():Boolean = present.expanded()
  def hasAbsent():Boolean = absent.expanded()
  def expanded():Boolean = true
}
protected case class Present(index: Int) extends TrackTree {
  def expanded():Boolean = true
}
protected case class Absent() extends TrackTree {
  def expanded():Boolean = false
}