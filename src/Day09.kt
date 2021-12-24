data class HeightCell(val x: Int, val y: Int, val height: Int, var basin: Int?) {
    constructor(x: Int, y: Int, height: Int) : this(x, y, height, basin = null)
}

data class Heightmap(val heights: List<List<HeightCell>>) {

    fun getLowPointRisk(): Int {
        var risk = 0
        for ((y, row) in heights.withIndex()) {
            for ((x, cell) in row.withIndex()) {
                val adjacentHeights = adjacentHeights(x, y)
                if (adjacentHeights.all { it.height > cell.height }) {
                    risk += cell.height + 1
                }
            }
        }
        return risk
    }

    fun mapBasins(): Int {
        var basin = 0
        for ((y, row) in heights.withIndex()) {
            for ((x, cell) in row.withIndex()) {
                if (cell.height == MAX_HEIGHT || cell.basin != null) {
                    continue
                }
                mapBasin(x, y, basin)
                basin++
            }
        }

        return heights.flatten().filter { it.basin != null }
            .map { it.basin!! }
            .groupBy { it }
            .mapValues { it.value.size }.values.sortedDescending()
            .take(3)
            .fold(1) { acc, i ->
                acc * i
            }
    }

    private fun mapBasin(x: Int, y: Int, basin: Int) {
        heights[y][x].basin = basin
        adjacentHeights(x, y).filter { it.height < MAX_HEIGHT && it.basin == null }
            .forEach { mapBasin(it.x, it.y, basin) }
    }

    private fun adjacentHeights(x: Int, y: Int): List<HeightCell> {
        val adjacentHeights = mutableListOf<HeightCell>()

        if (x > 0) {
            adjacentHeights.add(heights[y][x - 1])
        }
        if (x < heights.first().size - 1) {
            adjacentHeights.add(heights[y][x + 1])
        }
        if (y > 0) {
            adjacentHeights.add(heights[y - 1][x])
        }
        if (y < heights.size - 1) {
            adjacentHeights.add(heights[y + 1][x])
        }

        return adjacentHeights
    }

    companion object {
        private const val MAX_HEIGHT = 9

        fun fromStringList(input: List<String>): Heightmap {
            val heightmap = mutableListOf<List<HeightCell>>()
            val heights = input.map { row -> row.chunked(1).map { it.toInt() } }
            for ((y, row) in heights.withIndex()) {
                val heightRow = mutableListOf<HeightCell>()
                for ((x, height) in row.withIndex()) {
                    heightRow.add(HeightCell(x, y, height))
                }
                heightmap.add(heightRow)
            }
            return Heightmap(heightmap)
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val heightmap = Heightmap.fromStringList(input)
        return heightmap.getLowPointRisk()
    }

    fun part2(input: List<String>): Int {
        val heightmap = Heightmap.fromStringList(input)
        return heightmap.mapBasins()
    }

    val testInput = readInput("Day09_test")
    val input = readInput("Day09")

    val part1Test = part1(testInput)
    println("Test answer part1 = $part1Test")
    check(part1Test == 15)
    println("Answer part1 = ${part1(input)}")

    val part2Test = part2(testInput)
    println("Test answer part2 = $part2Test")
    check(part2Test == 1134)
    println("Answer part2 = ${part2(input)}")
}
