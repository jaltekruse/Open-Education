package expression;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeParser parser = new NodeParser();
		String p = "sin(10^3+9^3-12^3+x)";
		Node n = parser.parseNode(p);
		System.out.println(n);
		System.out.println(n.numericSimplify());
//		parser.seekFromLast("log(2)", "log", 5);
	}
}
