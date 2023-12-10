package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {

    @Test
    fun testPart1_1() {
        val task = Day10(AocDay.Config("Day10_test_1"))
        assertEquals("4", task.part1())
    }

    @Test
    fun testPart1_2() {
        val task = Day10(AocDay.Config("Day10_test_2"))
        assertEquals("4", task.part1())
    }

    @Test
    fun testPart1_3() {
        val task = Day10(AocDay.Config("Day10_test_3"))
        assertEquals("8", task.part1())
    }

    @Test
    fun testPart1_4() {
        val task = Day10(AocDay.Config("Day10_test_4"))
        assertEquals("8", task.part1())
    }

    @Test
    fun testPart2_5() {
        val task = Day10(AocDay.Config("Day10_test_5"))
        assertEquals("4", task.part2())
    }

    @Test
    fun testPart2_6() {
        val task = Day10(AocDay.Config("Day10_test_6"))
        assertEquals("4", task.part2())
    }

    @Test
    fun testPart2_7() {
        val task = Day10(AocDay.Config("Day10_test_7"))
        assertEquals("8", task.part2())
    }

    @Test
    fun testPart2_8() {
        val task = Day10(AocDay.Config("Day10_test_8"))
        assertEquals("10", task.part2())
    }
}
