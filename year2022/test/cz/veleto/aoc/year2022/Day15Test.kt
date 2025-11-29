package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {
    private val config = Year2022Config(
        inputName = "Day15_test",
        day15part1row = 10,
        day15part2max = 20,
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
