package cz.veleto.aoc.year2022

class Day04(inputName: String) {
    private val input: List<String> = loadInput(inputName)

    fun part1(): Int = input
        .map(::parseRanges)
        .count { (a, b) -> a.fullyIn(b) || b.fullyIn(a) }

    fun part2(): Int = input
        .map(::parseRanges)
        .count { (a, b) -> b.first in a || a.first in b }

    private fun parseRanges(s: String): Pair<UIntRange, UIntRange> = s
        .split(',')
        .map { sRange ->
            sRange.split('-')
                .map { it.toUInt() }
                .also { check(it.size == 2) }
                .let { (a, b) -> a..b }
                .also { check(!it.isEmpty()) }
        }
        .also { check(it.size == 2) }
        .let { (a, b) -> a to b }

    private fun <T : Comparable<T>> ClosedRange<T>.fullyIn(other: ClosedRange<T>): Boolean =
        start in other && endInclusive in other
}

fun main() {
    val task = Day04("Day04")
    println(task.part1())
    println(task.part2())
}
