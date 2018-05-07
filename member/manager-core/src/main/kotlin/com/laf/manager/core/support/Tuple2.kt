package com.laf.manager.core.support

class Tuple2<A, B>(val _1: A, val _2: B) {
    operator fun component1(): A {
        return _1
    }

    operator fun component2(): B {
        return _2
    }
}