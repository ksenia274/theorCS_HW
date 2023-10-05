import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class RegexTest {

    @Test
    fun generateCodesTest1() {
        val regex = "a+b+"
        val instructions = generateInstructions(regex)
        val result = listOf("char a",
                "split 0, 2",
                "char b",
                "split 2, 4",
                "match")
        assertEquals(result,instructions)

    }

    @Test
    fun generateCodesTest2() {
        val regex = "a*b+"
        val instructions = generateInstructions(regex)
        val result = listOf("split 1, 3",
            "char a",
            "jmp 0",
            "char b",
            "split 3, 5",
            "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesTest3() {
        val regex = "a|cb+"
        val instructions = generateInstructions(regex)
        val result = listOf("split 1, 3",
            "char a",
            "jmp 6",
            "char c",
            "char b",
            "split 4, 6",
            "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesTest4() {
        val regex = "a?b+"
        val instructions = generateInstructions(regex)
        val result = listOf("split 1, 2",
            "char a",
            "char b",
            "split 2, 4",
            "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesTest5() {
        val regex = "abc+"
        val instructions = generateInstructions(regex)
        val result = listOf("char a",
            "char b",
            "char c",
            "split 2, 4",
            "match")
        assertEquals(result,instructions)

    }

    @Test
    fun generateCodesTest6() {
        val regex = "a|b|c"
        val instructions = generateInstructions(regex)
        val result = listOf("split 1, 3, 5",
            "char a", "jmp 6", "char b", "jmp 6",
            "char c", "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesTest7() {
        val regex = "a|b*|c|d"
        val instructions = generateInstructions(regex)
        val result = listOf("split 1, 3, 7, 9",
            "char a", "jmp 10", "split 4, 6",
                "char b", "jmp 3", "jmp 10", "char c",
            "jmp 10", "char d", "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesTest8() {
        val regex = "a|b+|c+"
        val instructions = generateInstructions(regex)
        val result = listOf("split 1, 3, 6",
            "char a",
            "jmp 8",
            "char b",
            "split 3, 5",
            "jmp 8",
            "char c",
            "split 6, 8",
            "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesTest9() {
        val regex = "a|b|c?"
        val instructions = generateInstructions(regex)
        val result = listOf("split 1, 3, 5",
            "char a",
            "jmp 7",
            "char b",
            "jmp 7",
            "split 6, 7",
            "char c",
            "match")
        assertEquals(result,instructions)

    }

    @Test
    fun generateCodesTest10() {
        val regex = "a|b|c*"
        val instructions = generateInstructions(regex)
        val result = listOf("split 1, 3, 5",
            "char a",
            "jmp 8",
            "char b",
            "jmp 8",
            "split 6, 8",
            "char c",
            "jmp 5",
            "match")
        assertEquals(result,instructions)

    }

    @Test
    fun matchingTest1() {
        val regex = "a+b+"
        val instructions = generateInstructions(regex)
        assertTrue( executeInstructions(instructions,"aabbb"))
        assertTrue( executeInstructions(instructions,"ab"))
        assertFalse( executeInstructions(instructions,"aaaa"))
        assertFalse( executeInstructions(instructions,"bbb"))
        assertFalse( executeInstructions(instructions,"baabababb"))

    }

    @Test
    fun matchingTest2() {
        val regex = "a*b+"
        val instructions = generateInstructions(regex)
        assertTrue( executeInstructions(instructions,"aabbb"))
        assertTrue( executeInstructions(instructions,"ab"))
        assertFalse( executeInstructions(instructions,"aaaa"))
        assertTrue( executeInstructions(instructions,"bbb"))
        assertFalse( executeInstructions(instructions,"baabababb"))

    }

    @Test
    fun matchingTest3() {
        val regex = "a|cb+"
        val instructions = generateInstructions(regex)
        assertTrue( executeInstructions(instructions,"a"))
        assertTrue( executeInstructions(instructions,"cb"))
        assertTrue( executeInstructions(instructions,"cbbbbb"))
        assertFalse( executeInstructions(instructions,"aaaa"))
        assertFalse( executeInstructions(instructions,"bbb"))
        assertFalse( executeInstructions(instructions,"baabababb"))
        assertFalse( executeInstructions(instructions,"cbaabababb"))
    }

    @Test
    fun matchingTest4() {
        val regex = "a?b+"
        val instructions = generateInstructions(regex)
        assertTrue( executeInstructions(instructions,"abbb"))
        assertTrue( executeInstructions(instructions,"ab"))
        assertFalse( executeInstructions(instructions,"aaaa"))
        assertTrue( executeInstructions(instructions,"bbb"))
        assertFalse( executeInstructions(instructions,"baabababb"))

    }

    @Test
    fun matchingTest5() {
        val regex = "abc+"
        val instructions = generateInstructions(regex)
        assertTrue( executeInstructions(instructions,"abc"))
        assertTrue( executeInstructions(instructions,"abcccc"))
        assertFalse( executeInstructions(instructions,"aaaa"))
        assertFalse( executeInstructions(instructions,"bbb"))
        assertFalse( executeInstructions(instructions,"baccabababb"))

    }

    @Test
    fun matchingTest6() {
        val regex = "a|b|c"
        val instructions = generateInstructions(regex)
        assertTrue( executeInstructions(instructions,"a"))
        assertTrue( executeInstructions(instructions,"b"))
        assertTrue( executeInstructions(instructions,"c"))
        assertFalse( executeInstructions(instructions,"aaaa"))
        assertFalse( executeInstructions(instructions,"bbb"))
        assertFalse( executeInstructions(instructions,"baabababb"))
        assertFalse( executeInstructions(instructions,"ccabb"))
        assertFalse( executeInstructions(instructions,"abc"))



    }

    @Test
    fun matchingTest7() {
        val regex = "a|b+|c+"
        val instructions = generateInstructions(regex)
        assertTrue( executeInstructions(instructions,"a"))
        assertTrue( executeInstructions(instructions,"b"))
        assertTrue( executeInstructions(instructions,"c"))
        assertTrue( executeInstructions(instructions,"bbbbb"))
        assertTrue( executeInstructions(instructions,"ccc"))
        assertFalse( executeInstructions(instructions,"aaaa"))
        assertFalse( executeInstructions(instructions,"bbcb"))
        assertFalse( executeInstructions(instructions,"baabababb"))

    }

}