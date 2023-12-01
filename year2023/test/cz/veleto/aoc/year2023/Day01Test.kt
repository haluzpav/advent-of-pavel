package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {

    @Test
    fun testPart1() {
        val task = Day01(AocDay.Config("Day01_test_1"))
        assertEquals("142", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day01(AocDay.Config("Day01_test_2"))
        assertEquals("281", task.part2())
    }
}
