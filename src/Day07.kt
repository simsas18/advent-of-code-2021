import kotlin.math.abs

fun main() {

    fun calcMedian(numbers: List<Int>): Int {
        val sortedList = numbers.sorted()
        val middleIndex = Math.floorDiv(numbers.size, 2)
        return if (numbers.size % 2 != 0) sortedList[middleIndex + 1] else (sortedList[middleIndex - 1] + sortedList[middleIndex]) / 2
    }

    fun part1(input: List<String>): Int {
        val positions = input.first().split(",").map { it.toInt() }
        val median = calcMedian(positions)

        return positions.sumOf { abs(median - it) }
    }

    fun calcDistance(position: Int, targetPosition: Int): Int {
        val firstCost = 1
        val lastCost = abs(targetPosition - position)
        return (firstCost + lastCost) * lastCost / 2
    }

    fun calcDistance(positions: List<Int>, targetPosition: Int): Int {
        return positions.sumOf { calcDistance(it, targetPosition) }
    }

    fun part2(input: List<String>): Int {
        val positions = input.first().split(",").map { it.toInt() }.sorted()
        var currentTarget = positions.first()

        var prevCost = Int.MAX_VALUE
        while (currentTarget <= positions.last()) {
            val cost = calcDistance(positions, currentTarget)

            if (cost < prevCost) {
                prevCost = cost
                currentTarget++
            } else {
                return prevCost
            }
        }

        return 0
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println("Answer part1 = ${part1(input)}")
    println("Answer part2 = ${part2(input)}")
}
