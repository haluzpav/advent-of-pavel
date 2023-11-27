package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    private val task = Day17(AocDay.Config("Day17_test"))

    @Test
    fun testPart1() {
        assertEquals("3068", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("1514285714288", task.part2())
    }
}
