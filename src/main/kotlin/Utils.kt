import java.io.File
import kotlin.math.abs

@Suppress("FunctionName")
private fun InputFile(name: String) = File("inputs", "$name.txt")

fun loadInput(name: String): List<String> = InputFile(name).readLines()

fun readInput(name: String): Sequence<String> = sequence {
    yieldAll(InputFile(name).bufferedReader().lineSequence())
}

fun <T, U> Iterable<T>.splitBy(predicate: (T) -> Boolean, mapElement: (T) -> U): List<List<U>> =
    fold(mutableListOf(mutableListOf<U>())) { acc, s ->
        if (predicate(s)) acc += mutableListOf<U>()
        else acc.last() += mapElement(s)
        acc
    }

typealias Pos = Pair<Int, Int>

operator fun List<String>.get(pos: Pos): Char = this[pos.first][pos.second]

operator fun Pos.plus(other: Pos): Pos = first + other.first to second + other.second

operator fun Pos.minus(other: Pos): Pos = first - other.first to second - other.second

fun Pos.manhattanTo(other: Pos): Int = abs(first - other.first) + abs(second - other.second)

operator fun <E> List<E>.component6(): E = this[5]
