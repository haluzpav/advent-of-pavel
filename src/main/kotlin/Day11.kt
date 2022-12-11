class Day11(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Long {
        val monkeys = parseMonkeys()
        return simulate(monkeys, rounds = 20) { it / 3 }
    }

    fun part2(): Long {
        val monkeys = parseMonkeys()
        val mod = monkeys.map { it.testDivisor }.reduce { acc, i -> acc * i }
        return simulate(monkeys, rounds = 10_000) { it.rem(mod) }
    }

    private fun parseMonkeys(): List<Monkey> = input
        .map { it.trim() }
        .chunked(7)
        .map { (monkey, items, operation, test, testSuccess, testFail) ->
            Monkey(
                id = monkey[7].digitToInt(),
                operation = run {
                    val parts = operation.split(' ')
                    val number = parts[5].toLongOrNull()
                    when (val op = parts[4].single()) {
                        '+' -> { old: Long -> old + (number ?: old) }
                        '*' -> { old: Long -> old * (number ?: old) }
                        else -> error("Unknown operator $op")
                    }
                },
                testDivisor = test.split(' ')[3].toInt(),
                testSuccessTarget = testSuccess.split(' ')[5].toInt(),
                testFailTarget = testFail.split(' ')[5].toInt(),
                items = items.drop(16).split(", ").map { it.toLong() }.toMutableList(),
            )
        }
        .toList()

    private fun simulate(monkeys: List<Monkey>, rounds: Int, boredom: (Long) -> Long): Long {
        val inspectsCount = (0..monkeys.lastIndex).associateWith { 0 }.toMutableMap()
        for (round in 1..rounds) {
            for (monkey in monkeys) {
                for (item in monkey.items) {
                    val increasedWorry = monkey.operation(item)
                    val boredWorry = boredom(increasedWorry)
                    val target = if (boredWorry.rem(monkey.testDivisor) == 0L) {
                        monkey.testSuccessTarget
                    } else {
                        monkey.testFailTarget
                    }
                    monkeys[target].items += boredWorry
                }
                inspectsCount[monkey.id] = inspectsCount[monkey.id]!! + monkey.items.size
                monkey.items.clear()
            }
        }
        return inspectsCount.values
            .sortedDescending()
            .take(2)
            .fold(1L) { acc, i -> acc * i }
    }

    data class Monkey(
        val id: Int,
        val operation: (Long) -> Long,
        val testDivisor: Int,
        val testSuccessTarget: Int,
        val testFailTarget: Int,
        val items: MutableList<Long>,
    )
}

fun main() {
    val task = Day11("Day11")
    println(task.part1())
    println(task.part2())
}
