class Day20(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Long {
        val list = parseList().toList()
        val mixedList = mix(list)
        return getGroveCoors(mixedList)
    }

    fun part2(): Long {
        val list = parseList()
        val decryptedList = list.map { it * 811_589_153 }.toList()
        val mixedList = (1..10).fold(decryptedList) { acc, _ -> mix(acc) }
        return getGroveCoors(mixedList)
    }

    private fun parseList(): Sequence<Long> = input.map { it.toLong() }

    private fun mix(originalList: List<Long>): List<Long> {
        val list = originalList.toMutableList()
        val size = list.size
        val isMoved = MutableList(size) { false }
        val cycleSize = size - 1

        fun removeAt(index: Int) {
            list.removeAt(index)
            isMoved.removeAt(index)
        }

        fun add(index: Int, value: Long) {
            list.add(index, value)
            isMoved.add(index, true)
        }

        var index = 0
        while (index < size) {
            val n = list[index]
            val newPosMaybeNegative = (index + n).rem(cycleSize).toInt()
            val newPos = (newPosMaybeNegative + cycleSize).rem(cycleSize)
            when {
                newPos == index -> {
                    isMoved[index] = true
                }
                newPos < index -> {
                    removeAt(index)
                    add(newPos, n)
                }
                else -> {
                    add(newPos + 1, n)
                    removeAt(index)
                }
            }
            while (index < size && isMoved[index]) index++
        }
        check(list.size == originalList.size)
        return list
    }

    private fun getGroveCoors(list: List<Long>): Long {
        val zeroIndex = list.indexOf(0)
        return listOf(1000, 2000, 3000).sumOf {
            list[(zeroIndex + it).rem(list.size)]
        }
    }
}

fun main() {
    val task = Day20("Day20")
    println(task.part1())
    println(task.part2())
}
