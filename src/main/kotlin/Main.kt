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

            "jmp" -> {
                val target = parts[1].toInt()
                programCounter = target
            }

            "split" -> {
                val targets = mutableListOf<Int>()
                var counter = 0
                for (i in 1 until parts.size - 1) {
                    targets.add(parts[i].split(",")[0].toInt())
                }
                targets.add(parts[parts.size - 1].toInt())
                for (i in 0 until targets.size - 1) {
                    if (inputIndex < input.length &&
                        (input[inputIndex].toString() == instructions[targets[i]].split(" ")[1] )

                    ) {
                        programCounter = targets[i]
                        counter++
                        break
                    }
                }
                if (counter == 0 && (inputIndex >= input.length ||
                            instructions[targets[targets.size - 1]] == "match" ||
                            input[inputIndex].toString() == instructions[targets[targets.size - 1]].split(" ")[1] ||
                            instructions[targets[targets.size - 1]].split(" ")[0] == "split" ||
                            instructions[targets[targets.size - 1]].split(" ")[0] == "jmp"
                            )
                ) {
                    programCounter = targets[targets.size - 1]
                    counter++
                }
                for (i in 0 until targets.size - 1) {
                    if (inputIndex < input.length &&
                        (input[inputIndex].toString() == instructions[targets[i]].split(" ")[1] ||
                                instructions[targets[i]].split(" ")[0] == "split") ||
                                instructions[targets[i]].split(" ")[0] == "jmp"

                    ) {
                        programCounter = targets[i]
                        counter++
                        break
                    }
                }
                if (counter == 0) {
                    return false
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


fun main() {
    val regex = "a(bc)+"
    val generator = Generator(regex)
    val instructions = generator.generateInstructions(regex)

    println("Инструкции для регулярного выражения \"$regex\":")
    instructions.forEachIndexed { index, instruction ->
        println("$index $instruction")
    }

    val input = "abcbcbcbcbc"
    val isMatch = executeInstructions(instructions, input)
    println("Результат сравнения: $isMatch")
}