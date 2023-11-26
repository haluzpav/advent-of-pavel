#!/usr/bin/env kotlin

import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists
import kotlin.io.path.outputStream
import kotlin.io.path.writeText

println("New day, new elf adventure!")

// region init
require(args.size == 2) { "Expecting year and day as arguments" }

fun String.isPositiveInt(): Boolean = toIntOrNull().let { it != null && it > 0 }

val year = args[0]
val day = args[1]
require(year.length == 4 && year.isPositiveInt()) { "Expecting year arg in YYYY format" }
require(day.length == 2 && day.isPositiveInt()) { "Expecting day arg in DD format" }

val modulePath = Path("year$year")
check(modulePath.isDirectory()) { "Module for year $year does not exist yet" }
// endregion

// region kotlin files
println("Creating Kotlin files...")

val packagePath = Path("cz", "veleto", "aoc", "year$year")

fun createKotlinFilePath(type: String, fileSuffix: String = ""): Path =
    modulePath.resolve(type).resolve(packagePath).resolve("Day$day$fileSuffix.kt")

val srcFile = createKotlinFilePath("src")
val testFile = createKotlinFilePath("test", fileSuffix = "Test")

listOf(srcFile, testFile).forEach { it.parent.createDirectories() }

fun Path.createFile(text: String) {
    writeText(
        text = text,
        charset = Charsets.UTF_8,
        StandardOpenOption.CREATE_NEW,
    )
}

srcFile.createFile(
    """
        hello impl
        
    """.trimIndent()
)

testFile.createFile(
    """
        hello test
        
    """.trimIndent()
)
// endregion

// region input data
println("Fetching input data...")

fun createInputFilePath(fileSuffix: String = ""): Path =
    modulePath.resolve("inputs").resolve("Day$day$fileSuffix.txt")

fun Path.fetchInput(commandSuffix: String = "") {
    check(notExists()) { "Input file already exists" }
    val aocd: Process = Runtime.getRuntime().exec("aocd $year $day $commandSuffix")
    aocd.errorStream.copyTo(System.err)
    aocd.waitFor()
    if (aocd.exitValue() != 0) {
        aocd.inputStream.copyTo(System.err)
        error("AOCD failed")
    }
    aocd.inputStream.copyTo(outputStream())
}

val exampleInputFile = createInputFilePath(fileSuffix = "_test")
val seriousInputFile = createInputFilePath()

listOf(exampleInputFile, seriousInputFile).forEach { it.parent.createDirectories() }

exampleInputFile.fetchInput(commandSuffix = "-e")
seriousInputFile.fetchInput()
// endregion

// region git add
println("Adding files to git...")
val createdFiles = listOf(srcFile, testFile, exampleInputFile, seriousInputFile)
val git: Process = Runtime.getRuntime().exec(arrayOf("git", "add") + createdFiles.map(Path::toString).toTypedArray())
git.errorStream.copyTo(System.err)
git.waitFor()
check(git.exitValue() == 0) { "Adding files to git failed" }
// endregion

// region report
println("Created files:")
createdFiles.forEach { println("\t$it") }
// endregion

println("Have fun!")
