package cz.veleto.aoc.core

import kotlin.math.max
import kotlin.math.min

fun <T : Comparable<T>> ClosedRange<T>.fullyIn(other: ClosedRange<T>): Boolean =
    isEmpty() || start in other && endInclusive in other

fun <T : Comparable<T>> ClosedRange<T>.overlapsWith(other: ClosedRange<T>): Boolean =
    start in other || endInclusive in other || other.fullyIn(this)

fun IntRange.coerceIn(bounds: IntRange): IntRange =
    intersect(bounds)

fun IntRange.expand(by: Int): IntRange =
    start - by..endInclusive + by

fun IntRange.shift(by: Int): IntRange =
    start + by..endInclusive + by

fun LongRange.shift(by: Long): LongRange =
    start + by..endInclusive + by

fun IntRange.intersect(other: IntRange): IntRange =
    max(first, other.first)..min(last, other.last)

fun LongRange.intersect(other: LongRange): LongRange =
    max(first, other.first)..min(last, other.last)
