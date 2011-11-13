package expression;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		NodeParser parser = new NodeParser();
//		Node p = parser.parseNode("4q+3");
//		Variable x = new Variable("x");
//		p = p.replace(new Identifier("q"), x);
//		Function f = new Function(p, x);
//		System.out.println(p);
//		Node a = f.apply(Number.get(5));
//		System.out.println(a.simplify());
		
//		ProblemGenerator g = new ProblemGenerator();
//		System.out.println(g.generateLinear());

		try {
			Node.parseNode("5^(3+x)=5").printTree();

			Node n = Node.parseNode("3+5+4");
			System.out.println( ((Expression)n).getChildren().size() );
		}catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}