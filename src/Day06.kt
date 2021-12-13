fun main() {

    fun calculateFishCount(input: List<String>, dayCount: Int): Long {
        var countByAge = input.first()
            .split(",")
            .map { it.toInt() }
            .groupBy { it }
            .mapValues { it.value.size.toLong() }
            .toMutableMap()

        var currentDay = 1
        while (currentDay <= dayCount) {
            val newCount = countByAge[0]
            if (newCount != null) {
                countByAge.remove(0)
                countByAge[7] = countByAge[7]?.let { it + newCount } ?: newCount
                countByAge[9] = newCount
            }

            countByAge = countByAge.mapKeys { it.key - 1 }.toMutableMap()

            currentDay++
        }

        return countByAge.map { it }.sumOf { it.value }
    }

    fun part1(input: List<String>): Long {
        return calculateFishCount(input, 80)
    }

    fun part2(input: List<String>): Long {
        return calculateFishCount(input, 256)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println("Answer part1 = ${part1(input)}")
    println("Answer part2 = ${part2(input)}")
}
