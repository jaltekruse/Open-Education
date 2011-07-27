/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package expression;

public class Identifier extends Value {

	private String identifier;
	
	public Identifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Identifier))
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
		this.identifier = identifier;
	}

	public static boolean isValidChar(char c) {
		return Character.isLetter(c);
	}
}
