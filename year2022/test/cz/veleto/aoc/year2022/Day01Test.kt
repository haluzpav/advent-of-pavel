package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {
    private val task = Day01(Year2022Config("Day01_test"))

    @Test
    fun testPart1() {
        assertEquals("24000", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("45000", task.part2())
    }
}
