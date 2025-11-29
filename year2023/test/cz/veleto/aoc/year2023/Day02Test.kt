package cz.veleto.aoc.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {

    @Test
    fun testPart1() {
        val task = Day02(Year2023Config("Day02_test"))
        assertEquals("8", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day02(Year2023Config("Day02_test"))
        assertEquals("2286", task.part2())
    }
}
