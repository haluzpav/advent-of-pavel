package cz.veleto.aoc.year2022

import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {
    private val task = Day10(Year2022Config("Day10_test"))

    @Test
    fun testPart1() {
        assertEquals("13140", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("""
            ██  ██  ██  ██  ██  ██  ██  ██  ██  ██  
            ███   ███   ███   ███   ███   ███   ███ 
            ████    ████    ████    ████    ████    
            █████     █████     █████     █████     
            ██████      ██████      ██████      ████
            ███████       ███████       ███████     
        """.trimIndent(), task.part2())
    }
}
