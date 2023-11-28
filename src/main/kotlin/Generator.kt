class Generator(regex: String) {
    private val alphabet = selectAlphabet(regex)
    private val instructions = mutableListOf<String>()
    private var pipeCount = mutableListOf(0)
    private var parenthesisCount = mutableListOf(0)
    private var prevISParenthesis = false
    private var pipeVisible = mutableListOf(pipeCount)


    private fun selectAlphabet(regex: String): MutableSet<Char> { //все, что не операторы, становится буквой
        val operands = setOf('+', '?', '|', '*', '(', ')')
        val alphabet = mutableSetOf<Char>()
        for (char in regex) {
            if (!operands.contains(char)) {
                alphabet.add(char)
            }
        }
        return alphabet
    }

    private fun addInstruction(instruction: String) {
        instructions.add(instruction)
    }

    private fun checkPipeCount(target: Int) { //изменения индексов jmp в строках
        val j = pipeVisible.size - 1
        if (pipeVisible[j][0] != 0) {
            for (i in 2 until pipeVisible[j].size) {
                instructions[pipeVisible[j][i]] = "jmp $target"
            }
        }
    }

    private fun plusManip() { //манипуляции, когда в регулярном выражении встречается +
        val target1: Int
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

    private fun questionManip() { //манипуляции, когда в регулярном выражении встречается ?
        val target1: Int
        val target2 = instructions.size + 1
        if (!prevISParenthesis) {
            target1 = instructions.size
            addInstruction("split $target1, $target2")
            instructions[target1] = instructions[target1 - 1]
        } else {
            target1 = parenthesisCount[parenthesisCount.size - 1] + 1
            addInstruction("split $target1, $target2")
            replaceInstructions()
            parenthesisCount.removeAt(parenthesisCount.size - 1)

        }
        instructions[target1 - 1] = "split $target1, $target2"
        checkPipeCount(target2)
    }

    private fun replaceInstructions() { //циклически перемещает инструкции (увеличивая на 1) для вставки других сверху
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

    private fun multManip() { //манипуляции, когда в регулярном выражении встречается *
        val target1: Int
        val target2 = instructions.size + 2
        val prevTarget: Int
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
            parenthesisCount.removeAt(parenthesisCount.size - 1)

        }
        addInstruction("jmp $prevTarget")
        checkPipeCount(target2)

    }

    private fun pipeManip() {  //манипуляции, когда в регулярном выражении встречается  |
        if (!prevISParenthesis) {
            val j = pipeVisible.size - 1
            if (pipeVisible[j][0] == 0) {
                val target1 = instructions.size
                val target2 = instructions.size + 2
                addInstruction("split $target1, $target2")
                replaceInstructions()
                instructions[target1 - 1] = "split $target1, $target2"
                val nextTarget = target2 + 1
                addInstruction("jmp $nextTarget")
                pipeVisible[j].add(target1 - 1)
                pipeVisible[j].add(instructions.size - 1)

            } else {
                val target1 = instructions.size + 1
                val target2 = instructions.size + 2
                instructions[pipeVisible[j][1]] += ", $target1"
                for (i in 2 until pipeVisible[j].size) {
                    instructions[pipeVisible[j][i]] = "jmp $target2"
                }
                instructions.add("jmp $target2")
                pipeVisible[j].add(instructions.size - 1)
            }
        } else {
            val j = pipeVisible.size - 1
            if (pipeVisible[j][0] == 0) {
                val target1 = parenthesisCount[parenthesisCount.size - 1] + 1
                val target2 = instructions.size + 2
                addInstruction("split $target1, $target2")
                replaceInstructions()
                instructions[target1 - 1] = "split $target1, $target2"
                val nextTarget = target2 + 1
                addInstruction("jmp $nextTarget")
                pipeVisible[j].add(target1 - 1)
                pipeVisible[j].add(instructions.size - 1)

            } else {

                val target1 = instructions.size + 1
                val target2 = instructions.size + 2
                instructions[pipeVisible[j][1]] += ", $target1"
                for (i in 2 until pipeVisible[j].size) {
                    instructions[pipeVisible[j][i]] = "jmp $target2"
                }
                instructions.add("jmp $target2")
                pipeVisible[j].add(instructions.size - 1)
            }
        }

    }

    fun generateInstructions(regex: String): List<String> { //основная функция
        var current = 0
        while (current < regex.length) {
            when (val char = regex[current]) {
                in alphabet -> {
                    addInstruction("char $char")
                    if (prevISParenthesis)
                        parenthesisCount.removeAt(parenthesisCount.size - 1)
                    prevISParenthesis = false
                    current++
                }
                '(' -> {
                    val curPipeCount = mutableListOf(0)
                    pipeVisible.add(curPipeCount)
                    if (prevISParenthesis)
                        parenthesisCount.removeAt(parenthesisCount.size - 1)
                    parenthesisCount.add(instructions.size)
                    prevISParenthesis = false
                    current++
                }
                ')' -> {
                    pipeVisible.removeAt(pipeVisible.size - 1)
                    if (prevISParenthesis)
                        parenthesisCount.removeAt(parenthesisCount.size - 1)
                    prevISParenthesis = true
                    current++
                }
                '|' -> {
                    pipeManip()
                    prevISParenthesis = false
                    pipeVisible[pipeVisible.size - 1][0]++
                    current++
                }
                '?' -> {
                    questionManip()
                    prevISParenthesis = false
                    current++
                }
                '+' -> {
                    plusManip()
                    prevISParenthesis = false
                    current++
                }
                '*' -> {
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