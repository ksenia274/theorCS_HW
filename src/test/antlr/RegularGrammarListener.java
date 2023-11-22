// Generated from D:/2023-2024/1 семестр/Теоретическая информатика/theorCS/src/main/antlr/RegularGrammar.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RegularGrammarParser}.
 */
public interface RegularGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RegularGrammarParser#regex}.
	 * @param ctx the parse tree
	 */
	void enterRegex(RegularGrammarParser.RegexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegularGrammarParser#regex}.
	 * @param ctx the parse tree
	 */
	void exitRegex(RegularGrammarParser.RegexContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegularGrammarParser#branch}.
	 * @param ctx the parse tree
	 */
	void enterBranch(RegularGrammarParser.BranchContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegularGrammarParser#branch}.
	 * @param ctx the parse tree
	 */
	void exitBranch(RegularGrammarParser.BranchContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegularGrammarParser#concatenation}.
	 * @param ctx the parse tree
	 */
	void enterConcatenation(RegularGrammarParser.ConcatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegularGrammarParser#concatenation}.
	 * @param ctx the parse tree
	 */
	void exitConcatenation(RegularGrammarParser.ConcatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegularGrammarParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit(RegularGrammarParser.UnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegularGrammarParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit(RegularGrammarParser.UnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegularGrammarParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(RegularGrammarParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegularGrammarParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(RegularGrammarParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegularGrammarParser#group}.
	 * @param ctx the parse tree
	 */
	void enterGroup(RegularGrammarParser.GroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegularGrammarParser#group}.
	 * @param ctx the parse tree
	 */
	void exitGroup(RegularGrammarParser.GroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegularGrammarParser#character}.
	 * @param ctx the parse tree
	 */
	void enterCharacter(RegularGrammarParser.CharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegularGrammarParser#character}.
	 * @param ctx the parse tree
	 */
	void exitCharacter(RegularGrammarParser.CharacterContext ctx);
}