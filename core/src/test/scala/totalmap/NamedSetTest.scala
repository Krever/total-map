package totalmap

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.FunSuite

class NamedSetTest extends FunSuite with TypeCheckedTripleEquals {

  test("provide AllValues") {
    val s: NamedSet[Int] = NamedSet(1, 2, 3)
    import s.allValues

    implicitly[AllValues[s.Elem]]
  }

  test("toMap") {
    val s: NamedSet[Int] = NamedSet(1, 2, 3)
    val m                = s.toMap(_ % 2 == 0)
    assert(m.toMap == Map(1 -> false, 2 -> true, 3 -> false))
  }

}
