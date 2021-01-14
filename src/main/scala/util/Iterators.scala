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

object Iterators {

  /**
    * Transform several Iterators into a single Iterator.
    */
  def onlySome[A](iter: Iterator[Option[A]]): Iterator[A] =
    new Iterator[A] {

      private var nextA: Option[A] = findActual()

      private def findActual(): Option[A] = {
        while (iter.hasNext) {
          iter.next() match {
            case opt@Some(_) => return opt
            case None => { }
          }
        }
        return None
      }

      override def hasNext: Boolean = !nextA.isEmpty

      override def next(): A = nextA match {
        case None => throw new NoSuchElementException
        case Some(a) => {
          nextA = findActual()
          a
        }
      }
    }

  def sequenceIterators[A](iters: Iterator[Iterator[A]]): Iterator[A] =
    new MultiIterator[A](iters)

  def sequenceIterators[A](iters: Iterable[Iterator[A]]): Iterator[A] =
    new MultiIterator[A](iters.iterator)

  def iterateIterables[A](iters: Iterator[Iterable[A]]): Iterator[A] =
    new MultiIterator(iters.map(_.iterator))

  def sequenceIterables[A](iters: Iterable[Iterable[A]]): Iterable[A] =
    new Iterable[A] {
      override def iterator: Iterator[A] =
        new MultiIterator[A]((for (i <- iters) yield i.iterator).iterator)
    }

  // -----------------------------------------------------------------
  // Private classes implementing the above methods.

  private class MultiIterator[A](
    private val metaIterator: Iterator[Iterator[A]]
  )
  extends Iterator[A] {

    private var currentIterator = firstNonempty()
    private var nextA: Option[A] = updateNextA()

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

    /** {@inheritDocs} */
    override def hasNext: Boolean = !nextA.isEmpty

    /** {@inheritDocs} */
    override def next(): A = nextA match {
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
}
