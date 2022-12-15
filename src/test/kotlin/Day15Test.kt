import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {
    private val task = Day15("Day15_test")

    @Test
    fun testPart1() {
        assertEquals(26, task.part1(row = 10))
    }

    @Test
    fun testPart2() {
        assertEquals(-1, task.part2())
    }
}
