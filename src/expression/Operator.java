package expression;

import java.util.Vector;

/**
 * A class representing a generic operator in an expression tree.
 * All operators, such as addition, subtraction, etc. are instances 
 * of this class.
 * 
 * @author Killian
 *
 */
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
	
	public static abstract class Function extends Operator {
		public abstract int getArity();
		
		public abstract Number safeEval(Vector<Number> children);
		
		@Override
		public Number evaluate(Vector<Number> children) {
			if (children.size() != getArity())
				throwBadArguments();
			return safeEval(children);
		}
		
		@Override
		public String format(Vector<String> children) {
			if (children.size() != getArity())
				throwBadArguments();
			String s = getSymbol() + "(";
			for (int i = 0 ; i < children.size(); i++) {
				s += children.get(i);
				if (i != (children.size() - 1)) 
					s += ", ";
			}
			s += ")";
			return s;
		}
	}
	
	public static class LogBase extends Function {
		@Override
		public int getArity() {
			return 2;
		}

		@Override
		public Number safeEval(Vector<Number> children) {
			return Number.log(children.get(0), children.get(1));
		}

		@Override
		public String getSymbol() {
			return "log";
		}
	}
	
	public static class Root extends Function {
		@Override
		public int getArity() {
			return 2;
		}

		@Override
		public Number safeEval(Vector<Number> children) {
			return Number.root(children.get(0), children.get(1));
		}

		@Override
		public String getSymbol() {
			return "root";
		}
	}
	
	public static abstract class UnaryFunction extends Function {

		@Override
		public int getArity() {
			return 1;
		}
		
		@Override
		public Number safeEval(Vector<Number> children) {
			return evaluate(children.firstElement());
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
	
	public static class NaturalLog extends UnaryFunction {
		@Override
		public String getSymbol() {
			return "ln";
		}

		@Override
		public Number evaluate(Number a) {
			return a.ln();
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
	
	public static class SquareRoot extends UnaryFunction {
		@Override
		public String getSymbol() {
			return "sqrt";
		}
		
		@Override
		public Number evaluate(Number a) {
			return a.sqrt();
		}
	}
	
	public static class CubeRoot extends UnaryFunction {
		@Override
		public String getSymbol() {
			return "cbrt";
		}
		
		@Override
		public Number evaluate(Number a) {
			return a.cbrt();
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
