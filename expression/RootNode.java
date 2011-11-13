package expression;

import java.util.Vector;

/**
 * Wrapper class for a an entire expression tree. Holds information that applies to the entire tree.
 * 
 * @author jason
 *
 */
public class RootNode {

	private Node root;
	
	private Vector<String> identifiersWithValues;

	private Vector<Number> identifierValues;

	public RootNode(Node n){
		root = n;
		identifiersWithValues = new Vector<String>();
		identifierValues = new Vector<Number>();
	}
	
	public void setIdentifierValue(String s, Number n){
		identifiersWithValues.add(s);
		identifierValues.add(n);
	}
	
	public void setRoot(Node root) {
		this.root = root;
	}

	public Node getRoot() {
		return root;
	}
	
	
}
