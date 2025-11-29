package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day20Test {
    @Test
    fun testPart1() {
        val task = Day20(Year2022Config("Day20_test"))
        assertEquals("3", task.part1())
    }
    @Test
    fun testPart1_2() {
        val task = Day20(Year2022Config("Day20_test_2"))
        assertEquals("4", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day20(Year2022Config("Day20_test"))
        assertEquals("1623178306", task.part2())
    }
}
