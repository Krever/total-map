package totalmap.modules

import _root_.pureconfig.ConfigReader.Result
import _root_.pureconfig._
import _root_.pureconfig.error.CannotConvert
import com.typesafe.config.ConfigValue
import totalmap.TotalMap.NonTotal
import totalmap.{AllValues, TotalMap}

package object pureconfig {

  implicit def totalMapConfigConvert[K, V](implicit mapConvert: Derivation[ConfigConvert[Map[K, V]]],
                                           allValues: AllValues[K]): ConfigConvert[TotalMap[K, V]] = {
    val reader: ConfigReader[TotalMap[K, V]] = mapConvert.value.emap(map => {
      TotalMap.fromMap(map).left.map {
        case NonTotal(missing) =>
          CannotConvert(map.toString(), "TotalMap", s"following keys are missing: ${missing.mkString("[", ",", "]")}")
      }
    })
    val writer = mapConvert.value.contramap[TotalMap[K, V]](_.toMap.toMap)

    new ConfigConvert[TotalMap[K, V]] {
      override def from(cur: ConfigCursor): Result[TotalMap[K, V]] = reader.from(cur)

      override def to(a: TotalMap[K, V]): ConfigValue = writer.to(a)
    }
  }

  implicit def mapConvert[K <: String, V](implicit allValues: AllValues[K],
                                          vReader: Derivation[ConfigReader[V]],
                                          vWriter: Derivation[ConfigWriter[V]]): ConfigConvert[Map[K, V]] = {
    import _root_.pureconfig._
    import _root_.pureconfig.configurable._
    import _root_.pureconfig.error._

    val reader: ConfigReader[Map[K, V]] = genericMapReader(
      key => allValues.getElem(key).toRight(KeyNotFound(key, allValues.toSet.toSet)))
    val writer: ConfigWriter[Map[K, V]] = genericMapWriter((x: K) => x)

    ConfigConvert[Map[K, V]](reader, writer)
  }

}
