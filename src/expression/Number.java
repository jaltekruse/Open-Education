package expression;

/** A generic class representing literal numbers.
 * Currently represented by a {@code double} value.
 * 
 * @author Killian
 */
public class Number extends Value {
	
	private double value;
	
	public Number(double value) {
		if (Double.isInfinite(value) || Double.isNaN(value))
			throw new NumericException("Invalid number format or operation");
		this.value = value;
	}
	
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Number))
			return false;
		return (this.value == ((Number) other).value);
	}
	
	@Override
	public int hashCode() {
		return Double.valueOf(value).hashCode();
	}

	@Override
	public String toString() {
		return value + ""; // change this!!
	}

	public Node replace(Identifier x, Node n) {
		return this;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public Number add(Number o) {
		return new Number(this.value + o.value);
	}
	
	public Number subtract(Number o) {
		return new Number(this.value - o.value);
	}

	public Number multiply(Number o) {
		return new Number(this.value * o.value);
	}
	
	public Number divide(Number o) {
		return new Number(this.value / o.value);
	}

	public Number exponent(Number o) {
		return new Number(Math.pow(this.value, o.value));
	}

	public Number negate() {
		return new Number(- this.value);
	}
	
	public Number log() {
		return new Number(Math.log10(this.value));
	}
	
	public Number sin() {
		return new Number(Math.sin(this.value));
	}
	
	public Number cos() {
		return new Number(Math.sin(this.value));
	}
	
	public Number tan() {
		return new Number(Math.tan(this.value));
	}

	public static boolean isNumeric(char c) {
		return Character.isDigit(c) || c == '.';
	}

	public static Number parseNumber(String s) {
		try {
			return new Number(Double.parseDouble(s));
		} catch (NumberFormatException e) {
			throw new NumericException(e);
		}
	}
}
