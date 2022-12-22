import kotlin.test.Test
import kotlin.test.assertEquals

class Day19Test {
    private val task = Day19("Day19_test")

    @Test
    fun testPart1() {
        assertEquals(33, task.part1())
    }

    @Test
    fun testPart1Blueprint1() {
        val blueprint = task.parseBlueprints().first()
        assertEquals(9, task.calcMaxGeodes(blueprint))
    }

    @Test
    fun testPart1Blueprint2() {
        val blueprint = task.parseBlueprints().last()
        assertEquals(12, task.calcMaxGeodes(blueprint))
    }

    @Test
    fun testPart2() {
        assertEquals(-1, task.part2())
    }
}
