import kotlin.test.Test
import kotlin.test.assertEquals

class Day09Test {

    @Test
    fun testPart1() {
        val task = Day09("Day09_test_1")
        assertEquals(13, task.part1())
    }

    @Test
    fun testPart2() {
        val task = Day09("Day09_test_2")
        assertEquals(36, task.part2())
    }
}
