#!/usr/bin/env kotlin

import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
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
    val outputStream = outputStream(StandardOpenOption.CREATE_NEW)
    val process: Process = Runtime.getRuntime().exec("aocd $year $day $commandSuffix")
    process.inputStream.copyTo(outputStream)
    process.waitFor()
}

val exampleInputFile = createInputFilePath(fileSuffix = "_test")
val seriousInputFile = createInputFilePath()

listOf(exampleInputFile, seriousInputFile).forEach { it.parent.createDirectories() }

exampleInputFile.fetchInput(commandSuffix = "-e")
seriousInputFile.fetchInput()
// endregion

// region report
println("Created files:")
listOf(srcFile, testFile, exampleInputFile, seriousInputFile).forEach { println("\t$it") }
// endregion

println("Have fun!")
