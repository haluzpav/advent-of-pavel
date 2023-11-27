package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {
    private val config = AocDay.Config(
        inputName = "Day15_test",
        year2022day15part1row = 10,
        year2022day15part2max = 20,
    )
    private val task = Day15(config)

    @Test
    fun testPart1() {
        assertEquals("26", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("56000011", task.part2())
    }
}
