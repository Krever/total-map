package totalmap.modules

import _root_.enumeratum.EnumEntry
import _root_.enumeratum.Enum
import totalmap.AllValues

package object enumeratum {

  implicit def enumeratumEnumAllValues[E <: EnumEntry](implicit enum: Enum[E]): AllValues[E] =
    AllValues.fromSetUnsafe(enum.values.toSet)

}
