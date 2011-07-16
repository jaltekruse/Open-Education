package expression;

import java.util.Vector;

/**
 * The {@code Expression} class represents the combination
 * of children {@code Node} objects with an {@code Operator} 
 * to create a nontrivial expression.
 * 
 * @author Killian Kvalvik
 *
 */
public class Expression extends Node {

	private Operator o;
	private Vector<Node> children;

	public Expression(Operator o, Vector<Node> children) {
		setOperator(o);
		setChildren(children);
	}
	
	public Expression(Operator o, Node... children) {
		Vector<Node> v = new Vector<Node>();
		for (Node c : children) {
			v.add(c);
		}
		setOperator(o);
		setChildren(v);
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
	public Expression clone() {
		Vector<Node> clone = new Vector<Node>();
		for (Node child : children)
			clone.add(child.clone());
		return new Expression(o.clone(), clone);
	}

	@Override
	public Node replace(Identifier identifier, Node node) {
		Vector<Node> newChildren = new Vector<Node>();
		for (Node c : children)
			newChildren.add(c.replace(identifier, node));
		return new Expression(o, newChildren);
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
				numbers.add((Number) simplified.clone());
			} else {
				totallyNumeric = false;
			}
		}
		if (totallyNumeric)
			return o.evaluate(numbers);
		else
			return new Expression(o.clone(), simplifiedChildren);
	}
	
	@Override
	public Node smartNumericSimplify() {
		Vector<Node> addends = splitOnAddition();
		if (addends.size() > 1) {
			Vector<Node> simplified = new Vector<Node>();
			for (Node addend : addends) {
				simplified.add(addend.smartNumericSimplify());
			}
			Vector<Node> numbers = new Vector<Node>();
			Node addend;
			for (int i = simplified.size() - 1 ; i >= 0 ; i--) {
				addend = simplified.get(i);
				if (addend instanceof Number) {
					numbers.add((Number) addend);
					simplified.remove(i);
				}
			}
			simplified.add(staggerAddition(numbers).numericSimplify());
			if (simplified.contains(new Number(0))) {
				simplified.remove(new Number(0));
				return staggerAddition(simplified).smartNumericSimplify();
			}
			Node finalNode = staggerAddition(simplified);
			if (finalNode instanceof Expression) {
				Expression ex = (Expression) finalNode;
				if (ex.getOperator() instanceof Operator.Addition) {
					Node second = ex.getChildren().get(1);
					if (second instanceof Number) {
						Number n = (Number) second;
						if (n.isNegative()) {
							finalNode = new Expression(new Operator.Subtraction(), 
									ex.getChildren().firstElement(), n.negate());
						}
					}
				}
			}
			return finalNode;
		}
		
		Vector<Node> factors = splitOnMultiplication();
		if (factors.size() > 1) {
			Vector<Node> simplified = new Vector<Node>();
			for (Node factor : factors) {
				simplified.add(factor.smartNumericSimplify());
			}
			Vector<Node> numbers = new Vector<Node>();
			Node addend;
			for (int i = simplified.size() - 1 ; i >= 0 ; i--) {
				addend = simplified.get(i);
				if (addend instanceof Number) {
					numbers.add((Number) addend);
					simplified.remove(i);
				}
			}
			simplified.add(staggerMultiplication(numbers).numericSimplify());
			if (simplified.contains(new Number(0))) {
				return new Number(0);
			}
			if (simplified.contains(new Number(1))) {
				simplified.remove(new Number(1));
				return staggerMultiplication(simplified).smartNumericSimplify();
			}
			return staggerMultiplication(simplified);
		}
		
		if (o instanceof Operator.Exponent) {
			Node exponent = children.get(1).smartNumericSimplify();
			Node base = children.get(0).smartNumericSimplify();
			if (exponent.equals(new Number(0)))
				return new Number(1);
			if (exponent.equals(new Number(1)))
				return base;
			if (base.equals(new Number(0)))
				return new Number(0);
			if (base.equals(new Number(1)))
				return new Number(1);
			return new Expression(new Operator.Exponent(), base, exponent);
		}
		
		Vector<Node> simplifiedChildren = new Vector<Node>();
		for (Node child : children)
			simplifiedChildren.add(child.smartNumericSimplify());
		return new Expression(o.clone(), simplifiedChildren);
	}

	@Override
	public Node collectLikeTerms() {
		Vector<Node> split = splitOnAddition();
		Vector<Node> terms = new Vector<Node>();
		for (Node n : split)
			terms.add(n.clone());
		if (terms.size() == 1) {
			Vector<Node> args = new Vector<Node>();
			for (Node c : children)
				args.add(c.collectLikeTerms());
			return new Expression(o.clone(), args);
		}
		Vector<Vector<Node>> expandedTerms = new Vector<Vector<Node>>();
		Vector<Node> factors;
		Vector<Node> collectedFactors;
		for (Node term : terms) {
			factors = term.splitOnMultiplication();
			collectedFactors = new Vector<Node>();
			for (Node f : factors) {
				collectedFactors.add(f.collectLikeTerms());
			}
			expandedTerms.add(collectedFactors);
		} // that was the recursion part
		
		Vector<Node> additionChildren = new Vector<Node>();
		
		Node likeTerm = findDuplicate(expandedTerms);
		while (likeTerm != null) {
			Vector<Vector<Node>> commonTerms = new Vector<Vector<Node>>();
			Vector<Node> expandedTerm;
			Vector<Integer> termsToRemove = new Vector<Integer>();
			for (int i = 0 ; i < terms.size() ; i++) {
				expandedTerm = expandedTerms.get(i);
				boolean addedThisTerm = false;
				for (Node factor : expandedTerm) {
					if (factor.equals(likeTerm)) {
						commonTerms.add(expandedTerm);
						addedThisTerm = true;
						break;
					}
				}
				if (addedThisTerm) {
					termsToRemove.add(i);
				}
			}
			for (int i = termsToRemove.size() - 1; i >= 0 ; i--) {
				int index = termsToRemove.get(i);
				terms.remove(index);
				expandedTerms.remove(index);
			}
			
			// now commonTerms contains all nodes with a factor of likeTerm
			Vector<Node> combinedTerms = new Vector<Node>();
			for (Vector<Node> commonTerm : commonTerms) {
				commonTerm.remove(likeTerm);
				combinedTerms.add(staggerMultiplication(commonTerm));
			}
			
			additionChildren.add(new Expression(
					new Operator.Multiplication(), 
					staggerAddition(combinedTerms),
					likeTerm));
			
			likeTerm = findDuplicate(expandedTerms);
		}
		
		for (Node t : terms) { // all the ones that couldn't be collected
			additionChildren.add(t);
		}
		
		return stagger(additionChildren, new Operator.Addition());
	}
	
	private Node findDuplicate(Vector<Vector<Node>> expandedTerms) {
		Vector<Node> factors;
		Vector<Node> searchTerm;
		for (int i = 0 ; i < expandedTerms.size(); i++) {
			factors = expandedTerms.get(i);
			for (Node f : factors) {
				if (!f.containsIdentifier())
					continue;
				for (int search = (i + 1) ; search < expandedTerms.size(); search++) {
					searchTerm = expandedTerms.get(search);
					if (searchTerm.contains(f))
						return f;
				}
			}
		}
		return null;
	}
	
	private static Node staggerAddition(Vector<Node> addends) {
		if (addends.isEmpty())
			return new Number(0);
		return stagger(addends, new Operator.Addition());
	}
	
	private static Node staggerMultiplication(Vector<Node> factors) {
		if (factors.isEmpty())
			return new Number(1);
		return stagger(factors, new Operator.Multiplication());
	}
	
	private static Node stagger(Vector<Node> addends, Operator op) {
		@SuppressWarnings("unchecked")
		Vector<Node> children = (Vector<Node>) addends.clone();
		
		int size = children.size();
		switch (size) {
		case 0:
			return null;
		case 1:
			return children.firstElement();
		case 2:
			return new Expression(op.clone(), children);
		default:
			Node last = children.remove(children.size() - 1);
			return new Expression(op.clone(), stagger(children, op), last);
		}
	}

	public boolean containsIdentifier() {
		boolean id = false;
		for (Node child : children) {
			id = id || child.containsIdentifier();
		}
		return id;
	}
	
	@Override
	public Vector<Node> splitOnAddition() {
		Vector<Node> terms = new Vector<Node>();
		if (o instanceof Operator.Addition) {
			terms.addAll(children.get(0).splitOnAddition());
			terms.addAll(children.get(1).splitOnAddition());
		} else if (o instanceof Operator.Subtraction) {
			terms.addAll(children.get(0).splitOnAddition());
			Vector<Node> subtracted = children.get(1).splitOnAddition();
			Vector<Node> negativeTerms = new Vector<Node>();
			for (Node s : subtracted) {
				negativeTerms.add(new Expression(new Operator.Negation(), s));
			}
			terms.addAll(negativeTerms);
		} else {
			terms.add(this);
		}
		return terms;
	}
	
	@Override
	public Vector<Node> splitOnMultiplication() {
		Vector<Node> factors = new Vector<Node>();
		if (o instanceof Operator.Multiplication) {
			factors.addAll(children.get(0).splitOnMultiplication());
			factors.addAll(children.get(1).splitOnMultiplication());
		} else if (o instanceof Operator.Division) {
			factors.addAll(children.get(0).splitOnMultiplication());
			Vector<Node> divided = children.get(1).splitOnMultiplication();
			Vector<Node> reciprocalFactors = new Vector<Node>();
			for (Node d : divided) {
				reciprocalFactors.add(new Expression(new Operator.Division(), new Number(1), d));
			}
			factors.addAll(reciprocalFactors);
		} else if (o instanceof Operator.Negation) {
			factors.add(new Number(-1));
			factors.add(children.get(0));
		} else {
			factors.add(this);
		}
		return factors;
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
