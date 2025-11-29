package cz.veleto.aoc.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {

    @Test
    fun testPart1() {
        val task = Day11(Year2023Config("Day11_test"))
        assertEquals("374", task.part1())
    }

    @Test
    fun testPart2_1() {
        val task = Day11(Year2023Config("Day11_test", day11part2expandFactor = 10))
        assertEquals("1030", task.part2())
    }

    @Test
    fun testPart2_2() {
        val task = Day11(Year2023Config("Day11_test", day11part2expandFactor = 100))
        assertEquals("8410", task.part2())
    }
}
