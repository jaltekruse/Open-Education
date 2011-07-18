package expression;

import java.util.Vector;

/** {@code Node} is the root class for an expression tree and 
 * values in that tree.
 * @author Killian Kvalvik
 */
public abstract class Node implements Cloneable {
	
	private static final NodeParser defaultParser = new NodeParser();
	
	private boolean displayParentheses = false;
	
	/**
	 * Returns whether all the sub-nodes of
	 * {@code this} are equal to those of
	 * {@code other}.
	 */
	@Override
	public abstract boolean equals(Object other);

	@Override
	public abstract int hashCode();

	/**  Returns a {@code String} representation 
	 * of this expression tree. */
	@Override
	public abstract String toString();


	/**
	 * Returns a deep copy of this {@code Node}; 
	 * i.e. clones all sub-nodes.
	 */
	@Override
	public abstract Node clone();
	
	/** Replaces all instances of {@code x} with {@code n}.
	 * @param identifier The identifier to find.
	 * @param node The node to replace the identifier with.
	 * @return An altered {@code Node} with no references to the original.
	 */
	public abstract Node replace(Identifier identifier, Node node);
	
	public abstract Node collectLikeTerms();
	
	/** Simplifies all numeric calculations. 
	 * Does not take advantage of numeric identities.
	 * @return An altered {@code Node} with no references to the original.
	 */
	public abstract Node numericSimplify();
	
	public Node smartNumericSimplify() {
		return numericSimplify();
	}
	
	public Node simplify() {
		Node simplified = this;
		Node last;
		do {
			last = simplified;
			simplified = last.smartNumericSimplify();
			simplified = simplified.collectLikeTerms();
		} while (!last.equals(simplified));
		
		return simplified;
	}

	public abstract Vector<Node> splitOnAddition();
	
	public abstract Vector<Node> splitOnMultiplication();
	
	/**
	 * @return Whether this {@code Node} is empty; that is,
	 * if it does not contain any information and is just a placeholder.
	 */
	public boolean isEmpty() {
		return (this instanceof EmptyValue);
	}
	
	public abstract boolean containsIdentifier();

	/**
	 * @return Whether this {@code Node} is currently
	 * set to display parentheses. 
	 */
	public boolean displayParentheses() {
		return displayParentheses;
	}

	/**
	 * Sets the parentheses display setting for this node.
	 * @param displayParentheses Whether to display parentheses.
	 */
	public void setDisplayParentheses(boolean displayParentheses) {
		this.displayParentheses = displayParentheses;
	}

	/** Parses {@code expression} to {@code Node} form with the default settings.
	 */
	public static Node parseNode(String expression) {
		return defaultParser.parseNode(expression);
	}

}
