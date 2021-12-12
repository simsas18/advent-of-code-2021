import kotlin.math.sign

data class Vent(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

fun main() {

    fun parseInput(input: List<String>): List<Vent> {
        val vents = mutableListOf<Vent>()
        for (inputRow in input) {
            val coordinates = inputRow.split(" -> ").map { it.split(",") }

            vents.add(
                Vent(
                    x1 = coordinates[0][0].toInt(),
                    y1 = coordinates[0][1].toInt(),
                    x2 = coordinates[1][0].toInt(),
                    y2 = coordinates[1][1].toInt()
                )
            )
        }
        return vents
    }

    fun mapVents(vents: List<Vent>, includeDiagonals: Boolean): MutableMap<Int, MutableMap<Int, Int>> {
        val ventMap = mutableMapOf<Int, MutableMap<Int, Int>>() // map<X, map<Y, count>>

        for (vent in vents) {
            if (vent.x1 == vent.x2) {
                if (vent.y1 <= vent.y2) {
                    for (yCord in vent.y1..vent.y2) {
                        ventMap.getOrPut(vent.x1) { mutableMapOf() }[yCord] =
                            ventMap.getValue(vent.x1).getOrPut(yCord) { 0 } + 1
                    }
                } else {
                    for (yCord in vent.y2..vent.y1) {
                        ventMap.getOrPut(vent.x1) { mutableMapOf() }[yCord] =
                            ventMap.getValue(vent.x1).getOrPut(yCord) { 0 } + 1
                    }
                }
            } else if (vent.y1 == vent.y2) {
                if (vent.x1 <= vent.x2) {
                    for (xCord in vent.x1..vent.x2) {
                        ventMap.getOrPut(xCord) { mutableMapOf() }[vent.y1] =
                            ventMap.getValue(xCord).getOrPut(vent.y1) { 0 } + 1
                    }
                } else {
                    for (xCord in vent.x2..vent.x1) {
                        ventMap.getOrPut(xCord) { mutableMapOf() }[vent.y1] =
                            ventMap.getValue(xCord).getOrPut(vent.y1) { 0 } + 1
                    }
                }
            } else if (includeDiagonals) {
                val xStart: Int
                val xStep: Int
                val yMin: Int
                val yMax: Int
                if (vent.y1 <= vent.y2) {
                    xStart = vent.x1
                    xStep = (vent.x2 - vent.x1).sign
                    yMin = vent.y1
                    yMax = vent.y2
                } else {
                    xStart = vent.x2
                    xStep = (vent.x1 - vent.x2).sign
                    yMin = vent.y2
                    yMax = vent.y1
                }

                var xCord = xStart
                for (yCord in yMin..yMax) {
                    ventMap.getOrPut(xCord) { mutableMapOf() }[yCord] = ventMap.getValue(xCord).getOrPut(yCord) { 0 } + 1
                    xCord += xStep
                }
            }
        }
        return ventMap
    }

    fun part1(input: List<String>): Int {
        val vents = parseInput(input)
        val ventMap = mapVents(vents, includeDiagonals = false)

        return ventMap.values.sumOf { column -> column.values.count { it > 1 } }
    }

    fun part2(input: List<String>): Int {
        val vents = parseInput(input)
        val ventMap = mapVents(vents, includeDiagonals = true)

        return ventMap.values.sumOf { column -> column.values.count { it > 1 } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
