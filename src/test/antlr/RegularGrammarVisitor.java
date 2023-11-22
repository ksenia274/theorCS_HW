// Generated from D:/2023-2024/1 семестр/Теоретическая информатика/theorCS/src/main/antlr/RegularGrammar.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RegularGrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RegularGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RegularGrammarParser#regex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegex(RegularGrammarParser.RegexContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegularGrammarParser#branch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBranch(RegularGrammarParser.BranchContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegularGrammarParser#concatenation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatenation(RegularGrammarParser.ConcatenationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegularGrammarParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit(RegularGrammarParser.UnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegularGrammarParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(RegularGrammarParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegularGrammarParser#group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup(RegularGrammarParser.GroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegularGrammarParser#character}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter(RegularGrammarParser.CharacterContext ctx);
}