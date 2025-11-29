package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
    private val task = Day18(Year2022Config("Day18_test"))

    @Test
    fun testPart1() {
        assertEquals("64", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("58", task.part2())
    }
}
