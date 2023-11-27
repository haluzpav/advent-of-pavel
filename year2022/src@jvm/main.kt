import cz.veleto.aoc.core.runAllDays
import cz.veleto.aoc.core.runSingleDay
import cz.veleto.aoc.year2022.createDayJvm

fun main() {
    runSingleDay(25, ::createDayJvm)
    runAllDays(::createDayJvm)
}
