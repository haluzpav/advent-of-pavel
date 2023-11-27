package cz.veleto.aoc.core

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

internal expect val fileSystem: FileSystem

fun readInput(name: String): Sequence<String> = sequence {
    val path = "inputs".toPath().resolve("$name.txt")
        // in case of macosApp, we need to look for the file one dir above // TODO do somehow better
        .let { if (!fileSystem.exists(it)) "..".toPath().resolve(it) else it }
    fileSystem.source(path).use { source ->
        source.buffer().use { bufferedSource ->
            while (true) {
                yield(bufferedSource.readUtf8Line() ?: break)
            }
        }
    }
}
