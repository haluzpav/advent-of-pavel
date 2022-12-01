import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("inputs", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun <T> Iterable<T>.splitBy(predicate: (T) -> Boolean): List<List<T>> = splitBy(predicate) { it }

fun <T, U> Iterable<T>.splitBy(predicate: (T) -> Boolean, mapElement: (T) -> U): List<List<U>> =
    fold(mutableListOf(mutableListOf<U>())) { acc, s ->
        if (predicate(s)) acc += mutableListOf<U>()
        else acc.last() += mapElement(s)
        acc
    }
