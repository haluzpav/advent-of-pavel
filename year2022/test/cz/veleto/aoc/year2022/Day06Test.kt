package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day06Test {
    private val task = Day06("Day06_test")

    @Test
    fun testPart1() {
        assertEquals(listOf(7, 5, 6, 10, 11), task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals(listOf(19, 23, 23, 29, 26), task.part2())
    }
}
