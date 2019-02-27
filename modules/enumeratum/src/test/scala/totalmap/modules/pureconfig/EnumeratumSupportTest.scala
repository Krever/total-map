package enumeratum

import _root_.enumeratum._
import org.scalatest.FunSuite
import totalmap.TotalMap

import scala.collection.immutable

class EnumeratumSupportTest extends FunSuite {

  sealed trait Foo extends EnumEntry

  object Foo extends Enum[Foo] {

    case object A extends Foo

    case object B extends Foo

    override def values: immutable.IndexedSeq[Foo] = findValues
  }


  test("implicit static set for enums") {

    import totalmap.modules.enumeratum._

    val x = TotalMap.fromInstancesOf[Foo]({
      case Foo.A => true
      case Foo.B => false
    })

    assert(x.toMap == Map(Foo.A -> true, Foo.B -> false))
  }

}