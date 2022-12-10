import kotlin.math.abs

class Day10(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    private val xReg: Sequence<Int> = input
        .flatMap {
            val parts = it.split(" ")
            when (val op = parts[0]) {
                "noop" -> listOf(null)
                "addx" -> listOf(null, parts[1].toInt())
                else -> error("Unknown operation $op")
            }
        }
        .runningFold(1) { acc, i -> i?.let { acc + it } ?: acc }

    fun part1(): Int = xReg
        .mapIndexedNotNull { index, i ->
            if ((index + 1 - 20).rem(40) == 0) {
                i * (index + 1)
            } else {
                null
            }
        }
        .sum()

    fun part2(): String = xReg
        .mapIndexed { index, spritePos ->
            val cyclePos = index.rem(40)
            if (abs(cyclePos - spritePos) <= 1) '#' else '.'
        }
        .joinToString(separator = "")
        .chunked(40)
        .take(6)
        .reduce { acc, s -> acc + '\n' + s }
}

fun main() {
    val task = Day10("Day10")
    println(task.part1())
    println(task.part2())
}
