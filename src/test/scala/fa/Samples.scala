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
import scala.language.adhocExtensions
import java.io.File
import scala.collection.mutable.HashSet
import org.maraist.util.FilesCleaner
import org.maraist.latex.{LaTeXdoc,Sampler}
import org.scalatest.matchers.*
import org.scalatest.matchers.should.*

/**
 * Sample automata, and printing a guide to them.
 */
object Samples extends App with Sampler {

  /**
   * Return a fresh copy of a sample DFA builder
   */
  def dfa1B: HashDFABuilder[String,Int] = {
    val builder = new HashDFABuilder[String,Int]("A")
    builder.addState("B")
    builder.addState("C")
    builder.addFinalState("D")
    builder.addTransition("A", 1, "B")
    builder.addTransition("A", 2, "C")
    builder.addTransition("B", 3, "D")
    builder
  }

  /**
   * Return a DFA derived from a fresh copy of the builder
   * {@link dfa1B}
   */
  def dfa1: ArrayDFA[String,Int] = {
    dfa1B.toDFA
  }

  /**
   * Return a fresh copy of a sample NDFA builder
   */
  def ndfa2B: HashNDFABuilder[String,Int] = {
    val builder = new HashNDFABuilder[String,Int]()
    builder.addInitialState("A")
    builder.addState("B")
    builder.addState("C")
    builder.addFinalState("D")
    builder.addTransition("A", 1, "B")
    builder.addTransition("A", 2, "C")
    builder.addTransition("B", 3, "D")
    builder.addETransition("C", "A")
    builder
  }

  /**
   * Return an NDFA, derived from a fresh copy of the sample builder
   * {@link ndfa2B}
   */
  def ndfa2: ArrayNDFA[String,Int] = {
    ndfa2B.toNDFA
  }

  /**
   * Return a DFA converted from a fresh copy of the sample NDFA builder
   * {@link ndfa2B}
   */
  def ndfa2dfa: ArrayDFA[Set[String],Int] = {
    ndfa2.toDFA
  }

  /**
   * Return a fresh copy of a sample DFA-with-hyperedge builder
   */
  def hdfa3B: HashHyperedgeDFABuilder[String,Int] = {
    val builder = new HashHyperedgeDFABuilder[String,Int]("A")
    builder.addState("B")
    builder.addState("C")
    builder.addFinalState("D")
    builder.addTransition("A", 1, "B")
    builder.addTransition("A", 2, "C")
    builder.addTransition("B", 3, "D")
    builder.addEHyperedge("C", Set[String]("B", "D"))
    builder
  }

  /**
   * Return a DFA converted from a fresh copy of the sample
   * DFA-with-hyperedge builder {@link hdfa3B}
   */
  def hdfa3: ArrayHyperedgeDFA[String,Int] = {
    hdfa3B.toDFA
  }

  /**
   * Return a fresh copy of a sample NDFA-with-hyperedge builder
   */
  def hndfa4B: HashHyperedgeNDFABuilder[String,Int] = {
    val builder = new HashHyperedgeNDFABuilder[String,Int]
    builder.addInitialState("Z")
    builder.addState("A")
    builder.addState("B")
    builder.addState("C")
    builder.addFinalState("D")
    builder.addState("E")
    builder.addState("F")
    builder.addState("G")
    builder.addState("H")
    builder.addState("I")
    builder.addState("J")
    builder.addState("K")
    builder.addState("L")
    builder.addTransition("Z", 7, "A")
    builder.addTransition("Z", 8, "C")
    builder.addTransition("Z", 9, "E")
    builder.addTransition("A", 1, "B")
    builder.addTransition("C", 2, "D")
    builder.addTransition("E", 3, "F")
    builder.addTransition("G", 4, "J")
    builder.addTransition("H", 5, "K")
    builder.addTransition("I", 6, "L")
    builder.addETransition("A", "C")
    builder.addETransition("C", "E")
    builder.addETransition("H", "I")
    builder.addEHyperedge("C", Set[String]("G", "H"))
    builder
  }

  /**
   * Return a NDFA converted from a fresh copy of the sample
   * NDFA-with-hyperedge builder {@link hndfa4B}
   */
  def hndfa4: ArrayHyperedgeNDFA[String,Int] = {
    hndfa4B.toNDFA
  }

  /**
   * Return a DFA converted from a fresh copy of the sample
   * NDFA-with-hyperedge builder {@link hndfa4B}
   */
  def hndfa4dfa: ArrayHyperedgeDFA[Set[String],Int] = {
    hndfa4.toDFA
  }

  def dlhPfa57:HashPFABuilder[Int,String] = {
    val res = new HashPFABuilder[Int,String]
    res.addInitialState(1, 1.0)
    res.addFinalState(2, 0.4)
    res.addFinalState(3, 0.2)
    res.addFinalState(4, 0.6)

    res.addTransition(1,"a",3, 0.5)
    res.addETransition(1,2, 0.5)

    res.addETransition(2,2, 0.5)
    res.addTransition(2,"a",4, 0.1)

    res.addETransition(3,2, 0.2)
    res.addETransition(3,3, 0.5)
    res.addTransition(3,"a",4, 0.1)

    res.addETransition(4,3, 0.2)
    res.addTransition(4,"a",4, 0.2)

    res
   }

  def dlhPfa57_erem:HashPFABuilder[Int,String] = {
    val res = dlhPfa57
    res.removeEpsilonTransitions
    res
  }

  def addSamples(guide:LaTeXdoc):FilesCleaner = {
    val cleaner = newCleaner()
    section(guide,"Package FA")
    graphable(guide,cleaner,dfa1B,    "dfa1B",    "dfa1B",     "1.75in")
    graphable(guide,cleaner,dfa1,     "dfa1",     "dfa1",      "1.75in")
    graphable(guide,cleaner,ndfa2B,   "ndfa2B",   "ndfa2B",    "1.75in")
    graphable(guide,cleaner,ndfa2,    "ndfa2",    "ndfa2",     "1.75in")
    graphable(guide,cleaner,ndfa2dfa, "ndfa2dfa", "ndfa2dfa",  "3in")
    graphable(guide,cleaner,hdfa3B,   "hdfa3B",   "hdfa3B",    "3in")
    graphable(guide,cleaner,hdfa3,    "hdfa3",    "hdfa3",     "3in")
    graphable(guide,cleaner,hndfa4B,  "hndfa4B",  "hndfa4B",   "3in")
    graphable(guide,cleaner,hndfa4,   "hndfa4",   "hndfa4",    "3in")
    graphable(guide,cleaner,hndfa4dfa,"hndfa4dfa","hndfa4dfa", "3in")
    graphable(guide,cleaner,dlhPfa57, "dlhPfa57", "dlhPfa57",  "3in")
    graphable(guide,cleaner,dlhPfa57_erem, "dlhPfa57er", "dlhPfa57er",  "3in")
    cleaner
  }

  private val guide = new LaTeXdoc("guide")
  guide.addPackage("times")
  guide.addPackage("graphicx")
  guide.addPackage("multicol")
  guide.open()
  guide ++= "\\begin{multicols}{2}"
  val cleanup = addSamples(guide)
  guide ++= "\\end{multicols}"
  guide.close()
  cleanup.clean
}