package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {
    private val task = Day07(AocDay.Config("Day07_test"))

    @Test
    fun testPart1() {
        assertEquals("95437", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("24933642", task.part2())
    }
}
