#!/usr/bin/env kotlin

import java.io.InputStream
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists
import kotlin.io.path.outputStream
import kotlin.io.path.writeLines
import kotlin.io.path.writeText

// TODO handle more than one example test data in a day
// TODO also update createDayCommon somehow...

fun String.isPositiveInt(): Boolean = toIntOrNull().let { it != null && it > 0 }

fun parseArgs(): Pair<String, String> {
    require(args.size == 2) { "Expecting year and day as arguments" }

    val year = args[0]
    val day = args[1]
    require(year.length == 4 && year.isPositiveInt()) { "Expecting year arg in YYYY format" }
    require(day.length == 2 && day.isPositiveInt()) { "Expecting day arg in DD format" }

    return year to day
}

fun createModulePath(year: String): Path = Path("year${year}").also {
    check(it.isDirectory()) { "Module for year $year does not exist yet" }
}

fun createKotlinSrcFileContent(year: String, day: String): String = """
    package cz.veleto.aoc.year$year

    import cz.veleto.aoc.core.AocDay

    class Day$day(config: Config) : AocDay(config) {

        override fun part1(): String {
            // TODO
            return ""
        }

        override fun part2(): String {
            // TODO
            return ""
        }
    }

""".trimIndent()

fun createKotlinTestFileContent(year: String, day: String, answerA: String?, answerB: String?): String = """
    package cz.veleto.aoc.year$year

    import cz.veleto.aoc.core.AocDay
    import kotlin.test.Test
    import kotlin.test.assertEquals

    class Day${day}Test {
        private val task = Day$day(AocDay.Config("Day${day}_test"))

        @Test
        fun testPart1() {
            assertEquals("%s", task.part1())
        }

        @Test
        fun testPart2() {
            assertEquals("%s", task.part2())
        }
    }

""".trimIndent().format(answerA ?: "TODO", answerB ?: "TODO")

fun Path.createFile(text: String) {
    writeText(
        text = text,
        charset = Charsets.UTF_8,
        StandardOpenOption.CREATE_NEW,
    )
}

fun createKotlinFiles(
    modulePath: Path,
    year: String,
    day: String,
    answerA: String?,
    answerB: String?,
): List<Path> {
    val packagePath = Path("cz", "veleto", "aoc", "year$year")

    fun createKotlinFilePath(type: String, fileSuffix: String = ""): Path =
        modulePath.resolve(type).resolve(packagePath).resolve("Day$day$fileSuffix.kt")

    val srcFile = createKotlinFilePath("src")
    val testFile = createKotlinFilePath("test", fileSuffix = "Test")

    val filePaths = listOf(srcFile, testFile)
    filePaths.forEach { it.createParentDirectories() }

    srcFile.createFile(createKotlinSrcFileContent(year, day))
    testFile.createFile(createKotlinTestFileContent(year, day, answerA, answerB))

    return filePaths
}

fun createInputFilePath(modulePath: Path, day: String, fileSuffix: String = ""): Path =
    modulePath.resolve("inputs").resolve("Day$day$fileSuffix.txt").also {
        check(it.notExists()) { "Input file already exists" }
    }

fun fetchInput(year: String, day: String, commandSuffix: String? = null): InputStream {
    val aocd: Process = listOfNotNull("aocd", year, day, commandSuffix).toTypedArray().awaitProcess()
    if (aocd.exitValue() != 0) {
        aocd.inputStream.copyTo(System.err)
        error("AOCD failed")
    }
    return aocd.inputStream
}

fun Array<String>.awaitProcess(): Process = Runtime.getRuntime().exec(this).apply {
    errorStream.copyTo(System.err)
    waitFor()
}

fun createInputFiles(modulePath: Path, year: String, day: String): Triple<List<Path>, String?, String?> {
    val exampleInputFile = createInputFilePath(modulePath, day, fileSuffix = "_test")
    val seriousInputFile = createInputFilePath(modulePath, day)

    val inputFiles = listOf(exampleInputFile, seriousInputFile)
    inputFiles.forEach { it.createParentDirectories() }

    val (answerA, answerB) = fetchExampleInput(exampleInputFile, year, day)
    fetchInput(year, day).copyTo(seriousInputFile.outputStream())

    return Triple(inputFiles, answerA, answerB)
}

fun fetchExampleInput(targetPath: Path, year: String, day: String): Pair<String?, String?> {
    val lines = fetchInput(year, day, commandSuffix = "-e").reader().readLines()
    check("/${year}/day/${day.toInt()} " in lines[1]) { "Example input isn't for the requested year and day" }

    val hasSingleData = " 1/1 " in lines[2]
    return if (hasSingleData) {
        parseSingleData(targetPath, lines)
    } else {
        targetPath.writeLines(lines)
        null to null
    }
}

fun parseSingleData(targetPath: Path, lines: List<String>): Pair<String?, String?> {
    val separatorStart = "---"
    val answerARegex = Regex("^answer_a: (.+)$")
    val answerBRegex = Regex("^answer_b: (.+)$")

    val answerAIndex = lines.indexOfFirst { it.matches(answerARegex) }
    check(answerAIndex - 1 > 2) { "Example data don't match expected format [0]" }
    check(lines[answerAIndex - 1].startsWith(separatorStart)) { "Example data don't match expected format [1]" }
    check(lines[answerAIndex + 1].matches(answerBRegex)) { "Example data don't match expected format [2]" }
    check(lines[answerAIndex + 2].startsWith(separatorStart)) { "Example data don't match expected format [3]" }

    targetPath.writeLines(lines.subList(3, answerAIndex - 1))

    val (answerA) = answerARegex.matchEntire(lines[answerAIndex])!!.destructured
    val (answerB) = answerBRegex.matchEntire(lines[answerAIndex + 1])!!.destructured

    return answerA to answerB
}

fun addFilesToGit(filePaths: List<Path>) {
    val git: Process = (arrayOf("git", "add") + filePaths.map(Path::toString).toTypedArray()).awaitProcess()
    check(git.exitValue() == 0) { "Adding files to git failed" }
}

fun main() {
    println("New day, new elf adventure!")

    val (year, day) = parseArgs()
    val modulePath = createModulePath(year)

    println("Fetching input data...")
    val (inputFiles, answerA, answerB) = createInputFiles(modulePath, year, day)

    println("Creating Kotlin files...")
    val kotlinFiles = createKotlinFiles(modulePath, year, day, answerA, answerB)

    val createdFiles = kotlinFiles + inputFiles

    println("Adding files to git...")
    addFilesToGit(createdFiles)

    println("Created files:")
    createdFiles.forEach { println("\t$it") }

    println("Have fun!")
}

main()
