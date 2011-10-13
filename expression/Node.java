package expression;

import java.util.Comparator;
import java.util.Vector;

/** {@code Node} is the root class for an expression tree and 
 * values in that tree.
 * @author Killian Kvalvik
 */
public abstract class Node implements Cloneable {
	
	private static final Comparator<Node> standardComparator = 
		new Comparator<Node>() {
			@Override
			public int compare(Node a, Node b) {
					return a.standardCompare(b);
			}
		};
		
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
	 * of this expression tree. 
	 * @throws NodeException */
	public abstract String toString();


	/**
	 * Returns a deep copy of this {@code Node}; 
	 * i.e. clones all sub-nodes.
	 * @throws NodeException 
	 */
	public abstract Node clone();
	
	/** Replaces all instances of {@code x} with {@code n}.
	 * @param identifier The identifier to find.
	 * @param node The node to replace the identifier with.
	 * @return An altered {@code Node} with no references to the original.
	 */
	public abstract Node replace(Identifier identifier, Node node);
	
	public Node replace(String id, Node node) throws IdentifierException {
		return replace(new Identifier(id), node);
	}
	
	public Node getSelection(Selection s) {
		if (s.depth() == 0)
			return this;
		throw new IndexOutOfBoundsException();
	}
	
	public Node replace(Selection s, Node node) {
		if (s.depth() == 0)
			return node;
		throw new IndexOutOfBoundsException();
	}
	
	public Selection find(Node node) {
		if (this.equals(node))
			return new Selection();
		return null;
	}
	
	public Selection findFromLast(Node node) {
		return find(node);
	}
	
	public abstract Node collectLikeTerms();
	
	/** Simplifies all numeric calculations. 
	 * Does not take advantage of numeric identities.
	 * @return An altered {@code Node} with no references to the original.
	 * @throws NodeException 
	 */
	public abstract Node numericSimplify();
	
	public abstract Node smartNumericSimplify();
	
	public Node simplify() {
		Node simplified = this;
		Node last;
		do {
			last = simplified;
			simplified = last.smartNumericSimplify();
			simplified = simplified.collectLikeTerms();
			simplified = simplified.standardFormat();
		} while (!last.equals(simplified));
		
		return simplified;
	}

	public abstract Vector<Node> splitOnAddition();
	
	public abstract Vector<Node> splitOnMultiplication();
	
	public abstract Node standardFormat();
	
	protected abstract int standardCompare(Node other);
	
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
	 * @throws NodeException 
	 */
	public static Node parseNode(String expression) throws ParseException {
		return defaultParser.parseNode(expression);
	}
	
	public static Comparator<Node> getStandardComparator() {
		return standardComparator;
	}
}
