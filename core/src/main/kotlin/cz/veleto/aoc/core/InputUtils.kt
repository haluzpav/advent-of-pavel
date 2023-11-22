package cz.veleto.aoc.core

import java.io.File

@Suppress("FunctionName")
private fun InputFile(name: String) = File("inputs", "$name.txt")

fun loadInput(name: String): List<String> = InputFile(name).readLines()

fun readInput(name: String): Sequence<String> = sequence {
    yieldAll(InputFile(name).bufferedReader().lineSequence())
}
