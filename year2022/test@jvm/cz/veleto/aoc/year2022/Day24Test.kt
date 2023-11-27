package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day24Test {
    private val task = Day24(AocDay.Config("Day24_test"))

    @Test
    fun testPart1() {
        assertEquals("18", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("54", task.part2())
    }
}