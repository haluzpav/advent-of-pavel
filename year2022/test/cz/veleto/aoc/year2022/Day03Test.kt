package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {
    private val task = Day03(Year2022Config("Day03_test"))

    @Test
    fun testPart1() {
        assertEquals("157", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("70", task.part2())
    }
}
