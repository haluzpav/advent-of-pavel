package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    private val task = Day17("Day17_test")

    @Test
    fun testPart1() {
        assertEquals(3_068, task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals(1_514_285_714_288, task.part2())
    }
}
