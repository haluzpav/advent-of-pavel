package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

class Day20(config: Config) : AocDay(config) {

    override fun part1(): String {
        val list = parseList().toList()
        val mixedList = mix(list, rounds = 1)
        return getGroveCoors(mixedList).toString()
    }

    override fun part2(): String {
        val list = parseList()
        val decryptedList = list.map { it * 811_589_153 }.toList()
        val mixedList = mix(decryptedList, rounds = 10)
        return getGroveCoors(mixedList).toString()
    }

    private fun parseList(): Sequence<Long> = input.map { it.toLong() }

    private fun mix(originalList: List<Long>, rounds: Int): List<Long> {
        val nodes: MutableList<Node> = Node.listFromLongs(originalList).toMutableList()
        val firstNode = nodes.first()
        val size = nodes.size
        val cycleSize = size - 1

        for (round in 1..rounds) {
            var node = firstNode
            while (node.mixedCount < round) {
                val index = nodes.indexOf(node)
                val newPosMaybeNegative = (index + node.value).rem(cycleSize).toInt()
                val newPos = (newPosMaybeNegative + cycleSize).rem(cycleSize)
                when {
                    newPos < index -> {
                        nodes.removeAt(index)
                        nodes.add(newPos, node)
                    }
                    newPos > index -> {
                        nodes.add(newPos + 1, node)
                        nodes.removeAt(index)
                    }
                    else -> Unit
                }
                node.mixedCount++
                node = node.nextNode
            }
        }
        return nodes.map { it.value }
    }

    private fun getGroveCoors(list: List<Long>): Long {
        val zeroIndex = list.indexOf(0)
        return listOf(1000, 2000, 3000).sumOf {
            list[(zeroIndex + it).rem(list.size)]
        }
    }

    private class Node private constructor(
        val value: Long,
        var mixedCount: Int,
        private var _nextNode: Node? = null,
    ) {
        companion object {
            fun listFromLongs(list: List<Long>): List<Node> {
                val nodes = list.map {
                    Node(
                        value = it,
                        mixedCount = 0,
                    )
                }
                nodes.forEachIndexed { index, node ->
                    node._nextNode = nodes[(index + 1).rem(nodes.size)]
                }
                return nodes
            }
        }

        val nextNode: Node get() = _nextNode!!

        override fun toString(): String = "Node(value=$value, mixedCount=$mixedCount, nextNodeValue=${nextNode.value})"
    }
}
