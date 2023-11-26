#!/usr/bin/env kotlin

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.writeText

// https://pypi.org/project/advent-of-code-data/

fun checkAocdExists() {
    val process = Runtime.getRuntime().exec("aocd --version")
    process.waitFor()
    process.errorStream.copyTo(System.err)
    check(process.exitValue() == 0) { "AOCD not available?!" }
}

checkAocdExists()

require(args.size == 1) { "Expecting token as single arg" }
val token = args[0]

val homeDirectory = Path(System.getenv("HOME"))
val aocdTokenFile: Path = homeDirectory.resolve(".config").resolve("aocd").resolve("token")

aocdTokenFile.createParentDirectories()

aocdTokenFile.writeText(token)
