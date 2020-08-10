package totalmap

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.FunSuite

class TotalMapTest extends FunSuite with TypeCheckedTripleEquals {
  test("create from elements") {
    val s: NamedSet[Int] = NamedSet(1, 2, 3)
    import s.allValues

    val m = TotalMap.fromInstancesOf[s.Elem](_ % 2 == 0)

    assert(m.toMap == Map(1 -> false, 2 -> true, 3 -> false))
  }

  test("instances") {
    assert(implicitly[AllValues[Boolean]].toSet === Set(true, false))
    assert(implicitly[AllValues[Unit]].toSet === Set(()))
  }

}
