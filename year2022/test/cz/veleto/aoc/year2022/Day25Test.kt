package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day25Test {
    private val task = Day25(Year2022Config("Day25_test"))

    @Test
    fun testPart1() {
        assertEquals("2=-1=0", task.part1())
    }
}
