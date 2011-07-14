package expression;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeParser parser = new NodeParser();
		parser.allowEmptyArguments(true);
		String p = "log(4,4^5)";
		Node n = parser.parseNode(p);
		System.out.println(n);
		System.out.println(n.numericSimplify());
	}
}
