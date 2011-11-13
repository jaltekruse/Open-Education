package expression;

/**
 * The class representing variable names.
 * 
 * @author Killian Kvalvik
 *
 */
public class Identifier extends Value {

	private String identifier;
	
	public Identifier(String identifier) throws IdentifierException {
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
<<<<<<< HEAD
	public String toString() {
=======
	public String toStringRepresentation() {
		if ( displayParentheses()){
			return "(" + getIdentifier() + ")";
		}
>>>>>>> newParser
		return getIdentifier();
	}
	
	@Override
	public Identifier clone() {
		try {
<<<<<<< HEAD
			return new Identifier(identifier);
		} catch (IdentifierException e) {
=======
			Identifier i = new Identifier(identifier);
			i.setDisplayParentheses(displayParentheses());
			return i;
		} catch (NodeException e) {
			System.err.println("random stupid error, in class Identifier");
>>>>>>> newParser
			return null;
		}
	}

	@Override
	public Node replace(Identifier identifier, Node node) {
<<<<<<< HEAD
//		System.out.println("this identifer:" + this.identifier);
//		System.out.println("to replace with:" + identifier.identifier);
		if (this.equals(identifier)){
//			System.out.println("replacing");
=======
		if (this.equals(identifier)){
>>>>>>> newParser
			return node;
		}
		else
			return this;
	}

	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) throws IdentifierException {
		for (int i = 0 ; i < identifier.length(); i++) {
			if (!isValidChar(identifier.charAt(i)))
				throw new IdentifierException("Invalid identifier: " + identifier);
		}
		this.identifier = identifier;
	}

	public static boolean isValidChar(char c) {
		return Character.isLetter(c);
	}

	@Override
	protected int standardCompare(Node other) {
		if (other instanceof Number) {
			return 1;
		} else if (!(other instanceof Identifier)) {
			return -1;
		}
		return identifier.compareTo(((Identifier) other).getIdentifier());
	}
}
