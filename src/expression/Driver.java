package expression;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeParser parser = new NodeParser();
		parser.allowEmptyArguments(true);
		String p = "1+log(y+2+3)+ log(y+5) -0x^2+53-27";
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