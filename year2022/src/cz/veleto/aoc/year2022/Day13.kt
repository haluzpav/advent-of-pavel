package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.readInput
import kotlin.math.min

class Day13(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Int = input.chunked(3)
        .mapIndexed { pairIndex, (leftString, rightString) ->
            val left = parseList(leftString)
            val right = parseList(rightString)
            alignLists(left, right)
            val isInRightOrder = compareLists(left, right)
            if (isInRightOrder) pairIndex + 1 else 0
        }.sum()

    fun part2(): Int {
        val indexOf2 = countBefore("""[[2]]""") + 1
        val indexOf6 = countBefore("""[[6]]""") + 2
        return indexOf2 * indexOf6
    }

    private fun parseList(string: String): Element.List {
        val root = Element.List()
        val lists = mutableListOf<Element.List>()
        var lastOneIndex: Int? = null
        for (c in string) {
            when (c) {
                '[' -> lists += if (lists.isEmpty()) root else Element.List().also { lists.last().list += it }
                '1' -> {
                    lists.last().list += Element.Int(1)
                    lastOneIndex = 0
                }
                in '0'..'9' -> {
                    val int = c.digitToInt().let {
                        if (lastOneIndex == 1) {
                            lists.last().list.removeLast()
                            it + 10
                        } else {
                            it
                        }
                    }
                    lists.last().list += Element.Int(int)
                }
                ']' -> lists.removeLast()
                ',' -> Unit
                else -> error("undefined char $c")
            }
            lastOneIndex?.let { lastOneIndex = it + 1 }
        }
        check(lists.isEmpty())
        return root
    }

    private fun alignLists(left: Element.List, right: Element.List) {
        val listsToGoThrough = mutableListOf(left to right)
        while (listsToGoThrough.isNotEmpty()) {
            val (l, r) = listsToGoThrough.removeFirst()
            (l.list zip r.list).forEachIndexed { index, (le, re) ->
                when {
                    le is Element.Int && re is Element.List -> l.list[index] = Element.List(mutableListOf(le))
                        .also { listsToGoThrough += it to re }
                    le is Element.List && re is Element.Int -> r.list[index] = Element.List(mutableListOf(re))
                        .also { listsToGoThrough += le to it }
                    le is Element.List && re is Element.List -> listsToGoThrough += le to re
                }
            }
        }
    }

    private fun compareLists(left: Element.List, right: Element.List): Boolean {
        val listsToGoThrough = mutableListOf(Triple(left, right, 0))
        while (listsToGoThrough.isNotEmpty()) {
            val (l, r, startIndex) = listsToGoThrough.removeLast()
            var finished = true
            for (i in startIndex until min(l.list.size, r.list.size)) {
                val le = l.list[i]
                val re = r.list[i]
                when {
                    le is Element.List && re is Element.List -> {
                        listsToGoThrough += Triple(l, r, i + 1)
                        listsToGoThrough += Triple(le, re, 0)
                        finished = false
                        break
                    }
                    le is Element.Int && re is Element.Int && le.int < re.int -> return true
                    le is Element.Int && re is Element.Int && le.int > re.int -> return false
                }
            }
            if (finished) {
                when {
                    l.list.size < r.list.size -> return true
                    l.list.size > r.list.size -> return false
                }
            }
        }
        error("the list are the same?!")
    }

    private fun countBefore(string: String) : Int = input
        .filter { it.isNotBlank() }
        .count {
            val left = parseList(it)
            val right = parseList(string)
            alignLists(left, right)
            compareLists(left, right)
        }

    private sealed interface Element {
        @JvmInline
        value class Int(val int: kotlin.Int) : Element {
            override fun toString(): String = int.toString()
        }

        @JvmInline
        value class List(val list: MutableList<Element> = mutableListOf()) : Element {
            override fun toString(): String = list.toString()
        }
    }
}

fun main() {
    val task = Day13("Day13")
    println(task.part1())
    println(task.part2())
}
