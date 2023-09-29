import java.io.File

class NFA(
    private val n: Int,
    val m: Int,
    val initialState: Set<Int>,
    val finalState: Set<Int>,
    val transitions: MutableMap<Pair<Int, Int>, Set<Int>>
) {
    // Функция для проверки строки на принятие автоматом
    fun isAccepted(input: String): Boolean {
        var currentStates = initialState
        for (symbol in input) {
            val newStates = mutableSetOf<Int>()
            for (state in currentStates) {
                val transitionKey = Pair(state, symbol.toString().toInt())
                val nextStates = transitions[transitionKey] ?: emptySet()
                newStates.addAll(nextStates)
            }
            currentStates = newStates
        }
        return currentStates.intersect(finalState).isNotEmpty()
    }
}

fun main() {
    // Чтение файла с описанием автомата
    val file = File("src/automaton.txt")
    val lines = file.readLines()
    val n = lines[0].toInt()
    val m = lines[1].toInt()

    val initialStateStr = lines[2].split(" ").map { it.toInt() }
    val finalStateStr = lines[3].split(" ").map { it.toInt() }

    // Создание множества начальных и конечных состояний
    val initialState = initialStateStr.toSet()
    val finalState = finalStateStr.toSet()

    // Инициализация переходов
    val transitions = mutableMapOf<Pair<Int, Int>, Set<Int>>()

    for (i in 4 until lines.size) {
        val transitionInfo = lines[i].split(" ").map { it.toInt() }
        val fromState = transitionInfo[0]
        val symbol = transitionInfo[1]
        val toState = transitionInfo[2]

        val key = Pair(fromState, symbol)
        val existingTransitions = transitions.getOrDefault(key, emptySet())
        val newTransitions = existingTransitions + toState
        transitions[key] = newTransitions
    }

    // Создание объекта NFA
    val nfa = NFA(n, m, initialState, finalState, transitions)

    // Тестирование
    val testString = "01"
    val isAcceptedString = nfa.isAccepted(testString)
    println(isAcceptedString)
}