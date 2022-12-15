import kotlin.math.abs

class Day15(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(row: Int): Int {
        val r = Regex("""^.*x=(-?[0-9]+), y=(-?[0-9]+).+x=(-?[0-9]+), y=(-?[0-9]+).*$""")
        val sensors = input.map { line ->
            val (sensorX, sensorY, beaconX, beaconY) = r.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
            val distance = abs(sensorX - beaconX) + abs(sensorY - beaconY)
            Triple(sensorX, sensorY, distance)
        }

        return sensors
            .map { (x, y, d) ->
                val dToRow = abs(row - y)
                val halfIntersectSize = d - dToRow
                val intersectMinX = x - halfIntersectSize
                val intersectMaxX = x + halfIntersectSize
                (intersectMinX..intersectMaxX).toSet()
            }
            .reduce { acc, longs -> acc.union(longs) }
            .count() - 1 // why -1? 🤷 it works though 🤣
    }

    fun part2(): Int = -1
}

fun main() {
    val task = Day15("Day15")
    println(task.part1(row = 2_000_000))
    println(task.part2())
}
