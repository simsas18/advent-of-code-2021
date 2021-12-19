data class Display(val patterns: List<Set<Char>>, val output: List<Set<Char>>)

enum class SegmentDigit(val value: Int, val segments: Set<Char>) {
    ZERO(0, setOf('a', 'b', 'c', 'e', 'f', 'g')),
    ONE(1, setOf('c', 'f')),
    TWO(2, setOf('a', 'c', 'd', 'e', 'g')),
    THREE(3, setOf('a', 'c', 'd', 'f', 'g')),
    FOUR(4, setOf('b', 'c', 'd', 'f')),
    FIVE(5, setOf('a', 'b', 'd', 'f', 'g')),
    SIX(6, setOf('a', 'b', 'd', 'e', 'f', 'g')),
    SEVEN(7, setOf('a', 'c', 'f')),
    EIGHT(8, setOf('a', 'b', 'c', 'd', 'e', 'f', 'g')),
    NINE(9, setOf('a', 'b', 'c', 'd', 'f', 'g'));

    companion object {
        private val stringMap = values().associateBy { it.segments.sorted().joinToString("") }

        private val segmentCount = values().fold(mutableListOf<Char>()) { acc, segmentDigit ->
            acc.addAll(segmentDigit.segments)
            return@fold acc
        }.groupingBy { it }.eachCount()

        val digitsByCount = values().groupBy { it.segments.size }
        val segmentsByCount = segmentCount.entries.groupBy(keySelector = { it.value }, valueTransform = { it.key })
            .mapValues { it.value.toSet() }

        fun fromSet(segments: Set<Char>): SegmentDigit? {
            val pattern = segments.sorted().joinToString("")
            return stringMap[pattern]
        }
    }
}

fun main() {

    fun parseDisplaysSet(input: List<String>): List<Display> {
        return input.map { it.split(" | ") }
            .map { part ->
                Display(part[0].split(" ").map { it.toCharArray().toSet() },
                    part[1].split(" ").map { it.toCharArray().toSet() })
            }
    }

    fun part1(input: List<String>): Int {
        val displays = parseDisplaysSet(input)
        return displays.sumOf { display -> display.output.count { it.size in listOf(2, 3, 4, 7) } }
    }

    fun decodeSegmentMapping(display: Display): Map<Char, Char> {
        val segmentMapping = mutableMapOf<Char, Set<Char>>() // Actual segment (a-g) to possible correct segments

        // Possible segments based on how many segments are shown
        for (pattern in display.patterns) {
            val possibleDigits = SegmentDigit.digitsByCount.getValue(pattern.size)
            val possibleSegments = possibleDigits.fold(mutableSetOf<Char>()) { acc, segmentDigit ->
                acc.addAll(segmentDigit.segments)
                return@fold acc
            }
            for (segment in pattern) {
                val segmentsMap = segmentMapping[segment] ?: possibleSegments
                segmentMapping[segment] = segmentsMap.intersect(possibleSegments)
            }
        }

        // Possible segments based on how many times it is shown in 0-9
        val countBySegment = display.patterns.fold(mutableListOf<Char>()) { acc, chars ->
            acc.addAll(chars)
            return@fold acc
        }.groupingBy { it }.eachCount()
        for ((segment, count) in countBySegment) {
            val possibleSegments = SegmentDigit.segmentsByCount.getValue(count)
            val segmentsMap = segmentMapping.getValue(segment)
            segmentMapping[segment] = segmentsMap.intersect(possibleSegments)
        }

        // If segment mapping is known, remove those segments from other segments' possibilities
        val knownSegments = segmentMapping.filterValues { it.size == 1 }.flatMap { it.value }.toSet()
        for ((fromChar, toChars) in segmentMapping) {
            if (toChars.size != 1) {
                segmentMapping[fromChar] = toChars.subtract(knownSegments)
            }
        }

        return segmentMapping.mapValues { it.value.single() }
    }

    fun part2(input: List<String>): Int {
        val displays = parseDisplaysSet(input)

        var sum = 0
        for (display in displays) {
            var numberStr = ""
            val mapping = decodeSegmentMapping(display)
            for (outputPattern in display.output) {
                val decodedPattern = outputPattern.map { mapping.getValue(it) }.toSet()
                val digit = SegmentDigit.fromSet(decodedPattern)!!
                numberStr += digit.value
            }
            sum += numberStr.toInt()
        }

        return sum
    }

    val testInput = readInput("Day08_test")
    val input = readInput("Day08")

    val part1Test = part1(testInput)
    println("Test answer part1 = $part1Test")
    check(part1Test == 26)
    println("Answer part1 = ${part1(input)}")

    val part2Test = part2(testInput)
    println("Test answer part2 = $part2Test")
    check(part2Test == 61229)
    println("Answer part2 = ${part2(input)}")
}
