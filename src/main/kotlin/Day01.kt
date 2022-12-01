class Day01(filename: String = "Day01") {
    private val input: List<String> = readInput(filename)

    fun part1(): Int = input
        .asSequence()
        .splitBy(predicate = { it == "" }, mapElement = { it.toInt() })
        .maxOf { it.sum() }

    fun part2(): Int = input
        .asSequence()
        .splitBy(predicate = { it == "" }, mapElement = { it.toInt() })
        .map { it.sum() }
        .sorted()
        .takeLast(3)
        .sum()
}

fun main() {
    val task = Day01("Day01")
    println(task.part1())
    println(task.part2())
}
