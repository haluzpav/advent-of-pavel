class Day03(filename: String) {
    private val input: List<String> = readInput(filename)

    fun part1(): Int {
        var sum = 0
        for (s in input) {
            check(s.length.rem(2) == 0)
            val (comp1, comp2) = s.chunked(s.length / 2) { it.toSet() }
            val inBoth = comp1.toSet().intersect(comp2).single()
            sum += inBoth.priority
        }
        return sum
    }

    fun part2(): Int {
        check(input.size.rem(3) == 0)
        var sum = 0
        input.chunked(3) { group ->
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
