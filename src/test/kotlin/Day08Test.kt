import kotlin.test.Test
import kotlin.test.assertEquals

class Day08Test {
    private val task = Day08("Day08_test")

    @Test
    fun testPart1() {
        assertEquals(21, task.part1())
    }

    @Test
    fun testPart2() {
        assertEquals(8, task.part2())
    }
}
