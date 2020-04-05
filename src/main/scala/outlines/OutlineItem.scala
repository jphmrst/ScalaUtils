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

class OutlineItem[E](val heading: E, val items: Seq[OutlineItem[E]],
                     val summary: Option[E] = None) {
  def formatAsItem(
    dest: PrintWriter, lead: String, prefix: String = ""
  )(
    implicit controls: OutlineOptions
  ): Unit = {
    if (controls.summarize) {
      summary match {
        case Some(s) => dest.println(lead + prefix + s)
        case None => fullFormatAsItem(dest, lead, prefix)
      }
    } else {
      fullFormatAsItem(dest, lead, prefix)
    }
  }

  def fullFormatAsItem(dest:PrintWriter, lead:String, prefix:String)(
    implicit controls: OutlineOptions
  ): Unit = {
    dest.println(lead + prefix + heading)
    val sublead = "  " + lead
    for(item <- items) {
      item.formatAsItem(dest, sublead)
    }
  }
}

object OutlineItem {
  def apply[E](e: E, items: OutlineItem[E]*): OutlineItem[E]
    = new OutlineItem[E](e, items)
  def item[E](e: E, items: OutlineItem[E]*): OutlineItem[E]
    = new OutlineItem[E](e, items)
  def summarized[E](summary: E, e: E, items: OutlineItem[E]*): OutlineItem[E]
    = new OutlineItem[E](e, items, Some(summary))
  implicit def basicToItem[E](s: E): OutlineItem[E] = new OutlineItem(s,Seq())
  implicit def seqToItem[E](s: Seq[E]): OutlineItem[E]
    = new OutlineItem(s.head,s.tail.map(apply(_)))
}
