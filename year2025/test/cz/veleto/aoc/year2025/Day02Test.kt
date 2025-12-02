package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {

    @Test
    fun testPart1() {
        val task = Day02(Year2025Config("Day02_test"))
        assertEquals("1227775554", task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day02(Year2025Config("Day02_test"))
        assertEquals("4174379265", task.part2())
    }
}
