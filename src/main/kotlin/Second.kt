import java.io.File
import java.util.*
import kotlin.collections.*
import java.util.ArrayDeque


class NFAtoDFA(
    private val nfa: NFA
) {
    // Преобразование NFA в DFA
    fun convertToDFA(): DFA {
        val nfaInitialState = nfa.initialState
        val dfaInitialState = epsilonClosure(nfaInitialState)
        val dfaStates = mutableSetOf(dfaInitialState)
        val dfaTransitions = mutableMapOf<Pair<Set<Int>, Int>, Set<Int>>()
        val dfaFinalStates = mutableSetOf<Set<Int>>()

        val queue = LinkedList<Set<Int>>()
        queue.add(dfaInitialState)

        while (queue.isNotEmpty()) {
            val currentStateSet = queue.poll()

            for (symbol in 0 until nfa.m) {
                val nextStateSet = computeNextStateSet(currentStateSet, symbol)
                if (nextStateSet.isNotEmpty()) {
                    if (!dfaStates.contains(nextStateSet)) {
                        queue.add(nextStateSet)
                        dfaStates.add(nextStateSet)
                    }
                    dfaTransitions[Pair(currentStateSet, symbol)] = nextStateSet
                }
            }

            if (currentStateSet.intersect(nfa.finalState).isNotEmpty()) {
                dfaFinalStates.add(currentStateSet)
            }
        }

        return DFA(dfaStates, nfa.m, dfaInitialState, dfaFinalStates, dfaTransitions)
    }

    private fun epsilonClosure(states: Set<Int>): Set<Int> {
        val epsilonClosureSet = mutableSetOf<Int>()
        val stack = ArrayDeque<Int>(states)

        while (stack.isNotEmpty()) {
            val currentState = stack.pop()
            epsilonClosureSet.add(currentState)

            val epsilonTransitions = nfa.transitions[Pair(currentState, -1)] ?: emptySet()
            for (state in epsilonTransitions) {
                if (!epsilonClosureSet.contains(state)) {
                    stack.push(state)
                }
            }
        }

        return epsilonClosureSet
    }

    private fun computeNextStateSet(currentStateSet: Set<Int>, symbol: Int): Set<Int> {
        val nextStateSet = mutableSetOf<Int>()

        for (state in currentStateSet) {
            val transitionKey = Pair(state, symbol)
            val nextStates = nfa.transitions[transitionKey] ?: emptySet()
            nextStateSet.addAll(nextStates)
        }

        return epsilonClosure(nextStateSet)
    }
}

class DFA(
    private val states: Set<Set<Int>>,
    private val m: Int,
    private val initialState: Set<Int>,
    private val finalStates: Set<Set<Int>>,
    private val transitions: MutableMap<Pair<Set<Int>, Int>, Set<Int>>
) {
    // Функция для проверки строки на принятие автоматом
    fun isAccepted(input: String): Boolean {
        var currentStates = initialState
        for (symbol in input) {
            val transitionKey = Pair(currentStates, symbol.toString().toInt())
            val nextStates = transitions[transitionKey] ?: emptySet()
            currentStates = nextStates
        }
        return finalStates.contains(currentStates)
    }

    // Функция для вывода автомата в файл
    fun exportToFile(filename: String) {
        val file = File(filename)
        val writer = file.bufferedWriter()

        writer.write(states.size.toString() + "\n")
        writer.write(m.toString() + "\n")

        val initialStateString = initialState.joinToString("")
        writer.write(initialStateString + "\n")

        val finalStatesStrings = finalStates.map { it.joinToString("") }
        writer.write(finalStatesStrings.joinToString("\n") + "\n")

        for ((key, value) in transitions) {
            val (fromStates, symbol) = key
            val toState = value.joinToString("")
            writer.write("${fromStates.joinToString("")} $symbol $toState \n")
        }

        writer.close()
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

    // Преобразование NFA в DFA
    val dfaConverter = NFAtoDFA(nfa)
    val dfa = dfaConverter.convertToDFA()

    // Вывод DFA в файл
    dfa.exportToFile("src/dfa_output.txt")
}