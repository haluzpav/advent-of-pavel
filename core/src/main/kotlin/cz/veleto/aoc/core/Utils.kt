package cz.veleto.aoc.core

@Suppress("RemoveExplicitTypeArguments")
fun <T, U> Iterable<T>.splitBy(predicate: (T) -> Boolean, mapElement: (T) -> U): List<List<U>> =
    fold(mutableListOf(mutableListOf<U>())) { acc, s ->
        if (predicate(s)) acc += mutableListOf<U>()
        else acc.last() += mapElement(s)
        acc
    }

operator fun <E> List<E>.component6(): E = this[5]

inline fun <reified T : Enum<T>> T.rotateBy(rotations: Int): T {
    val values = enumValues<T>()
    val size = values.size
    val index = (ordinal + rotations).positiveRem(size)
    return values[index]
}

fun Int.positiveRem(other: Int): Int = (rem(other) + other).rem(other)

fun <T> List<T>.getWrapped(index: Int) = this[index.positiveRem(size)]
