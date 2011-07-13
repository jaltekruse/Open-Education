package expression;

import java.util.Vector;

public class Expression extends Node {

	private Operator o;
	private Vector<Node> children;

	public Expression(Operator o, Vector<Node> children) {
		setOperator(o);
		setChildren(children);
	}
	
	public boolean equals(Object other) {
		if ((other == null) || !(other instanceof Expression))
			return false;
		Expression e = (Expression) other;
		return (o.equals(e.getOperator())) && 
				(children.equals(e.getChildren()));
	}

	@Override
	public int hashCode() {
		return o.hashCode() + children.hashCode();
	}
	
	@Override
	public String toString() {
		return parenthetize(o.toString(children));
	}

	@Override
	public Node replace(Identifier identifier, Node node) {
		Vector<Node> newChildren = new Vector<Node>();
		for (Node c : children)
			newChildren.add(c.replace(identifier, node));
		return new Expression(o, newChildren);
	}

	@Override
	public Node collectLikeTerms() {
		//TODO implement
		return null;
	}

	@Override
	public Node numericSimplify() {
		Vector<Node> simplifiedChildren = new Vector<Node>();
		Vector<Number> numbers = new Vector<Number>();
		Node simplified;
		boolean totallyNumeric = true;
		for (Node c : children) {
			simplified = c.numericSimplify();
			simplifiedChildren.add(simplified);
			if (simplified instanceof Number) {
				numbers.add((Number) simplified);
			} else {
				totallyNumeric = false;
			}
		}
		if (totallyNumeric)
			return o.evaluate(numbers);
		else
			return new Expression(o, simplifiedChildren);
	}

	public Operator getOperator() {
		return o;
	}

	public void setOperator(Operator o) {
		this.o = o;
	}

	public Vector<Node> getChildren() {
		return children;
	}

	public void setChildren(Vector<Node> children) {
		this.children = children;
	}

	private static String parenthetize(String s) {
		return "(" + s + ")";
	}
	
}
