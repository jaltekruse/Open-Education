package expression;

import java.util.Vector;

/**
 * Instances of this class are {@code Node} objects that
 * do not contain other {@code Node} objects; leaves on the
 * expression tree.
 * 
 * @author Killian Kvalvik
 *
 */
public abstract class Value extends Node {
	
	@Override
	public Node collectLikeTerms() {
		// this means nothing for pure Values
		return this.clone();
	}

	@Override
	public Node smartNumericSimplify() {
		return numericSimplify();
	}
	
	@Override
	public Node numericSimplify() {
		// this means nothing for pure Values
		return this.clone();
	}
	
	@Override
	public Node standardFormat() {
		return this.clone();
	}
	
	@Override
	public Vector<Node> splitOnAddition() {
		Vector<Node> v = new Vector<Node>();
		v.add(this);
		return v;
	}
	
	@Override
	public Vector<Node> splitOnMultiplication() {
		Vector<Node> v = new Vector<Node>();
		v.add(this);
		return v;
	}
	
	@Override
	public boolean containsIdentifier() {
		return (this instanceof Identifier);
	}

	public static Value parseValue(String expression) {
		try {
			return Number.parseNumber(expression);
		} catch (NumericException e) {
			return new Identifier(expression);
		}
	}
}
