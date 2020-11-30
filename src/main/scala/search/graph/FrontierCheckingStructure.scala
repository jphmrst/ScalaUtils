package org.maraist.search.graph

trait FrontierCheckingStructure[Node] extends Frontier[Node] {
  def contains(n: Node): Boolean
}
