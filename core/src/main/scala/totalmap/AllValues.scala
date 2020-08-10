package totalmap

sealed trait AllValues[T] {

  def toSet: Set[T]

  def getElem[E >: T](x: E): Option[T] = toSet.find(_ == x)

  override def toString: String = toSet.mkString("AllValues(", ",", ")")

}

object AllValues {

  def apply[T](implicit ev: AllValues[T]): AllValues[T] = ev

  private[totalmap] def fromSetUnsafe[T](set: Set[T]): AllValues[T] = new AllValues[T] {
    override val toSet: Set[T] = set
  }

  implicit val boolAllValues: AllValues[Boolean] = fromSetUnsafe(Set(true, false))

  implicit val unitAllValues: AllValues[Unit] = fromSetUnsafe(Set(()))

}
