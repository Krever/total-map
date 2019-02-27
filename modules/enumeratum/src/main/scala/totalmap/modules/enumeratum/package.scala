package totalmap.modules

import totalmap.AllValuesOf

import _root_.enumeratum._

package object enumeratum {

  implicit def enumeratumStaticSet[E <: EnumEntry](implicit enum: Enum[E]): AllValuesOf[E] = AllValuesOf.fromSetUnsafe(enum.values.toSet)

}
