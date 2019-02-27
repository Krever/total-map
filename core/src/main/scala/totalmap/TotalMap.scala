package totalmap


class TotalMap[K, +V] private(val toMap: Map[K, V], keys: AllValuesOf[K]) {
  def apply(k: K): V = toMap(k)

  override def toString: String = toMap.mkString("TotalMap(", ",", ")")
}

object TotalMap {

  case class NonTotal[K](missing: Set[K]) extends Exception

  def fromInstancesOf[K] = new Create[K]

  def fromMap[K, V](map: Map[K,V])(implicit ss: AllValuesOf[K]):  Either[NonTotal[K], TotalMap[K, V]] = {
    val missing = ss.values -- map.keySet
    if(missing.isEmpty){
      Right(new TotalMap(map, ss))
    } else {
      Left(NonTotal(missing))
    }
  }

  class Create[K]{
    def apply[V](f: K => V)(implicit set: AllValuesOf[K]): TotalMap[K, V] = new TotalMap[K, V](set.values.map(k => k -> f(k)).toMap, set)
  }


}