

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
                var targets = mutableListOf<Int>()
                var counter = 0
                for (i in 1 until parts.size - 1) {
                    targets.add(parts[i].split(",")[0].toInt())
                }
                targets.add(parts[parts.size - 1].toInt())
                for (i in 0 until targets.size - 1) {
                    if (inputIndex < input.length &&
                        input[inputIndex].toString() == instructions[targets[i]].split(" ")[1]
                    ) {
                        programCounter = targets[i]
                        counter++
                        break
                    }
                }
                if (counter == 0 && (inputIndex >= input.length ||
                            instructions[targets[targets.size - 1]] == "match" ||
                            input[inputIndex].toString() == instructions[targets[targets.size - 1]].split(" ")[1]
                            )
                ) {
                    programCounter = targets[targets.size - 1]
                    counter++
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

class Generator(regex: String) {
    val alphabet = selectAlphabet(regex)
    val instructions = mutableListOf<String>()
    var pipeCount = mutableListOf<Int>(0)
    var parenthesisCount = mutableListOf<Int>(0)
    var prevISParenthesis = false


    fun selectAlphabet(regex: String): MutableSet<Char> {
        val operands = setOf<Char>('+', '?', '|', '*', '(', ')');
        var alphabet = mutableSetOf<Char>();
        for (char in regex) {
            if (!operands.contains(char)) {
                alphabet.add(char);
            }
        }
        return alphabet;
    }

    fun addInstruction(instruction: String) {
        instructions.add(instruction)
    }

    fun checkPipeCount(target: Int){
        if (pipeCount[0] != 0) {
            for (i in 2 until pipeCount.size) {
                instructions[pipeCount[i]] = "jmp $target"
            }
        }
    }

    fun plusManip() {
        var target1 = 0
        val target2 = instructions.size + 1
        if (!prevISParenthesis) {
            target1 = instructions.size - 1
        } else {
            target1 = parenthesisCount[parenthesisCount.size - 1]
            parenthesisCount.removeAt(parenthesisCount.size - 1)
        }
        addInstruction("split $target1, $target2")
        checkPipeCount(target2)
    }
    fun replaceInstructions() {
        for (i in instructions.size - 1 downTo parenthesisCount[parenthesisCount.size - 1] + 1) {
            val parts = instructions[i - 1].split(" ")
            var newInstruction = ""
            if (parts[0] == "split") {
                newInstruction += parts[0] + " "
                newInstruction += (parts[1].split(',')[0].toInt() + 1).toString() + ", "
                newInstruction += (parts[2].split(',')[0].toInt() + 1).toString()
            } else if (parts[0] == "jmp") {
                newInstruction += parts[0] + " "
                newInstruction += (parts[1].split(',')[0].toInt() + 1).toString()
            } else {
                newInstruction = instructions[i - 1]
            }
            instructions[i] = newInstruction
        }
    }

    fun multManip(){
        var target1 = 0
        val target2 = instructions.size + 2
        var prevTarget = 0
        if (!prevISParenthesis) {
            target1 = instructions.size
            addInstruction("split $target1, $target2")
            instructions[target1] = instructions[target1 - 1]
            instructions[target1 - 1] = "split $target1, $target2"

            prevTarget = instructions.size - 2
        } else {
            target1 = parenthesisCount[parenthesisCount.size - 1] + 1
            addInstruction("split $target1, $target2")
            replaceInstructions()
            instructions[target1 - 1] = "split $target1, $target2"

            prevTarget = parenthesisCount[parenthesisCount.size - 1]
        }
        addInstruction("jmp $prevTarget")
        checkPipeCount(target2)

    }

    fun generateInstructions(regex: String): List<String> {
        var current = 0
        while (current < regex.length) {
            val char = regex[current]
            when {
                char in alphabet -> {
                    addInstruction("char $char")
                    prevISParenthesis = false
                    current++
                }

                char == '(' -> {
                    parenthesisCount.add(instructions.size)
                    prevISParenthesis = false
                    current++
                }

                char == ')' -> {
                    prevISParenthesis = true
                    current++
                }

                char == '|' -> {
                    if (pipeCount[0] == 0) {
                        val target1 = instructions.size
                        val target2 = instructions.size + 2
                        addInstruction("split $target1, $target2")
                        instructions[current] = instructions[current - 1]
                        instructions[current - 1] = "split $target1, $target2"
                        val nextTarget = target2 + 1
                        addInstruction("jmp $nextTarget")
                        pipeCount.add(current - 1)
                        pipeCount.add(current + 1)

                    } else {
                        val target1 = instructions.size + 1
                        val target2 = instructions.size + 2
                        instructions[pipeCount[1]] += ", $target1"
                        for (i in 2 until pipeCount.size) {
                            instructions[pipeCount[i]] = "jmp $target2"
                        }
                        instructions.add("jmp $target2")
                        pipeCount.add(instructions.size - 1)
                    }
                    prevISParenthesis = false
                    pipeCount[0]++
                    current++
                }

                char == '?' -> {
                    val target1 = instructions.size
                    val target2 = instructions.size + 1

                    addInstruction("split $target1, $target2")
                    instructions[target1] = instructions[target1 - 1]
                    instructions[target1 - 1] = "split $target1, $target2"
                    if (pipeCount[0] != 0) {
                        for (i in 2 until pipeCount.size) {
                            instructions[pipeCount[i]] = "jmp $target2"
                        }
                    }
                    prevISParenthesis = false
                    current++
                }

                char == '+' -> {
                    plusManip()
                    prevISParenthesis = false
                    current++
                }

                char == '*' -> {
                    multManip()
                    prevISParenthesis = false
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
}

fun main() {
    val regex = "a*(a*b*)*"
    var generator = Generator(regex)
    val instructions = generator.generateInstructions(regex)

    println("Инструкции для регулярного выражения \"$regex\":")
    instructions.forEachIndexed { index, instruction ->
        println("$index $instruction")
    }

    val input = "aabbbb"
    val isMatch = executeInstructions(instructions, input)
    println("Результат сравнения: $isMatch")
}