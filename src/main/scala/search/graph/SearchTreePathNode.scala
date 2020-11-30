package org.maraist.search.graph
import java.util.ArrayList

trait SearchTreePathNode[This <: SearchTreePathNode[This,S], S]
extends SearchTreeNode[This, S] {

  val parent: Option[This]

  def statePath(): ArrayList[S] = statePath(new ArrayList[S]())
  def statePath(states: ArrayList[S]): ArrayList[S] = {
    parent match {
      case None => { }
      case Some(p) => {
        p.statePath(states)
      }
    }
    states.add(state)
    states
  }

  def pathToString(): String = {
    parent match {
      case None => state.toString()
      case Some(p) => p.pathToString() + " >> " + state.toString()
    }
  }
}

