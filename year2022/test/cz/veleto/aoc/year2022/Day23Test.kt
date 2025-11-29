package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    private val task = Day23(Year2022Config("Day23_test"))

    @Test
    fun testPart1() {
        assertEquals("110", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("20", task.part2())
    }
}
