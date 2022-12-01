import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {
    private val day01 = Day01("Day01_test")

    @Test
    fun testPart1() {
        assertEquals(24000, day01.part1())
    }

    @Test
    fun testPart2() {
        assertEquals(45000, day01.part2())
    }
}
