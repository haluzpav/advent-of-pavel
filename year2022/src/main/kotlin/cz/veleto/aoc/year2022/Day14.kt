package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.Pos
import cz.veleto.aoc.core.readInput
import kotlin.math.max
import kotlin.math.min

class Day14(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    private val xShift = -300
    private val maxX = 700
    private val maxXShifted = maxX + xShift

    fun part1(): Int {
        val grid = parseInput()
        var lastSandPos: Pos? = null
        var sandCount = 0
        while (lastSandPos == null || lastSandPos.second < grid.lastIndex) {
            if (lastSandPos == null) {
                val x = 500 + xShift
                val y = 0
                grid[y][x] = Block.Sand
                lastSandPos = x to y
                sandCount++
            } else {
                lastSandPos = letSandFall(grid, lastSandPos)
            }
        }
        printGrid(grid)
        return sandCount - 1
    }

    fun part2(): Int {
        val grid = parseInput()
        addFloor(grid)
        var lastSandPos: Pos? = null
        var sandCount = 0
        while (true) {
            if (lastSandPos == null) {
                val x = 500 + xShift
                val y = 0
                if (grid[y][x] == Block.Sand) break
                grid[y][x] = Block.Sand
                lastSandPos = x to y
                sandCount++
            } else {
                lastSandPos = letSandFall(grid, lastSandPos)
            }
        }
        printGrid(grid)
        return sandCount
    }

    private fun parseInput(): MutableList<MutableList<Block>> {
        val grid = mutableListOf<MutableList<Block>>()
        for (s in input) {
            val corners = s.split(" -> ").map { stringPos ->
                val (x, y) = stringPos.split(',').map { it.toInt() }
                x + xShift to y
            }
            corners.forEachIndexed { index, (targetX, targetY) ->
                if (index == 0) return@forEachIndexed
                val (sourceX, sourceY) = corners[index - 1]
                for (x in min(sourceX, targetX)..max(sourceX, targetX)) {
                    for (y in min(sourceY, targetY)..max(sourceY, targetY)) {
                        repeat(y - grid.size + 1) {
                            grid += MutableList(maxXShifted) { Block.Air }
                        }
                        grid[y][x] = Block.Rock
                    }
                }
            }
        }
        return grid
    }

    private fun printGrid(grid: List<List<Block>>) {
        for (layer in grid) {
            layer.joinToString(separator = "") {
                when (it) {
                    Block.Air -> " "
                    Block.Rock -> "█"
                    Block.Sand -> "o"
                }
            }.also { println(it) }
        }
    }

    private fun letSandFall(grid: List<MutableList<Block>>, lastSandPos: Pos): Pos? {
        val (lastX, lastY) = lastSandPos
        check(grid[lastY][lastX] == Block.Sand)
        val nextSandPos = when (Block.Air) {
            grid[lastY + 1][lastX] -> lastX to lastY + 1
            grid[lastY + 1][lastX - 1] -> lastX - 1 to lastY + 1
            grid[lastY + 1][lastX + 1] -> lastX + 1 to lastY + 1
            else -> null
        }
        if (nextSandPos != null) {
            val (nextX, nextY) = nextSandPos
            grid[lastY][lastX] = Block.Air
            grid[nextY][nextX] = Block.Sand
        }
        return nextSandPos
    }

    private fun addFloor(grid: MutableList<MutableList<Block>>) {
        grid += MutableList(maxXShifted) { Block.Air }
        grid += MutableList(maxXShifted) { Block.Rock }
    }

    private enum class Block {
        Air, Rock, Sand
    }
}

fun main() {
    val task = Day14("Day14")
    println(task.part1())
    println(task.part2())
}
