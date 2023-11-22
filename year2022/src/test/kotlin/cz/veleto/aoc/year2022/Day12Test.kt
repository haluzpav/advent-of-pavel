package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    private val task = Day12("Day12_test")

    @Test
    fun testPart1() {
        assertEquals(31, task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals(29, task.part2())
    }
}
