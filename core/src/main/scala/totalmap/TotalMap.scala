package totalmap

class TotalMap[K, +V] private (val toMap: Map[K, V])(implicit keys: AllValues[K]) {
  def apply(k: K): V = toMap(k)

  override def toString: String = toMap.mkString("TotalMap(", ",", ")")
}

object TotalMap {

  case class NonTotal[K](missing: Set[K]) extends Exception

  def fromInstancesOf[K] = new Create[K]

  def fromMap[K, K1 >: K, V](map: Map[K1, V])(implicit av: AllValues[K]): Either[NonTotal[K1], TotalMap[K, V]] = {
    val missing = av.toSet.toSet[K1] -- map.keySet
    if (missing.isEmpty) {
      val convertedMap = map.map({ case (k, v) => (av.getElem(k).get -> v) })
      Right(new TotalMap(convertedMap))
    } else {
      Left(NonTotal(missing))
    }
  }

  class Create[K] {
    def apply[V](f: K => V)(implicit set: AllValues[K]): TotalMap[K, V] =
      new TotalMap[K, V](set.toSet.map(k => k -> f(k)).toMap)
  }

}
