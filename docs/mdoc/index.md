---
layout: home
title:  "Home"
section: "home"
---

# total-map

**total-map** is a small library for working with types which have finite set of possible values. 

## Why?

To have a `Map[K, V]` that guarantees totality of `apply`.

## What?

The core of the library is just two things:

A typeclass which express all possible values of given type

```scala mdoc
trait AllValues[T] {
    def toSet: Set[T]
}
```

And a data structure which guarantees that there exist an entry for every possible key

```scala mdoc
trait TotalMap[K, +V] {
    def toMap: Map[K, V]
    def apply(k: K): V
}
```

## How?

Install
```scala
libraryDependencies += "com.github.krever" % "total-map" % "@VERSION@"
```

Import
```scala mdoc:reset
import totalmap._
```

An then you need to have an instance of `AllValues[T]`. It can be acquired in few ways:

### Create named set

```scala mdoc
val myStrings = NamedSet("a", "b", "c")
import myStrings.allValues

implicitly[AllValues[myStrings.Elem]]
```

### Use preexisting instances

```scala mdoc
implicitly[AllValues[Unit]]
implicitly[AllValues[Boolean]]
```

Instances that require materializing big collections are not available, but might come if someone comes with reasonable 
use case
```scala mdoc:fail
implicitly[AllValues[Int]]
```

#### Enumeratum

You can get `enumeratum` support by installing

```scala
libraryDependencies += "com.github.krever" % "total-map-enumeratum" % "@VERSION@"
```

So when you have an `Enum`

```scala mdoc
import enumeratum._
sealed trait Animal extends EnumEntry 
object Animal extends Enum[Animal] {
  case object Cat extends Animal
  case object Dog extends Animal
  override def values: scala.collection.immutable.IndexedSeq[Animal] = findValues
}
```

You can the instances for free

```scala mdoc
import totalmap.modules.enumeratum._

implicitly[AllValues[Animal]]
```

## Pureconfig support

You can load `TotalMap`s directly from config. This works currently only for `String`s as keys.

```scala mdoc:reset
import totalmap._
import com.typesafe.config.ConfigFactory
import pureconfig._
import totalmap.modules.pureconfig._

val mySet = NamedSet("a", "b", "c")
import mySet.allValues

implicitly[AllValues[mySet.Elem]]

val cfg = ConfigFactory.parseString(
    """
    |a = foo
    |b = bar
    |c = baz
    """.stripMargin
)

val result = pureconfig.loadConfig[TotalMap[mySet.Elem, String]](cfg)
```



