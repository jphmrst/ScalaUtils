// Copyright (C) 2020 John Maraist
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied, for NON-COMMERCIAL use.  See the License for the specific
// language governing permissions and limitations under the License.

package org.maraist.outlines
import scala.language.implicitConversions
import java.io.PrintWriter
import org.maraist.latex.{LaTeXdoc,LaTeXRenderable}

class Outline[E] (val items: Seq[OutlineItem[E]],
                  val summary: Option[E] = None)
extends LaTeXRenderable {
  def formatAsItemTop(dest: PrintWriter, lead: String,
                      singular: String, plural: String)(
    implicit controls: OutlineOptions
  ): Unit = {
    items.size match {
      case 0 => { }
      case 1 => {
        items(0).formatAsItem(dest, lead, singular + ": ")
      }
      case n => {
        dest.println(lead + plural + ": ")
        val sublead = "  " + lead
        for(item <- items) {
          item.formatAsItem(dest, sublead)
        }
      }
    }
  }

  def formatAsItems(dest: PrintWriter, lead: String)(
    implicit controls: OutlineOptions
  ): Unit = {
    for(item <- items)  item.formatAsItem(dest, lead)
  }

  def size: Int = items.size

  override def toLaTeX(doc:LaTeXdoc): Unit = items.size match {
    case 0 => { }
    case 1 => { items(0).toLaTeX(doc) }
    case n => {
      doc ++= "\\begin{itemize}\n"
      for(item <- items) {
        doc ++= "\\item "
        item.toLaTeX(doc)
      }
      doc ++= "\\end{itemize}\n"
    }
  }

}

object Outline {
  def apply[E](items: OutlineItem[E]*): Outline[E] = new Outline[E](items)
  implicit def itemToOutline[E](s: OutlineItem[E]): Outline[E] = Outline(s)
  implicit def elemToOutline[E](e: E): Outline[E] = Outline(OutlineItem(e))
  implicit def elemsToOutline[E](es: Seq[E]): Outline[E]
    = new Outline(es.map(OutlineItem(_)))
}

object OutlineWithSummary {
  def apply[E](summary: E, items: OutlineItem[E]*): Outline[E] =
    new Outline[E](items, Some(summary))
}
