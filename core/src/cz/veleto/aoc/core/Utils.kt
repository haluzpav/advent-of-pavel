package cz.veleto.aoc.core

import kotlin.math.pow

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

fun <T> permute(choiceA: T, choiceB: T, size: Int): Sequence<List<T>> {
    val count = 2.0.pow(size).toULong()
    return (0uL..<count)
        .asSequence()
        .map { permutation ->
            (0..<size)
                .map { shift -> permutation shr shift and 1uL == 1uL }
                .map { if (it) choiceA else choiceB }
        }
}
