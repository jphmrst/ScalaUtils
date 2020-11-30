package org.maraist.search.graph

/**
 *  Thrown when a frontier structure is empty.  Generally this
 *  exception indicates something wrong in the implementation of the
 *  frontier, or else something very wrong in the {@link
 *  GraphSearcher#search search} method: this method arises from the
 *  {@link Frontier#pop pop} method of the frontier
 *  representation, but {@link Frontier#pop pop} is only
 *  called after verifying that {@link Frontier#isEmpty
 *  isEmpty} is false.
 */
class FrontierEmptyException(cause: Throwable)
extends IllegalStateException(FrontierEmptyException.MESSAGE, cause) {
  /**
   *  Used when the underlying data structure of the frontier throws
   *  an exception.
   *
   * @param cause Exeception caught from a method call on the
   * underlying structure on behalf of the {@link
   * Frontier#pop pop} method.
   */
  def this() = this(null)
}

object FrontierEmptyException {
  val MESSAGE = "Frontier (unexpectedly) empty"
}
