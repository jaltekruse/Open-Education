package expression;

public abstract class Value extends Node {
	
	@Override
	public Node collectLikeTerms() {
		// this means nothing for pure Values
		return this;
	}

	@Override
	public Node numericSimplify() {
		// this means nothing for pure Values
		return this;
	}

	public static Value parseValue(String expression) {
		try {
			return Number.parseNumber(expression);
		} catch (NumericException e) {
			return new Identifier(expression);
		}
	}
}
