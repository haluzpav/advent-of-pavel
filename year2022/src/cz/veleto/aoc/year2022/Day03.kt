package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

class Day03(config: Config) : AocDay(config) {

    override fun part1(): String = input.map { s ->
        check(s.length.rem(2) == 0)
        val (comp1, comp2) = s.chunked(s.length / 2) { it.toSet() }
        val inBoth = comp1.intersect(comp2).single()
        inBoth.priority
    }.sum().toString()

    override fun part2(): String = input.chunked(3) { group ->
        println(group)
        check(group.size == 3)
        val inAll = group.map { it.toSet() }.reduce { acc, chars -> acc.intersect(chars) }.single()
        inAll.priority
    }.sum().toString()

    private val Char.priority: Int
        get() = when (code) {
            in 'a'.code..'z'.code -> code - 'a'.code + 1
            in 'A'.code..'Z'.code -> code - 'A'.code + 27
            else -> error("Unknown char $this")
        }
}
