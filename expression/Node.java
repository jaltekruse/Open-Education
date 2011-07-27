/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package expression;

/** {@code Node} is the root class for an expression tree.
 * @author Killian
 */
public abstract class Node {

	//TODO edit for parentheses flag
	
	private static final NodeParser defaultParser = new NodeParser();
	
	@Override
	public abstract boolean equals(Object other);

	@Override
	public abstract int hashCode();

	/**  Returns a {@code String} representation 
	 * of this expression tree. */
	@Override
	public abstract String toString();

	/** Replaces all instances of {@code x} with {@code n}.
	 * @param identifier The identifier to find.
	 * @param node The node to replace the identifier with.
	 */
	public abstract Node replace(Identifier identifier, Node node);
	
	public abstract Node collectLikeTerms();
	
	/** Simplifies all numeric calculations. 
	 * Does not take advantage of numeric identities.
	 */
	public abstract Node numericSimplify();

	/** Parses {@code expression} to {@code Node} form with the default settings.
	 */
	public static Node parseNode(String expression) {
		return defaultParser.parseNode(expression);
	}

}
