class Day03(filename: String) {
    private val input: List<String> = readInput(filename)

    fun part1(): Int {
        var sum = 0
        for (s in input) {
            val comp1String = s.substring(0 until s.length / 2)
            val comp2String = s.substring(s.length / 2 until s.length)
            val inBoth = comp1String.toSet().intersect(comp2String.toSet()).single()
            sum += inBoth.priority
        }
        return sum
    }

    fun part2(): Int {
        check(input.size.rem(3) == 0)
        var sum = 0
        for (i in 0 until input.size / 3) {
            val group = input.subList(i * 3, i * 3 + 3)
            val inAll = group.map { it.toSet() }.reduce { acc, chars -> acc.intersect(chars) }.single()
            sum += inAll.priority
        }
        return sum
    }

    private val Char.priority: Int
        get() = when (code) {
            in 'a'.code..'z'.code -> code - 'a'.code + 1
            in 'A'.code..'Z'.code -> code - 'A'.code + 27
            else -> error("Unknown char $this")
        }
}

fun main() {
    val task = Day03("Day03")
    println(task.part1())
    println(task.part2())
}
