class Day05(filename: String) {
    private val input: List<String> = readInput(filename)

    fun part1(): String {
        val stacks = parseStacks()
        input.dropWhile { !it.startsWith("move") }
            .forEach { line ->
                val (count, from, to) = parseMoveLine(line)
                repeat(count) {
                    val removeFirst = stacks[from].removeLast()
                    stacks[to].addLast(removeFirst)
                }
            }
        return getLastCrates(stacks)
    }

    fun part2(): String {
        val stacks = parseStacks()
        input.dropWhile { !it.startsWith("move") }
            .forEach { line ->
                val (count, from, to) = parseMoveLine(line)
                val toRemove = stacks[from].takeLast(count)
                repeat(count) { stacks[from].removeLast() }
                stacks[to].addAll(toRemove)
            }
        return getLastCrates(stacks)
    }

    private fun parseStacks(): List<ArrayDeque<Char>> {
        val stacks = mutableListOf<ArrayDeque<Char>>()
        for (s in input) {
            val chunked = s.chunked(4) { it[1].takeUnless { it == ' ' } }
            if (chunked[0] == '1') break
            chunked
                .forEachIndexed { index, c ->
                    val stack = stacks.getOrNull(index) ?: ArrayDeque<Char>().also { stacks += it }
                    if (c != null) stack.addFirst(c)
                }
        }
        return stacks
    }

    private fun parseMoveLine(line: String): Triple<Int, Int, Int> {
        check(line.startsWith("move"))
        val (count, from, to) = line.split(' ').slice(listOf(1, 3, 5)).map { it.toInt() }
        return Triple(count, from - 1, to - 1)
    }

    private fun getLastCrates(stacks: List<ArrayDeque<Char>>): String =
        stacks.map { it.last() }.joinToString(separator = "")
}

fun main() {
    val task = Day05("Day05")
    println(task.part1())
    println(task.part2())
}
