package totalmap.modules.pureconfig

import org.scalatest.FunSuite
import totalmap.{NamedSet, TotalMap}

class X extends FunSuite {

  test("read and succeed for boolean") {
    import com.typesafe.config.ConfigFactory
    import pureconfig._
    import totalmap.modules.pureconfig._

    val mySet = NamedSet("a", "b", "c")
    import mySet.allValues

    val cfg1 = ConfigFactory.parseString(
      """
        |a = foo
        |b = bar
    """.stripMargin
    )
    _root_.totalmap.modules.pureconfig.totalMapConfigConvert[mySet.Elem, String]

    val result1 = pureconfig.loadConfig[TotalMap[mySet.Elem, String]](cfg1)

    val cfg2 = ConfigFactory.parseString(
      """
        |a = foo
        |b = bar
        |c = baz
    """.stripMargin
    )

    val result2 = pureconfig.loadConfig[TotalMap[mySet.Elem, String]](cfg2)
  }

}
