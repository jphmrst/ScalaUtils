// Copyright (C) 2021 John Maraist
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.util

class MutiIterator[A](val iters: Seq[Iterator[A]]) extends Iterator[A] {

  val metaIterator = iters.iterator
  var currentIterator = firstNonempty()
  var nextA: Option[A] = updateNextA()

  private def firstNonempty(): Iterator[A] = {
    while (metaIterator.hasNext) {
      val cand = metaIterator.next()
      if (cand.hasNext) { return cand }
    }
    return Iterator.empty[A]
  }
  private def updateNextA(): Option[A] = currentIterator.hasNext match {
    case true => Some(currentIterator.next())
    case false => None
  }

  def hasNext: Boolean = !nextA.isEmpty
  def next(): A = nextA match {
    case None => throw new NoSuchElementException
    case Some(a) => {
      if (!currentIterator.hasNext) {
        currentIterator = firstNonempty()
      }
      nextA = updateNextA()
      a
    }
  }
}
