package cz.veleto.aoc.core

@Suppress("RemoveExplicitTypeArguments")
fun <T, U> Iterable<T>.splitBy(predicate: (T) -> Boolean, mapElement: (T) -> U): List<List<U>> =
    fold(mutableListOf(mutableListOf<U>())) { acc, s ->
        if (predicate(s)) acc += mutableListOf<U>()
        else acc.last() += mapElement(s)
        acc
    }

operator fun <E> List<E>.component6(): E = this[5]
fun <T> List<T>.getWrapped(index: Int) = this[index.positiveRem(size)]
fun <T> Iterable<T>.popFirstOrElse(defaultValue: () -> T): Pair<T, List<T>> =
    (firstOrNull() ?: defaultValue()) to drop(1)

fun <T> List<T>.popLastOrElse(defaultValue: () -> T): Pair<List<T>, T> =
    dropLast(1) to (lastOrNull() ?: defaultValue())

fun <T> List<T>.popLastOrNull(): Pair<List<T>, T?> =
    dropLast(1) to lastOrNull()

/**
 * Zips up the 2 iterables. If one is shorter than the other, [defaultValue] will be used instead of
 * the missing values. Alternative to the standard [Iterable.zip].
 */
fun <T, V> Iterable<T>.zipLongest(other: Iterable<T>, defaultValue: () -> T, transform: (T, T) -> V): List<V> {
    val first = iterator()
    val second = other.iterator()
    fun Iterator<T>.nextOrDefault(): T = nextOrDefault(defaultValue)
    return buildList {
        while (first.hasNext() || second.hasNext()) {
            this += transform(first.nextOrDefault(), second.nextOrDefault())
        }
    }
}

fun <T> Iterator<T>.nextOrDefault(defaultValue: () -> T): T =
    if (hasNext()) next() else defaultValue()

fun <T> Iterator<T>.nextOrNull(): T? =
    if (hasNext()) next() else null

fun <T> Iterable<T>.allSame(): Boolean =
    toSet().size == 1
