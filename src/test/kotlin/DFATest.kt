import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class DFATest {
    @Test
    fun testIsAccepted() {
        val states: Set<Int> = setOf(0, 1, 2, 3)
        val m = 2
        val initialState = 0
        val finalStates = setOf(1, 2)
        val transitions: MutableMap<Pair<Int, Int>, Int> = mutableMapOf(
            Pair(0, 0) to 1,
            Pair(0, 1) to 2,
            Pair(1, 0) to 3,
            Pair(1, 1) to 2,
            Pair(2, 0) to 0,
            Pair(2, 1) to 2,
            Pair(3, 0) to 3,
            Pair(3, 1) to 3,
        )
        val dfa = DFA(states, 2, m, initialState, finalStates, transitions)

        assertTrue(dfa.isAccepted("100"))
        assertFalse(dfa.isAccepted("010"))
    }


    @Test
    fun minimization_test() {
        val states = setOf(0, 1, 2, 3, 4, 5)
        val alphabetSize = 2
        val m = 2
        val initialState = 0
        val finalStates = setOf(0, 1)
        val transitions = mutableMapOf(
            Pair(0, 0) to 2,
            Pair(0, 1) to 1,
            Pair(1, 0) to 3,
            Pair(1, 1) to 0,
            Pair(2, 0) to 4,
            Pair(2, 1) to 3,
            Pair(3, 0) to 4,
            Pair(3, 1) to 3,
            Pair(4, 0) to 1,
            Pair(4, 1) to 4,
            Pair(5, 0) to 2,
            Pair(5, 1) to 5
        )

        val dfa = DFA(states, alphabetSize, m, initialState, finalStates, transitions)
        val result1 = dfa.isAccepted("100010101")
        val result2 = dfa.isAccepted("1000100")
        val result3 = dfa.isAccepted("")

        dfa.minimize()

        assertEquals(result1, dfa.isAccepted("100010101"))
        assertEquals(result2, dfa.isAccepted("1000100"))
        assertEquals(result3, dfa.isAccepted(""))

    }


}
