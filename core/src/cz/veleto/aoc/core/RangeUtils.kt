package cz.veleto.aoc.core

fun <T : Comparable<T>> ClosedRange<T>.fullyIn(other: ClosedRange<T>): Boolean =
    isEmpty() || start in other && endInclusive in other

fun <T : Comparable<T>> ClosedRange<T>.overlapsWith(other: ClosedRange<T>): Boolean =
    start in other || endInclusive in other || other.fullyIn(this)

fun IntRange.coerceIn(bounds: IntRange): IntRange =
    start.coerceIn(bounds)..endInclusive.coerceIn(bounds)

fun IntRange.expand(by: Int): IntRange =
    start - by..endInclusive + by

fun IntRange.shift(by: Int): IntRange =
    start + by..endInclusive + by

fun LongRange.shift(by: Long): LongRange =
    start + by..endInclusive + by

fun LongRange.intersect(other: LongRange): LongRange =
    kotlin.math.max(first, other.first)..kotlin.math.min(last, other.last)
