package expression;

/**
 * Represents an empty or incomplete node.
 * 
 * @author Killian Kvalvik
 *
 */
public class EmptyValue extends Value {

	@Override
	public boolean equals(Object other) {
		return ((other != null) && (other instanceof EmptyValue));
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return "?";
	}

	@Override
	public Node replace(Identifier identifier, Node node) {
		return this;
	}

	@Override
	public EmptyValue clone() {
		return new EmptyValue();
	}

	@Override
	protected int standardCompare(Node other) {
		return 0;
	}

}
