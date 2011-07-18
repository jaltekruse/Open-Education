package expression;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeParser parser = new NodeParser();
		parser.allowEmptyArguments(true);
		String p = "x^(y^(z^0-1+w-w)-1)-1";
		Node n = parser.parseNode(p);
		Node simplified = n.numericSimplify();
		Node smartSimplified = n.smartNumericSimplify();
		Node likeTerms = n.collectLikeTerms();
		Node s = n.simplify();
		System.out.println(n);
		System.out.println(simplified);
		System.out.println(smartSimplified);
		System.out.println(likeTerms);
		System.out.println(s);
	}
}