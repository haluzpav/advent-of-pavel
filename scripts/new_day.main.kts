#!/usr/bin/env kotlin

import java.io.InputStream
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.outputStream
import kotlin.io.path.readLines
import kotlin.io.path.writeLines
import kotlin.io.path.writeText

fun String.isPositiveInt(): Boolean = toIntOrNull().let { it != null && it > 0 }

fun parseArgs(): Triple<String, String, Boolean> {
    val part2Arg = "--part2"
    require(args.size in 2..3) { "Expecting arguments: <year> <day> ($part2Arg)" }

    val year = args[0]
    val day = args[1]

    val third = args.getOrNull(2)
    val part2 = third == part2Arg

    require(year.length == 4 && year.isPositiveInt()) { "Expecting year arg in YYYY format" }
    require(day.length == 2 && day.isPositiveInt()) { "Expecting day arg in DD format" }
    require(third == null || part2) { "Unexpected arg: $third" }

    return Triple(year, day, part2)
}

fun createModulePath(year: String): Path = Path("year${year}").also {
    check(it.isDirectory()) { "Module for year $year does not exist yet" }
}

fun createKotlinSrcFileContent(year: String, day: String): String = """
    package cz.veleto.aoc.year$year

    import cz.veleto.aoc.core.AocDay

    class Day$day(override val config: Year${year}Config) : AocDay(config) {

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

fun createKotlinTestFunction(year: String, day: String, part: Int, exampleInputSuffix: String, answer: String) = """
    @Test
    fun testPart$part$exampleInputSuffix() {
        val task = Day$day(Year${year}Config("Day${day}_test$exampleInputSuffix"))
        assertEquals("$answer", task.part$part())
    }
""".trimIndent()

fun createKotlinTestFunctions(year: String, day: String, answers: List<Pair<String?, String?>>): String = buildString {
    answers.forEachIndexed { inputIndex, inputAnswers ->
        inputAnswers.toList().forEachIndexed { partIndex, answer ->
            if (answer != null) {
                val suffix = if (answers.size > 1) "_${inputIndex + 1}" else ""
                appendLine()
                appendLine(
                    createKotlinTestFunction(
                        year = year,
                        day = day,
                        part = partIndex + 1,
                        exampleInputSuffix = suffix,
                        answer = answer,
                    ).prependIndent()
                )
            }
        }
    }
}

fun createKotlinTestFileContent(year: String, day: String, answers: List<Pair<String?, String?>>): String = """
    package cz.veleto.aoc.year$year

    import cz.veleto.aoc.core.AocDay
    import kotlin.test.Test
    import kotlin.test.assertEquals

    class Day${day}Test {
    %s}

