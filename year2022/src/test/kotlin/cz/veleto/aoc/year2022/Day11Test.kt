package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    private val task = Day11("Day11_test")

    @Test
    fun testPart1() {
        assertEquals(10605, task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals(2713310158, task.part2())
    }
}
