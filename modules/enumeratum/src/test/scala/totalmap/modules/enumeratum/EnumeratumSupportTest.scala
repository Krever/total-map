package totalmap.modules.enumeratum

import _root_.enumeratum._
import org.scalatest.FunSuite
import totalmap.{AllValues, TotalMap}

import scala.collection.immutable

class EnumeratumSupportTest extends FunSuite {

  sealed trait Foo extends EnumEntry

  object Foo extends Enum[Foo] {

    case object A extends Foo

    case object B extends Foo

    override def values: immutable.IndexedSeq[Foo] = findValues
  }

  test("implicit AllValues for enums") {

    import totalmap.modules.enumeratum._

    implicitly[AllValues[Foo]]
  }

}
