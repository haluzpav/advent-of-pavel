package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

class Day07(config: Config) : AocDay(config) {
    
    override fun part1(): String {
        val graph = parseIntoGraph()
        calculateDirSizes(graph)
        val atMost100kDirs = findAtMost100kDirs(graph)
        return atMost100kDirs.sumOf { it.childrenSize!! }.toString()
    }

    override fun part2(): String {
        val maxSpace = 70000000
        val minNeeded = 30000000
        val graph = parseIntoGraph()
        calculateDirSizes(graph)
        val nowAvailable = maxSpace - graph.childrenSize!!
        val toDeleteAtLeast = minNeeded - nowAvailable
        check(toDeleteAtLeast > 0)
        val deletionCandidates = findAtLeastDirs(graph, toDeleteAtLeast)
        return deletionCandidates.minOf { it.childrenSize!! }.toString()
    }

    private fun parseIntoGraph(): Node.Directory {
        check(cachedInput[0] == """$ cd /""")
        val root = Node.Directory("/")
        parseChildren(root, indexToContinueFrom = 1)
        return root
    }

    private fun parseChildren(directory: Node.Directory, indexToContinueFrom: Int): Int {
        check(cachedInput[indexToContinueFrom] == """$ ls""")
        var i = indexToContinueFrom + 1
        while (i < cachedInput.size) {
            val line = cachedInput[i]
            when {
                line == """$ cd ..""" -> return (i - indexToContinueFrom + 1)
                line.startsWith("""$ cd""") -> {
                    val childName = line.split(" ").also { check(it.size == 3) }[2]
                    directory.children
                        .filterIsInstance<Node.Directory>()
                        .find { it.name == childName }!!
                        .let { i += parseChildren(it, indexToContinueFrom = i + 1) }
                }
                line.startsWith("""dir""") -> {
                    directory.children += Node.Directory(
                        name = line.split(" ").also { check(it.size == 2) }[1],
                    )
                }
                else -> {
                    val (size, name) = line.split(" ").also { check(it.size == 2) }
                    directory.children += Node.File(
                        name = name,
                        size = size.toLong(),
                    )
                }
            }
            i++
        }
        return (i - indexToContinueFrom + 1)
    }

    private fun calculateDirSizes(graph: Node.Directory): Long = graph.children.sumOf {
        when (it) {
            is Node.Directory -> calculateDirSizes(it)
            is Node.File -> it.size
        }
    }.also { graph.childrenSize = it }

    private fun findAtMost100kDirs(graph: Node.Directory): List<Node.Directory> =
        listOfNotNull(graph.takeIf { it.childrenSize!! <= 100_000 }) + graph.children
            .filterIsInstance<Node.Directory>()
            .flatMap { findAtMost100kDirs(it) }

    private fun findAtLeastDirs(graph: Node.Directory, minSize: Long): List<Node.Directory> =
        graph.takeIf { it.childrenSize!! >= minSize }?.let { bigEnoughGraph ->
            listOf(bigEnoughGraph) + bigEnoughGraph.children
                .filterIsInstance<Node.Directory>()
                .flatMap { findAtLeastDirs(it, minSize) }
        } ?: emptyList()

    private sealed interface Node {
        data class Directory(
            val name: String,
            val children: MutableList<Node> = mutableListOf(),
            var childrenSize: Long? = null,
        ) : Node

        data class File(
            val name: String,
            val size: Long,
        ) : Node
    }
}
