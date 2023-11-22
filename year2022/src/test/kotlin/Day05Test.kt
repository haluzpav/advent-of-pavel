import kotlin.test.Test
import kotlin.test.assertEquals

class Day05Test {
    private val task = Day05("Day05_test")

    @Test
    fun testPart1() {
        assertEquals("CMZ", task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals("MCD", task.part2())
    }
}
