fun executeInstructions(instructions: List<String>, input: String): Boolean {
    var programCounter = 0
    var inputIndex = 0

    while (programCounter < instructions.size) {
        val instruction = instructions[programCounter]
        val parts = instruction.split(" ")

        when (parts[0]) {
            "char" -> {
                val charToMatch = parts[1].single()
                if (inputIndex < input.length && input[inputIndex] == charToMatch) {
                    inputIndex++
                    programCounter++
                } else {
                    return false
                }
            }
            "split" -> {
                val target1 = parts[1].split(",")[0].toInt()
                val target2 = parts[2].toInt()
                if (inputIndex < input.length) {
                    programCounter = target1
                } else {
                    programCounter = target2
                }
            }
            "match" -> {
                return inputIndex == input.length
            }
            else -> throw IllegalArgumentException("Неподдерживаемая инструкция: $instruction")
        }
    }

    return false
}
fun generateInstructions(regex: String, alphabet: Set<Char>): List<String> {
    val instructions = mutableListOf<String>()

    fun addInstruction(instruction: String) {
        instructions.add(instruction)
    }

    var current = 0
    while (current < regex.length) {
        val char = regex[current]
        when {
            char in alphabet -> {
                addInstruction("char $char")
                current++
            }
            char == '|' -> {
                val target1 = instructions.size + 2
                val target2 = current + 1
                addInstruction("split $target1, $target2")
                val nextTarget = target2 + 1
                addInstruction("jmp $nextTarget")
                current++
            }
            char == '?' -> {
                val target1 = instructions.size + 1
                val target2 = current + 1
                addInstruction("split $target1, $target2")
                current++
            }
            char == '+' -> {
                val target1 = instructions.size + 2
                val target2 = current + 1
                addInstruction("split $target1, $target2")
                val loopTarget = instructions.size - 2
                instructions[instructions.size - 1] = "split $loopTarget, ${current + 1}"
                current++
            }
            char == '*' -> {
                val target1 = instructions.size - 1
                val target2 = current + 1
                addInstruction("split $target1, $target2")
                val loopTarget = instructions.size + 1
                instructions[instructions.size - 1] = "split $loopTarget, ${current + 1}"
                val prevTarget = instructions.size - 1
                addInstruction("jmp $prevTarget")
                current++

            }
            else -> {
                throw IllegalArgumentException("Неподдерживаемый символ в регулярном выражении: $char")
            }
        }
    }

    addInstruction("match")
    return instructions
}

fun main() {
    val regex = "a*b+"
    val alphabet = setOf('a', 'b', 'c')
    val instructions = generateInstructions(regex, alphabet)

    println("Инструкции для регулярного выражения \"$regex\":")
    instructions.forEachIndexed { index, instruction ->
        println("$index $instruction")
    }

    val input = "aabbb"
    val isMatch = executeInstructions(instructions, input)
    println("Результат сравнения: $isMatch")
}