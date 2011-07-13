package expression;

import java.util.Vector;

public abstract class Operator {

	public abstract String getSymbol();
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		return this.getClass() == o.getClass();
	}
	
	@Override
	public int hashCode() {
		return 17;
	}
	
	public String toString(Vector<Node> children) {
		Vector<String> stringChildren = new Vector<String>();
		for (Node c : children)
			stringChildren.add(c.toString());
		return format(stringChildren);
	}
	
	public abstract Number evaluate(Vector<Number> children);
	
	public abstract String format(Vector<String> children);
	
	public static abstract class BinaryOperator extends Operator {
		@Override
		public String format(Vector<String> children) {
			if (children.size() != 2)
				throwBadArguments();
			return children.get(0) + getSymbol() + children.get(1);
		}
		
		@Override
		public Number evaluate(Vector<Number> children) {
			if (children.size() != 2)
				throwBadArguments();
			return evaluate(children.get(0), children.get(1));
		}
		
		public abstract Number evaluate (Number a, Number b);
	}
	
	public static class Addition extends BinaryOperator {
		@Override
		public String getSymbol() {
			return "+";
		}

		@Override
		public Number evaluate(Number a, Number b) {
			return a.add(b);
		}
	}
	
	public static class Subtraction extends BinaryOperator {
		@Override
		public String getSymbol() {
			return "-";
		}

		@Override
		public Number evaluate(Number a, Number b) {
			return a.subtract(b);
		}
	}
	
	public static class Multiplication extends BinaryOperator {
		private boolean implicit;

		public Multiplication(boolean implicit) {
			this.implicit = implicit;
		}
		
		@Override
		public boolean equals(Object o) {
			return super.equals(o) && 
					(this.implicit == ((Multiplication) o).implicit);
		}
		
		public boolean isImplicit() {
			return implicit;
		}

		public void setImplicit(boolean implicit) {
			this.implicit = implicit;
		}

		@Override
		public Number evaluate(Number a, Number b) {
			return a.multiply(b);
		}

		@Override
		public String getSymbol() {
			if (isImplicit())
				return "";
			else
				return "*";
		}
	}
	
	public static class Division extends BinaryOperator { @Override
		public String getSymbol() {
			return "/";
		}

	// add formatting if necessary!

		@Override
		public Number evaluate(Number a, Number b) {
			return a.divide(b);
		}
	}
	
	public static class Exponent extends BinaryOperator { // add formatting if necessary

		@Override
		public Number evaluate(Number a, Number b) {
			return a.exponent(b);
		}

		@Override
		public String getSymbol() {
			return "^";
		}
		
	}
	
	public static abstract class UnaryFunction extends Operator {

		@Override
		public Number evaluate(Vector<Number> children) {
			if (children.size() != 1)
				throwBadArguments();
			return evaluate(children.get(0));
		}

		@Override
		public String format(Vector<String> children) {
			if (children.size() != 1)
				throwBadArguments();
			return getSymbol() + "(" + children.get(0) + ")";
		}

		public abstract Number evaluate(Number a);
	}
	
	public static class Logarithm extends UnaryFunction {

		@Override
		public String getSymbol() {
			return "log";
		}

		@Override
		public Number evaluate(Number a) {
			return a.log();
		}
	}
	
	public static class Sine extends UnaryFunction {
		@Override
		public String getSymbol() {
			return "sin";
		}

		@Override
		public Number evaluate(Number a) {
			return a.sin();
		}
	}
	
	public static class Cosine extends UnaryFunction {
		@Override
		public String getSymbol() {
			return "cos";
		}

		@Override
		public Number evaluate(Number a) {
			return a.cos();
		}
	}
	
	public static class Tangent extends UnaryFunction {
		@Override
		public String getSymbol() {
			return "tan";
		}

		@Override
		public Number evaluate(Number a) {
			return a.tan();
		}
	}
	
	public static class Negation extends Operator {

		@Override
		public String getSymbol() {
			return "-";
		}

		@Override
		public Number evaluate(Vector<Number> children) {
			if (children.size() != 1)
				throwBadArguments();
			return children.get(0).negate();
		}

		@Override
		public String format(Vector<String> children) {
			if (children.size() != 1)
				throwBadArguments();
			return getSymbol() + children.get(0);
		}
		
	}
	
	public static void throwBadArguments() {
		throw new NodeException("Incorrect number of arguments");
	}
}
