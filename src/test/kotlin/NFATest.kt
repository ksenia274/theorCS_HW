import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NFATest { //1 задание

    @Test
    fun testAcceptedString() {
        val transitions = mutableMapOf(
            Pair(0, 0) to setOf(1,2),
            Pair(0, 1) to setOf(2),
            Pair(1, 0) to setOf(2),
            Pair(2, 1) to setOf(2),
            Pair(2, 0) to setOf(0)
        )

        val nfa = NFA(3, 2, setOf(0), setOf(1), transitions)

        assertTrue(nfa.isAccepted("0"))
        assertTrue(nfa.isAccepted("000"))
        assertTrue(nfa.isAccepted("1100"))
        assertTrue(nfa.isAccepted("00100"))
    }

    @Test
    fun testRejectedString() {
        val transitions = mutableMapOf(
            Pair(0, 0) to setOf(1,2),
            Pair(0, 1) to setOf(2),
            Pair(1, 0) to setOf(2),
            Pair(2, 1) to setOf(2),
            Pair(2, 0) to setOf(0)
        )

        val nfa = NFA(3, 2, setOf(0), setOf(1), transitions)

        assertFalse(nfa.isAccepted("01"))
        assertFalse(nfa.isAccepted("00"))
        assertFalse(nfa.isAccepted("01111111"))
        assertFalse(nfa.isAccepted("001000"))

    }
    @Test
    fun testAcceptedStringFromLecture() {
        val transitions = mutableMapOf(
            Pair(0, 0) to setOf(0,1),
            Pair(0, 1) to setOf(0),
            Pair(1, 0) to setOf(2),
            Pair(1, 1) to setOf(2),
            Pair(2, 0) to setOf(3),
            Pair(2, 1) to setOf(3),
            )

        val nfa = NFA(4, 2, setOf(0), setOf(3), transitions)

        assertTrue(nfa.isAccepted("000"))
        assertTrue(nfa.isAccepted("1000"))
        assertTrue(nfa.isAccepted("0010"))
    }
    @Test
    fun testRejectedStringFromLecture() {
        val transitions = mutableMapOf(
            Pair(0, 0) to setOf(0,1),
            Pair(0, 1) to setOf(0),
            Pair(1, 0) to setOf(2),
            Pair(1, 1) to setOf(2),
            Pair(2, 0) to setOf(3),
            Pair(2, 1) to setOf(3),
        )

        val nfa = NFA(4, 2, setOf(0), setOf(3), transitions)

        assertFalse(nfa.isAccepted("0"))
        assertFalse(nfa.isAccepted("100"))
        assertFalse(nfa.isAccepted("1100"))
        assertFalse(nfa.isAccepted("10"))
    }
}
