class Day21(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    companion object {
        private const val rootMonkey = "root"
        private const val humnMonkey = "humn"
    }

    fun part1(): Long {
        val monkeyYells = parseMonkeys().toMutableMap()
        return yell(monkeyYells, rootMonkey, skipHumn = false)!!
    }

    fun part2(): Long {
        val monkeyYells = parseMonkeys().toMutableMap()
        yell(monkeyYells, rootMonkey, skipHumn = true)
        val (number, monkeyCoveringHumn) = splitMonkeyToNumberAndMath(monkeyYells, rootMonkey)
        return digHumnOutOfMonkeys(monkeyYells, monkeyCoveringHumn, number)
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
        if (skipHumn && yellingMonkey == humnMonkey) return null
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
            monkey.monkey1 == humnMonkey || side1 is MonkeyYell.Math -> Triple((side2 as MonkeyYell.Number).number, monkey.monkey1, false)
            side1 is MonkeyYell.Number -> Triple(side1.number, monkey.monkey2, true)
            else -> error("Can't split monkey $yellingMonkey")
        }
    }

    private fun digHumnOutOfMonkeys(
        monkeyYells: MutableMap<String, MonkeyYell>,
        monkeyCoveringHumn: String,
        number: Long,
    ): Long {
        if (monkeyCoveringHumn == humnMonkey) return number
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

fun main() {
    val task = Day21("Day21")
    println(task.part1())
    println(task.part2())
}
