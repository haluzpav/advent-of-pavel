package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

class Day05(override val config: Year2022Config) : AocDay(config) {

    override fun part1(): String {
        val stacks = parseStacks()
        cachedInput.dropWhile { !it.startsWith("move") }
            .forEach { line ->
                val (count, from, to) = parseMoveLine(line)
                repeat(count) {
                    val removeFirst = stacks[from].removeLast()
                    stacks[to].addLast(removeFirst)
                }
            }
        return getLastCrates(stacks)
    }

    override fun part2(): String {
        val stacks = parseStacks()
        cachedInput.dropWhile { !it.startsWith("move") }
            .forEach { line ->
                val (count, from, to) = parseMoveLine(line)
                val toRemove = stacks[from].takeLast(count)
                repeat(count) { stacks[from].removeLast() }
                stacks[to].addAll(toRemove)
            }
        return getLastCrates(stacks)
    }

    private fun parseStacks(): List<ArrayDeque<Char>> {
        val height = cachedInput.indexOfFirst { it.startsWith(" 1") }
        val count = cachedInput[height].trim().split(' ').last().toInt()
        val stacks = List(count) { ArrayDeque<Char>(height) }
        cachedInput.take(height).forEach { s ->
            s.chunked(4) { it[1].takeUnless { c -> c == ' ' } }
                .forEachIndexed { index, c ->
                    if (c != null) stacks[index].addFirst(c)
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
