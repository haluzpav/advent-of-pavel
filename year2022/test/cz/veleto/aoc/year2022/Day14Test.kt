package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {
    private val task = Day14(AocDay.Config("Day14_test"))

    @Test
    fun testPart1() {
        assertEquals("24", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("93", task.part2())
    }
}
