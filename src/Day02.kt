fun main() {
    fun part1(input: List<String>): Int {
        var depth = 0
        var position = 0
        for (step in input) {
            val parts = step.split(" ")
            val direction = parts[0]
            val stepSize = parts[1].toInt()
            when (direction) {
                "forward" -> position += stepSize
                "down" -> depth += stepSize
                "up" -> depth -= stepSize
            }
        }

        return depth * position
    }

    fun part2(input: List<String>): Int {
        var aim = 0
        var depth = 0
        var position = 0
        for (step in input) {
            val parts = step.split(" ")
            val direction = parts[0]
            val stepSize = parts[1].toInt()
            when (direction) {
                "forward" -> {
                    position += stepSize
                    depth += aim * stepSize
                }
                "down" -> aim += stepSize
                "up" -> aim -= stepSize
            }
        }

        return depth * position
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
