package cz.veleto.aoc.core

fun runSingleDay(day: Int, taskBuilder: (day: Int) -> AocDay) {
    taskBuilder(day).run()
}

fun runAllDays(taskBuilder: (day: Int) -> AocDay, vararg skippedDays: Int) {
    (1..25).filter { it !in skippedDays }.forEach { day ->
        runSingleDay(day, taskBuilder)
    }
}

private fun AocDay.run() {
    println("\nRunning ${this::class.qualifiedName}")
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
    println()
}
