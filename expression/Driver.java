package expression;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeParser parser = new NodeParser();
		parser.allowEmptyArguments(true);
		String p;
		p = "-x^(y+2z-5w*200000-3+5-z2+7/(sqrt(4)+cbrt(a^(2b-2b^0+7))+2+3+sin(0)*(57)))";
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
		System.out.println(n.replace(new Identifier("b"), new Number(2)).simplify());
	}
}