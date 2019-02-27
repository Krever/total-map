package totalmap.modules.pureconfig

import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuite
import pureconfig.ConfigConvert
import totalmap.{AllValuesOf, TotalMap}

class PureconfigSupportTest extends FunSuite {

  test("read and fail") {
    val cfg = ConfigFactory.parseString(
      """
        |a = foo
        |b = bar
      """.stripMargin)

    implicit val mySet: AllValuesOf.NamedSet[String] = AllValuesOf.set(Set("a", "b", "c"))
    val result = pureconfig.loadConfig[TotalMap[mySet.Elem, String]](cfg)

    assert(result.left.get.head.description == "Cannot convert 'Map(b -> bar, a -> foo)' to TotalMap: following keys are missing: [c].")
  }

  test("read and succeed") {
    val cfg = ConfigFactory.parseString(
      """
        |a = foo
        |b = bar
      """.stripMargin)

    implicit val mySet: AllValuesOf.NamedSet[String] = AllValuesOf.set(Set("a", "b"))
    val result = pureconfig.loadConfig[TotalMap[mySet.Elem, String]](cfg)

    assert(result.right.get.toMap == Map("a" -> "foo", "b" -> "bar"))
  }

  test("read and succeed for boolean") {
    val cfg = ConfigFactory.parseString(
      """
        |true = 1
        |false = 2
      """.stripMargin)

    // until pureconfig allows for keys other than String
    implicit def booleanMapConvert[T: ConfigConvert]: ConfigConvert[Map[Boolean, T]] = ConfigConvert[Map[String, T]].xmap[Map[Boolean, T]](
      _.map{ case (k,v) => k.toBoolean -> v}.toMap,
      _.map{ case (k,v) => k.toString -> v}.toMap,
    )

    val result = pureconfig.loadConfig[TotalMap[Boolean, Int]](cfg)

    assert(result.right.get.toMap == Map(true -> 1, false -> 2))
  }



}
