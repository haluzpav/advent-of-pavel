package cz.veleto.aoc.year2022

class Day01(inputName: String) {
    private val input: List<String> = loadInput(inputName)

    fun part1(): Int = input
        .splitBy(predicate = { it == "" }, mapElement = { it.toInt() })
        .maxOf { it.sum() }

    fun part2(): Int = input
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
