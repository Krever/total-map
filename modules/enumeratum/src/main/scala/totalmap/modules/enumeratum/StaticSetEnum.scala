package totalmap.modules.enumeratum

import enumeratum._
import totalmap.AllValuesOf


//TODO rename
trait StaticSetEnum[T <: EnumEntry] { self: Enum[T] =>

  implicit val staticSet: AllValuesOf[T] = enumeratumStaticSet[T](self)

}
