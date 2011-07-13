package expression;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeParser parser = new NodeParser();
		parser.allowEmptyArguments(true);
		String p = "log(2^3+4^5+e^x,)";
		Node n = parser.parseNode(p);
		System.out.println(n);
		System.out.println(n.numericSimplify());
//		parser.seekFromLast("log(2)", "log", 5);
	}
}
