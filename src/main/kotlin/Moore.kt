import java.io.File

class DFA(
    var states: Set<Int>,
    private var alphabetSize: Int,
    private val m: Int,
    var initialState: Int,
    var finalStates: Set<Int>,
    var transitions: MutableMap<Pair<Int, Int>, Int>
) {
    // Функция для проверки строки на принятие автоматом
    fun isAccepted(input: String): Boolean {
        var currentState = initialState
        for (symbol in input) {
            val transitionKey = Pair(currentState, symbol.toString().toInt())
            val nextState = transitions[transitionKey] ?: return false
            currentState = nextState
        }
        return finalStates.contains(currentState)
    }

    // Функция для вывода автомата в файл
    fun exportToFile(filename: String) {
        val file = File(filename)
        val writer = file.bufferedWriter()

        writer.write(states.size.toString() + "\n")
        writer.write(m.toString() + "\n")
        writer.write(initialState.toString() + "\n")

        val finalStatesStrings = finalStates.joinToString(" ")
        writer.write(finalStatesStrings + "\n")

        for ((key, value) in transitions) {
            val (fromState, symbol) = key
            writer.write("$fromState $symbol $value \n")
        }

        writer.close()
    }

    fun minimize() {
        // Создаем карту для отметки неэквивалентных пар состояний.
        val marked = mutableMapOf<Pair<Int, Int>, Boolean>()
        // Инициализируем карту, обозначая пары с одним финальным и одним нефинальным состоянием как неэквивалентные.
        for (p in states) {
            for (q in states) {
                if (p != q) {
                    marked[Pair(p, q)] = finalStates.contains(p) != finalStates.contains(q)
                }
            }
        }

        // Продолжаем процесс до тех пор, пока будут выявляться новые неэквивалентные пары.
        var hasChange = true
        while(hasChange) {
            hasChange = false
            // Проверяем для каждой пары состояний
            for (p in states) {
                for (q in states) {
                    // Если они не отмечены как неэквивалентные
                    if (p != q && marked[Pair(p, q)] == false) {
                        for (a in 0 until alphabetSize) {
                            // Проверяем переходы для каждого символа
                            val pTransition = transitions[Pair(p, a)]
                            val qTransition = transitions[Pair(q, a)]
                            // Если переходы ведут в неэквивалентные состояния, отмечаем исходную пару как неэквивалентную
                            if (pTransition != null && qTransition != null && marked[Pair(pTransition, qTransition)] == true) {
                                marked[Pair(p, q)] = true
                                hasChange = true
                                break
                            }
                        }
                    }
                }
            }
        }

        // Вторая часть: перестраиваем автомат
        val equivalenceClasses = mutableMapOf<Int, MutableSet<Int>>()
        // Создаем классы эквивалентности для неотмеченных пар состояний
        for (p in states) {
            for (q in states) {
                if (p <= q && marked[Pair(p, q)] == false) {
                    equivalenceClasses.getOrPut(p) { mutableSetOf() }.add(q)
                }
            }
        }

        // Создаем новое множество состояний на основе классов эквивалентности
        val newStates = equivalenceClasses.keys
        val newInitialState = equivalenceClasses.keys.first { it == initialState }
        val newFinalStates = finalStates.intersect(newStates)

        // Перестраиваем переходы с учетом новых состояний
        val newTransitions = mutableMapOf<Pair<Int, Int>, Int>()
        for ((key, value) in transitions) {
            val newFromState = equivalenceClasses.keys.firstOrNull { equivalenceClasses[it]?.contains(key.first) == true }
            val newToState = equivalenceClasses.keys.firstOrNull { equivalenceClasses[it]?.contains(value) == true }
            if (newFromState != null && newToState != null) {
                newTransitions[Pair(newFromState, key.second)] = newToState
            }
        }
        // Обновляем наш автомат на основе новых данных
        states = newStates
        initialState = newInitialState
        finalStates = newFinalStates
        transitions = newTransitions
    }

}

fun main() {
    // Чтение файла с описанием автомата
    val file = File("src/automaton.txt")
    val lines = file.readLines()
    val n = lines[0].toInt()
    val m = lines[1].toInt()
    val alphabet = mutableSetOf<Int>()

    val initialState = lines[2].split(" ").map { it.toInt() }.first()
    val finalState = lines[3].split(" ").map { it.toInt() }.toSet()

    // Инициализация переходов
    val transitions = mutableMapOf<Pair<Int, Int>, Int>()

    for (i in 4 until lines.size) {
        val transitionInfo = lines[i].split(" ").map { it.toInt() }
        val fromState = transitionInfo[0]
        val symbol = transitionInfo[1]
        val toState = transitionInfo[2]

        val key = Pair(fromState, symbol)
        transitions[key] = toState
        alphabet.add(symbol)
    }

    val dfa = DFA((0 until n).toSet(), alphabet.size, m, initialState, finalState, transitions)
    dfa.minimize()

    // Вывод DFA в файл
    dfa.exportToFile("src/dfa_output.txt")
}
