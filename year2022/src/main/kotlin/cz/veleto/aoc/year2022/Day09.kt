package cz.veleto.aoc.year2022

import java.lang.Integer.max
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day09(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Int = countTailPositions(ropeLength = 2)

    fun part2(): Int = countTailPositions(ropeLength = 10)

    private fun countTailPositions(ropeLength: Int): Int {
        val rope = MutableList(ropeLength) { 0 to 0 }
        val tailPoses = mutableSetOf(rope.last())
        for (s in input) {
            val (direction, steps) = parseMotion(s)
            repeat(steps) {
                rope[0] = moveHead(rope[0], direction)
                rope.indices.drop(1).forEach { rope[it] = moveTail(rope[it - 1], rope[it]) }
                tailPoses += rope.last()
            }
        }
        return tailPoses.size
    }

    private fun parseMotion(line: String): Pair<Char, Int> {
        val (directionChars, stepsChars) = line.split(' ')
        val direction = directionChars.single()
        val steps = stepsChars.toInt()
        return direction to steps
    }

    private fun moveHead(head: Pos, direction: Char): Pos {
        val (hx, hy) = head
        return when (direction) {
            'U' -> hx to hy + 1
            'R' -> hx + 1 to hy
            'D' -> hx to hy - 1
            'L' -> hx - 1 to hy
            else -> error("wut direction $direction")
        }
    }

    private fun moveTail(head: Pos, tail: Pos): Pos {
        val (tx, ty) = tail
        val (dx, dy) = head - tail
        return when {
            max(dx.absoluteValue, dy.absoluteValue) <= 1 -> tail
            dx != 0 && dy != 0 -> tx + dx.sign to ty + dy.sign
            dx != 0 -> tx + dx.sign to ty
            dy != 0 -> tx to ty + dy.sign
            else -> error("wut ds")
        }
    }
}

fun main() {
    val task = Day09("Day09")
    println(task.part1())
    println(task.part2())
}
