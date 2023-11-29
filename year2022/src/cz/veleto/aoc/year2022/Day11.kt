package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.component6

class Day11(config: Config) : AocDay(config) {

    override fun part1(): String {
        val monkeys = parseMonkeys()
        return simulate(monkeys, rounds = 20) { it / 3 }
    }

    override fun part2(): String {
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

    private fun simulate(monkeys: List<Monkey>, rounds: Int, boredom: (Long) -> Long): String {
        val inspectsCount = monkeys.associate { it.id to 0 }.toMutableMap()
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
            .toString()
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
