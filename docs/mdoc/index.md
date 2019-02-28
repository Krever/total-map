---
layout: home
title:  "Home"
section: "home"
---

# total-map

**total-map** is a small library for working with types which have finite set of possible values. 

## Why?

I want to have a `Map[K, V]` (read from config or created manually) that guarantees totality of `apply`.

## What?

The core of the library is just two things:

### `AllValuesOf[T]` 

A typeclass which express all possible values of given type

```scala mdoc
trait AllValuesOf[T] {
    def toSet: Set[T]
}
```

### `TotalMap[K, V]` 

A data structure which guarantees that there exist an entry for every possible key

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

An then you need to have an instance of `AllValuesOf[T]`. It can be acquired in one of 2 ways:

### Use preexisting instances

```scala mdoc
implicitly[AllValuesOf[Unit]]
implicitly[AllValuesOf[Boolean]]
```

Instances that require materializing big collections are not available, but might come if someone comes with reasonable 
use case
```scala mdoc:fail
implicitly[AllValuesOf[Int]]
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

```
import totalmap.modules.enumeratum._

implicitly[AllValuesOf[Animal]]
```


### Create named set

```scala mdoc
implicit val myStrings = AllValuesOf.set(Set("a", "b", "c"))

implicitly[AllValuesOf[myStrings.Elem]]
```







