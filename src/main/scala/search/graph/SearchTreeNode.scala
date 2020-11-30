package org.maraist.search.graph

trait SearchTreeNode[Self <: SearchTreeNode[Self,State], State] {
  val state: State
  def expand(): Iterable[Self]
}

