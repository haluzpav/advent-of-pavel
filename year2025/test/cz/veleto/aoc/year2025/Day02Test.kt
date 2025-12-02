package cz.veleto.aoc.year2025

import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {

    @Test
    fun testPart1() {
        val task = Day02(Year2025Config("Day02_test"))
        assertEquals("1227775554", task.part1())
    }
}
