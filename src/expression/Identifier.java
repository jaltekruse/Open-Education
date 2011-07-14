package expression;

/**
 * The class representing variable names.
 * 
 * @author Killian Kvalvik
 *
 */
public class Identifier extends Value {

	private String identifier;
	
	public Identifier(String identifier) {
		setIdentifier(identifier);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (this.getClass() != o.getClass())
			return false;
		return this.identifier
				.equals(((Identifier) o).identifier);
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}

	@Override
	public String toString() {
		return getIdentifier();
	}
	
	@Override
	public Identifier clone() {
		return new Identifier(identifier);
	}

	@Override
	public Node replace(Identifier identifier, Node node) {
		if (this.equals(identifier))
			return node;
		else
			return this;
	}

	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		for (int i = 0 ; i < identifier.length(); i++) {
			if (!isValidChar(identifier.charAt(i)))
				throw new NodeException("Invalid identifier: " + identifier);
		}
		this.identifier = identifier;
	}

	public static boolean isValidChar(char c) {
		return Character.isLetter(c);
	}
}