""".trimIndent().format(createKotlinTestFunctions(year, day, answers))

fun Path.createFile(text: String): Path = apply {
    writeText(
        text = text,
        charset = Charsets.UTF_8,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE,
    )
}

val basePackagePath = Path("cz", "veleto", "aoc")

fun createKotlinFilePath(
    modulePath: Path,
    year: String,
    day: String,
    type: String,
    fileSuffix: String = "",
): Path {
    val packagePath = basePackagePath.resolve("year$year")
    return modulePath.resolve(type).resolve(packagePath).resolve("Day$day$fileSuffix.kt")
}

fun createKotlinSrcFile(
    modulePath: Path,
    year: String,
    day: String,
): Path = createKotlinFilePath(modulePath, year, day, "src")
    .createParentDirectories()
    .createFile(createKotlinSrcFileContent(year, day))

fun createKotlinTestFile(
    modulePath: Path,
    year: String,
    day: String,
    answers: List<Pair<String?, String?>>,
): Path = createKotlinFilePath(modulePath, year, day, "test", fileSuffix = "Test")
    .createParentDirectories()
    .createFile(createKotlinTestFileContent(year, day, answers))

fun createInputFilePath(modulePath: Path, day: String, fileSuffix: String = ""): Path =
    modulePath.resolve("inputs").resolve("Day$day$fileSuffix.txt")

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

fun fetchSeriousInput(modulePath: Path, year: String, day: String): Path {
    val seriousInputFile = createInputFilePath(modulePath, day)
    seriousInputFile.createParentDirectories()
    fetchInput(year, day).copyTo(seriousInputFile.outputStream())
    return seriousInputFile
}

// parsing Python print output, lol, don't judge, yolo
fun fetchExampleInputs(modulePath: Path, year: String, day: String): List<Triple<Path, String?, String?>> {
    fun execPython(toPrint: String): Process = listOf(
        "python",
        "-c",
        "from aocd.models import Puzzle; e = Puzzle(year=$year, day=${day.toInt()})._get_examples(); print($toPrint)",
    ).toTypedArray().awaitProcess()

    fun Process.readSingleLine(): String = inputStream.reader().readLines().single()

    val exampleCount = execPython("len(e)").readSingleLine().toInt()

    return List(exampleCount) { exampleIndex ->
        val fileSuffixSuffix = if (exampleCount > 1) "_${exampleIndex + 1}" else ""
        val filePath = createInputFilePath(modulePath, day, fileSuffix = "_test$fileSuffixSuffix")
        filePath.createParentDirectories()

        fun fetchAnswer(type: String): String? =
            execPython("e[$exampleIndex].answer_$type").readSingleLine().takeIf { it != "None" }

        execPython("e[$exampleIndex].input_data").inputStream.copyTo(filePath.outputStream())

        Triple(filePath, fetchAnswer("a"), fetchAnswer("b"))
    }
}

fun addFilesToGit(filePaths: List<Path>) {
    val git: Process = (arrayOf("git", "add") + filePaths.map(Path::toString).toTypedArray()).awaitProcess()
    check(git.exitValue() == 0) { "Adding files to git failed" }
}

fun addImplementationToBuilder(modulePath: Path, year: String, day: String) {
    val path = modulePath.resolve("src").resolve(basePackagePath).resolve("year$year").resolve("CreateDayCommon.kt")
    val lines = path.readLines()
    val elseCaseIndex = lines.indexOfFirst { "else ->" in it }
    val newLines = lines.toMutableList().apply {
        val newLine = """${day.toInt()} -> Day$day(mainConfig.copy(inputName = "Day$day"))"""
        add(elseCaseIndex, newLine.prependIndent())
    }
    path.writeLines(newLines)
}

fun initializeDay(year: String, day: String) {
    println("New day, new elf adventure!")

    val modulePath = createModulePath(year)

    println("Fetching input data...")
    val exampleInputs = fetchExampleInputs(modulePath, year, day)
    val seriousInputFile = fetchSeriousInput(modulePath, year, day)
    val inputFiles = exampleInputs.map { it.first } + listOf(seriousInputFile)

    println("Creating Kotlin files...")
    val kotlinSrcFile = createKotlinSrcFile(modulePath, year, day)
    val kotlinTestFile = createKotlinTestFile(modulePath, year, day, exampleInputs.map { it.second to it.third })

    val createdFiles = listOf(kotlinSrcFile, kotlinTestFile) + inputFiles

    println("Adding files to git...")
    addFilesToGit(createdFiles)

    println("Adding implementation to the builder...")
    addImplementationToBuilder(modulePath, year, day)

    println("Created files:")
    createdFiles.forEach { println("\t$it") }

    println("Have fun!")
}

fun continueDay(year: String, day: String) {
    println("The elf adventure continues, stay strong!")

    val modulePath = createModulePath(year)

    println("Fetching input data...")
    val exampleInputs = fetchExampleInputs(modulePath, year, day)
    val seriousInputFile = fetchSeriousInput(modulePath, year, day)
    val inputFiles = exampleInputs.map { it.first } + listOf(seriousInputFile)

    println("Creating Kotlin files...")
    val kotlinTestFile = createKotlinTestFile(modulePath, year, day, exampleInputs.map { it.second to it.third })

    val createdFiles = listOf(kotlinTestFile) + inputFiles

    println("Adding files to git...")
    addFilesToGit(createdFiles)

    println("Created files:")
    createdFiles.forEach { println("\t$it") }

    println("Have fun!")
}

fun main() {
    val (year, day, part2) = parseArgs()
    when (part2) {
        false -> initializeDay(year = year, day = day)
        true -> continueDay(year = year, day = day)
    }
}

main()
