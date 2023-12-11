package cz.veleto.aoc.core

inline fun <reified T : Enum<T>> T.rotateBy(rotations: Int): T {
    val values = enumValues<T>()
    val size = values.size
    val index = (ordinal + rotations).positiveRem(size)
    return values[index]
}

fun List<String>.transpose(): List<String> = this
    .also { check(it.map(String::length).allSame()) { "Only rectangle can be transposed" } }
    .first()
    .indices
    .map { columnIndex -> asSequence().map { it[columnIndex] }.joinToString(separator = "") }
