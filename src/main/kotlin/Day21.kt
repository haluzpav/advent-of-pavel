class Day21(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Long {
        val monkeyYells = parseMonkeys().toMutableMap()
        return yell(monkeyYells, "root")
    }

    fun part2(): Int = -1

    private fun parseMonkeys(): Map<String, MonkeyYell> = input.map { line ->
        val name = line.substring(0..3)
        val number = line.substring(6..line.lastIndex).toLongOrNull()
        val yell = if (number == null) {
            MonkeyYell.Math(
                monkey1 = line.substring(6..9),
                monkey2 = line.substring(13..16),
                operation = line[11],
            )
        } else {
            MonkeyYell.Number(number)
        }
        name to yell
    }.toMap()

    private fun yell(monkeyYells: MutableMap<String, MonkeyYell>, yellingMonkey: String): Long =
        when (val yell = monkeyYells[yellingMonkey]) {
            is MonkeyYell.Math -> when (yell.operation) {
                '+' -> yell(monkeyYells, yell.monkey1) + yell(monkeyYells, yell.monkey2)
                '-' -> yell(monkeyYells, yell.monkey1) - yell(monkeyYells, yell.monkey2)
                '*' -> yell(monkeyYells, yell.monkey1) * yell(monkeyYells, yell.monkey2)
                '/' -> yell(monkeyYells, yell.monkey1) / yell(monkeyYells, yell.monkey2)
                else -> error("unknown yell ${yell.operation} for monkey $yellingMonkey")
            }.also { monkeyYells[yellingMonkey] = MonkeyYell.Number(it) }
            is MonkeyYell.Number -> yell.number
            null -> error("unknown yell for monkey $yellingMonkey")
        }

    sealed interface MonkeyYell {
        data class Number(val number: Long) : MonkeyYell
        data class Math(val monkey1: String, val monkey2: String, val operation: Char) : MonkeyYell
    }
}

fun main() {
    val task = Day21("Day21")
    println(task.part1())
    println(task.part2())
}
