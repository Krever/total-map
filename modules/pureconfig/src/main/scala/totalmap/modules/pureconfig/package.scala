package totalmap.modules

import _root_.pureconfig._
import _root_.pureconfig.ConfigReader.Result
import _root_.pureconfig.error.CannotConvert
import com.typesafe.config.ConfigValue
import totalmap.TotalMap.NonTotal
import totalmap.{AllValuesOf, TotalMap}

package object pureconfig {

  implicit def totalMapConfigConvert[K, V](implicit mapConvert: Derivation[ConfigConvert[Map[K, V]]],
                                           staticSet: AllValuesOf[K]): ConfigConvert[TotalMap[K, V]] = {
    val reader: ConfigReader[TotalMap[K, V]] = mapConvert.value.emap(map => {
      TotalMap.fromMap[K, V](map).left.map {
        case NonTotal(missing) => CannotConvert(map.toString(), "TotalMap", s"following keys are missing: ${missing.mkString("[", ",", "]")}")
      }
    })
    val writer = mapConvert.value.contramap[TotalMap[K, V]](_.toMap.toMap)

    new ConfigConvert[TotalMap[K, V]] {
      override def from(cur: ConfigCursor): Result[TotalMap[K, V]] = reader.from(cur)

      override def to(a: TotalMap[K, V]): ConfigValue = writer.to(a)
    }
  }

  implicit def totalMapConfigConvertFromAnonSet[Base, V](implicit ss: AllValuesOf.NamedSet[Base],
                                                         mapConvert: Derivation[ConfigConvert[Map[Base, V]]]): ConfigConvert[TotalMap[ss.Elem, V]] = {
    val reader: ConfigReader[TotalMap[ss.Elem, V]] = mapConvert.value.emap(map => {
      val properMap = map.flatMap { case (k, v) => ss.get.getElem(k).map(_ -> v) }
      TotalMap.fromMap[ss.Elem, V](properMap)(ss.get).left.map {
        case NonTotal(missing) => CannotConvert(map.toString(), "TotalMap", s"following keys are missing: ${missing.mkString("[", ",", "]")}")
      }
    })
    val writer = mapConvert.value.contramap[TotalMap[ss.Elem, V]](_.toMap.toMap)

    new ConfigConvert[TotalMap[ss.Elem, V]] {
      override def from(cur: ConfigCursor): Result[TotalMap[ss.Elem, V]] = reader.from(cur)

      override def to(a: TotalMap[ss.Elem, V]): ConfigValue = writer.to(a)
    }
  }

}
