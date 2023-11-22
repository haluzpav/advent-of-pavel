package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day04Test {
    private val task = Day04("Day04_test")

    @Test
    fun testPart1() {
        assertEquals(2, task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals(4, task.part2())
    }
}
