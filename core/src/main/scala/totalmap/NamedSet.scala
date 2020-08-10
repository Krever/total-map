package totalmap

import totalmap.AllValues.fromSetUnsafe

sealed trait NamedSet[Src] {
  type Elem <: Src
  implicit def allValues: AllValues[Elem]

  def toMap[V](f: Elem => V): TotalMap[Elem, V] = TotalMap.fromInstancesOf[Elem](f)
}

object NamedSet {

  def fromSet[T](set: Set[T]): NamedSet[T] = new NamedSet[T] {
    override type Elem = T
    implicit val allValues: AllValues[Elem] = fromSetUnsafe(set)
  }

  def apply[T](elems: T*): NamedSet[T] = new NamedSet[T] {
    override type Elem = T
    implicit val allValues: AllValues[Elem] = fromSetUnsafe(elems.toSet)
  }

}
