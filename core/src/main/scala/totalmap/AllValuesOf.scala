package totalmap


sealed trait AllValuesOf[T] {

  def values: Set[T]

  def getElem[E >: T](x: E): Option[T] = values.find(_ == x)

  override def toString: String = values.mkString("AllValuesOf(", ",", ")")

}

object AllValuesOf {

  def apply[T](implicit ev: AllValuesOf[T]): AllValuesOf[T] = ev

  trait NamedSet[Src] {
    type Elem <: Src
    def get: AllValuesOf[Elem]
  }

  private[totalmap] def fromSetUnsafe[T](set: Set[T]): AllValuesOf[T] = new AllValuesOf[T] {
    override def values: Set[T] = set
  }

  def set[T](set: Set[T]): NamedSet[T] = new NamedSet[T] {
    override type Elem = T

    def get: AllValuesOf[Elem] = fromSetUnsafe(set)
  }


  implicit val boolStaticSet: AllValuesOf[Boolean] = fromSetUnsafe(Set(true, false))

  implicit val unitStaticSet: AllValuesOf[Unit] = fromSetUnsafe(Set(()))

  implicit def anonSetStaticSet[K](implicit anonSet: NamedSet[K]): AllValuesOf[anonSet.Elem] = anonSet.get

}