package totalmap


import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.FunSuite

class TotalMapTest extends FunSuite with TypeCheckedTripleEquals {
  test("create from instances") {
    implicit val s: AllValuesOf.NamedSet[Int] = AllValuesOf.set(Set(1, 2, 3))

    val m = TotalMap.fromInstancesOf[s.Elem]((x: Int) => x % 2 == 0)

    assertDoesNotCompile(
      "TotalMap.fromInstancesOf[Int]((x: Int) => x % 2 == 0)"
    )

    assert(m.toMap == Map(1 -> false, 2 -> true, 3 -> false))
  }

  test("instances") {
    assert(implicitly[AllValuesOf[Boolean]].values === Set(true, false))
    assert(implicitly[AllValuesOf[Unit]].values === Set(()))
  }

}