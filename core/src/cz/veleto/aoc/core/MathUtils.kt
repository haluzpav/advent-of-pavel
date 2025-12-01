package cz.veleto.aoc.core

import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

fun Int.positiveRem(other: Int): Int = (rem(other) + other).rem(other)

fun Boolean.toInt(): Int = if (this) 1 else 0

fun solveQuadratic(a: Double, b: Double, c: Double): Pair<Double, Double> {
    val det = b.pow(2) - 4 * a * c
    val x1 = (-b - sqrt(det)) / 2 / a
    val x2 = (-b + sqrt(det)) / 2 / a
    return x1 to x2
}

fun leastCommonMultiple(a: Long, b: Long): Long {
    check(a > 0 && b > 0)
    val smaller = min(a, b)
    val larger = max(a, b)
    return (1..smaller)
        .asSequence()
        .map { it * larger }
        .first { it % a == 0L && it % b == 0L }
}

fun leastCommonMultiple(numbers: List<Long>): Long =
    numbers.reduce { acc, number -> leastCommonMultiple(acc, number) }
