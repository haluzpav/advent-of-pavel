package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

class Day21(config: Config) : AocDay(config) {
    
    companion object {
        private const val RootMonkey = "root"
        private const val HumnMonkey = "humn"
    }

    override fun part1(): String {
        val monkeyYells = parseMonkeys().toMutableMap()
        return yell(monkeyYells, RootMonkey, skipHumn = false)!!.toString()
    }

    override fun part2(): String {
        val monkeyYells = parseMonkeys().toMutableMap()
        yell(monkeyYells, RootMonkey, skipHumn = true)
        val (number, monkeyCoveringHumn) = splitMonkeyToNumberAndMath(monkeyYells, RootMonkey)
        return digHumnOutOfMonkeys(monkeyYells, monkeyCoveringHumn, number).toString()
    }

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

    private fun yell(monkeyYells: MutableMap<String, MonkeyYell>, yellingMonkey: String, skipHumn: Boolean): Long? {
        if (skipHumn && yellingMonkey == HumnMonkey) return null
        return when (val yell = monkeyYells[yellingMonkey]) {
            is MonkeyYell.Math -> {
                val evaluatedMonkey1 = yell(monkeyYells, yell.monkey1, skipHumn)
                val evaluatedMonkey2 = yell(monkeyYells, yell.monkey2, skipHumn)
                if (evaluatedMonkey1 != null && evaluatedMonkey2 != null) {
                    when (yell.operation) {
                        '+' -> evaluatedMonkey1 + evaluatedMonkey2
                        '-' -> evaluatedMonkey1 - evaluatedMonkey2
                        '*' -> evaluatedMonkey1 * evaluatedMonkey2
                        '/' -> evaluatedMonkey1 / evaluatedMonkey2
                        else -> error("unknown yell ${yell.operation} for monkey $yellingMonkey")
                    }.also { monkeyYells[yellingMonkey] = MonkeyYell.Number(it) }
                } else {
                    null
                }
            }
            is MonkeyYell.Number -> yell.number
            null -> error("unknown yell for monkey $yellingMonkey")
        }
    }

    private fun splitMonkeyToNumberAndMath(
        monkeyYells: MutableMap<String, MonkeyYell>,
        yellingMonkey: String,
    ): Triple<Long, String, Boolean> {
        val monkey = monkeyYells[yellingMonkey] as MonkeyYell.Math
        val side1 = monkeyYells[monkey.monkey1]!!
        val side2 = monkeyYells[monkey.monkey2]!!
        return when {
            monkey.monkey1 == HumnMonkey || side1 is MonkeyYell.Math -> Triple((side2 as MonkeyYell.Number).number, monkey.monkey1, false)
            side1 is MonkeyYell.Number -> Triple(side1.number, monkey.monkey2, true)
            else -> error("Can't split monkey $yellingMonkey")
        }
    }

    private fun digHumnOutOfMonkeys(
        monkeyYells: MutableMap<String, MonkeyYell>,
        monkeyCoveringHumn: String,
        number: Long,
    ): Long {
        if (monkeyCoveringHumn == HumnMonkey) return number
        val monkey = monkeyYells[monkeyCoveringHumn] as MonkeyYell.Math
        val (yellNumber, newCoveringMonkey, isNumberOnLeft) = splitMonkeyToNumberAndMath(monkeyYells, monkeyCoveringHumn)
        val newNumber = when (monkey.operation) {
            '+' -> number - yellNumber
            '-' -> if (isNumberOnLeft) yellNumber - number else number + yellNumber
            '*' -> number / yellNumber
            '/' -> if (isNumberOnLeft) number / yellNumber else number * yellNumber
            else -> error("unknown yell")
        }
        return digHumnOutOfMonkeys(monkeyYells, newCoveringMonkey, newNumber)
    }

    sealed interface MonkeyYell {
        data class Number(val number: Long) : MonkeyYell
        data class Math(val monkey1: String, val monkey2: String, val operation: Char) : MonkeyYell
    }
}
