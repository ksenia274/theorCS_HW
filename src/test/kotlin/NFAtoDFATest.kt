import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NFAtoDFATest { //2 задание
    @Test
    fun testNFAtoDFAConversion() {
        val transitions = mutableMapOf(
            Pair(0, 0) to setOf(1,2),
            Pair(0, 1) to setOf(2),
            Pair(1, 0) to setOf(2),
            Pair(2, 1) to setOf(2),
            Pair(2, 0) to setOf(0)
        )

        val nfa = NFA(3, 2, setOf(0), setOf(1), transitions)

        val dfaConverter = NFAtoDFA(nfa)
        val dfa = dfaConverter.convertToDFA()

        assertEquals(dfa.isAccepted("01"), nfa.isAccepted("01"))
        assertEquals(dfa.isAccepted("0"), nfa.isAccepted("0"))
        assertEquals(dfa.isAccepted("00"), nfa.isAccepted("00"))
        assertEquals(dfa.isAccepted("01111111"), nfa.isAccepted("01111111"))
        assertEquals(dfa.isAccepted("001000"), nfa.isAccepted("001000"))
        assertEquals(dfa.isAccepted("000"), nfa.isAccepted("000"))
        assertEquals(dfa.isAccepted("1100"), nfa.isAccepted("1100"))
        assertEquals(dfa.isAccepted("00100"), nfa.isAccepted("00100"))

    }
    @Test
    fun testNFAtoDFAConversionFromLecture() {
        val transitions = mutableMapOf(
            Pair(0, 0) to setOf(0,1),
            Pair(0, 1) to setOf(0),
            Pair(1, 0) to setOf(2),
            Pair(1, 1) to setOf(2),
            Pair(2, 0) to setOf(3),
            Pair(2, 1) to setOf(3),
        )

        val nfa = NFA(4, 2, setOf(0), setOf(3), transitions)

        val dfaConverter = NFAtoDFA(nfa)
        val dfa = dfaConverter.convertToDFA()


        assertEquals(dfa.isAccepted("000"), nfa.isAccepted("000"))
        assertEquals(dfa.isAccepted("0"), nfa.isAccepted("0"))
        assertEquals(dfa.isAccepted("100"), nfa.isAccepted("100"))
        assertEquals(dfa.isAccepted("010"), nfa.isAccepted("010"))
        assertEquals(dfa.isAccepted("111"), nfa.isAccepted("111"))
        assertEquals(dfa.isAccepted("000000001110"), nfa.isAccepted("000000001110"))
        assertEquals(dfa.isAccepted("00011000"), nfa.isAccepted("00011000"))
        assertEquals(dfa.isAccepted("0100"), nfa.isAccepted("0100"))

    }

}