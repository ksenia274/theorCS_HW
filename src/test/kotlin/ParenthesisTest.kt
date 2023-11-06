import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue



class ParenthesisTest {
    @Test
    fun generateCodesParenthesisTest1() {
        val regex = "(a+b)+"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        val result = listOf("char a",
            "split 0, 2",
            "char b",
            "split 0, 4",
            "match")
        assertEquals(result,instructions)

    }

    @Test
    fun generateCodesParenthesisTest2() {
        val regex = "((a+b)+c+)+"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        val result = listOf("char a",
            "split 0, 2",
            "char b",
            "split 0, 4",
            "char c",
            "split 4, 6",
            "split 0, 7",
            "match")
        assertEquals(result,instructions)

    }

    @Test
    fun generateCodesParenthesisTest11() {
        val regex = "((a+b)+c+)(d+)"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        val result = listOf("char a",
            "split 0, 2",
            "char b",
            "split 0, 4",
            "char c",
            "split 4, 6",
            "char d",
            "split 6, 8",
            "match")
        assertEquals(result,instructions)

    }

    @Test
    fun generateCodesParenthesisTest12() {
        val regex = "(a*b)*"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        val result = listOf("split 1, 6",
            "split 2, 4",
            "char a",
            "jmp 1",
            "char b",
            "jmp 0",
            "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesParenthesisTest13() {
        val regex = "a*(a*b*)*"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        val result = listOf("split 1, 3", "char a",
            "jmp 0", "split 4, 11", "split 5, 7", "char a",
            "jmp 4", "split 8, 10", "char b", "jmp 7",
            "jmp 3", "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesParenthesisTest3() {
        val regex = "a|cb+"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
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
    fun generateCodesParenthesisTest4() {
        val regex = "a?b+"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        val result = listOf("split 1, 2",
            "char a",
            "char b",
            "split 2, 4",
            "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesParenthesisTest5() {
        val regex = "abc+"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        val result = listOf("char a",
            "char b",
            "char c",
            "split 2, 4",
            "match")
        assertEquals(result,instructions)

    }

    @Test
    fun generateCodesParenthesisTest6() {
        val regex = "a|b|c"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        val result = listOf("split 1, 3, 5",
            "char a", "jmp 6", "char b", "jmp 6",
            "char c", "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesParenthesisTest7() {
        val regex = "a|b*|c|d"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        val result = listOf("split 1, 3, 7, 9",
            "char a", "jmp 10", "split 4, 6",
            "char b", "jmp 3", "jmp 10", "char c",
            "jmp 10", "char d", "match")
        assertEquals(result,instructions)

    }
    @Test
    fun generateCodesParenthesisTest8() {
        val regex = "a|b+|c+"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
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
    fun generateCodesParenthesisTest9() {
        val regex = "a|b|c?"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
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
    fun generateCodesParenthesisTest10() {
        val regex = "a|b|c*"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
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
    fun matchingParenthesisTest1() {
        val regex = "(a+b)+"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        assertTrue( executeInstructions(instructions,"aabbb"))
        assertTrue( executeInstructions(instructions,"ab"))
        assertFalse( executeInstructions(instructions,"aaaa"))
        assertFalse( executeInstructions(instructions,"bbb"))
        assertFalse( executeInstructions(instructions,"baabababb"))

    }

    @Test
    fun matchingParenthesisTest2() {
        val regex = "(a*b)+"
        val generator = Generator(regex)
        val instructions = generator.generateInstructions(regex)
        assertTrue( executeInstructions(instructions,"aabbb"))
        assertTrue( executeInstructions(instructions,"ab"))
        assertFalse( executeInstructions(instructions,"aaaa"))
        assertTrue( executeInstructions(instructions,"bbb"))
        assertFalse( executeInstructions(instructions,"baabababb"))

    }

//    @Test
//    fun matchingParenthesisTest3() {
//        val regex = "(a|c)b+"
//        val instructions = generateInstructions(regex)
//        assertTrue( executeInstructions(instructions,"a"))
//        assertTrue( executeInstructions(instructions,"cb"))
//        assertTrue( executeInstructions(instructions,"cbbbbb"))
//        assertFalse( executeInstructions(instructions,"aaaa"))
//        assertFalse( executeInstructions(instructions,"bbb"))
//        assertFalse( executeInstructions(instructions,"baabababb"))
//        assertFalse( executeInstructions(instructions,"cbaabababb"))
//    }
//
//    @Test
//    fun matchingParenthesisTest4() {
//        val regex = "a?b+"
//        val instructions = generateInstructions(regex)
//        assertTrue( executeInstructions(instructions,"abbb"))
//        assertTrue( executeInstructions(instructions,"ab"))
//        assertFalse( executeInstructions(instructions,"aaaa"))
//        assertTrue( executeInstructions(instructions,"bbb"))
//        assertFalse( executeInstructions(instructions,"baabababb"))
//
//    }
//
//    @Test
//    fun matchingParenthesisTest5() {
//        val regex = "a(bc+)"
//        val instructions = generateInstructions(regex)
//        assertTrue( executeInstructions(instructions,"abc"))
//        assertTrue( executeInstructions(instructions,"abcccc"))
//        assertFalse( executeInstructions(instructions,"aaaa"))
//        assertFalse( executeInstructions(instructions,"bbb"))
//        assertFalse( executeInstructions(instructions,"baccabababb"))
//
//    }
//
//    @Test
//    fun matchingParenthesisTest6() {
//        val regex = "a|b|(c*)"
//        val instructions = generateInstructions(regex)
//        assertTrue( executeInstructions(instructions,"a"))
//        assertTrue( executeInstructions(instructions,"b"))
//        assertTrue( executeInstructions(instructions,"c"))
//        assertFalse( executeInstructions(instructions,"aaaa"))
//        assertFalse( executeInstructions(instructions,"bbb"))
//        assertFalse( executeInstructions(instructions,"baabababb"))
//        assertFalse( executeInstructions(instructions,"ccabb"))
//        assertFalse( executeInstructions(instructions,"abc"))
//
//
//
//    }
//
//    @Test
//    fun matchingParenthesisTest7() {
//        val regex = "a|b+|c+"
//        val instructions = generateInstructions(regex)
//        assertTrue( executeInstructions(instructions,"a"))
//        assertTrue( executeInstructions(instructions,"b"))
//        assertTrue( executeInstructions(instructions,"c"))
//        assertTrue( executeInstructions(instructions,"bbbbb"))
//        assertTrue( executeInstructions(instructions,"ccc"))
//        assertFalse( executeInstructions(instructions,"aaaa"))
//        assertFalse( executeInstructions(instructions,"bbcb"))
//        assertFalse( executeInstructions(instructions,"baabababb"))
//
//    }
}