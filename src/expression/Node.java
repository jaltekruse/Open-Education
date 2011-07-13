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

	/**
	 * @return Whether this {@code Node} is empty; that is,
	 * if it does not contain any information and is just a placeholder.
	 */
	public boolean isEmpty() {
		return (this instanceof EmptyValue);
	}

}
