package expression;

import generation.ProblemGenerator;

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
		Node node = null;
		try {
			node = Node.parseNode("log(2,10)");

			for (int i = 0; i < 300 ; i ++){
				System.out.println(node.numericSimplify().toStringRepresentation());
			}
		}catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Node ex = null;
		try {
			ex = Node.parseNode("((3x)+(2x))");
			System.out.println(ex.replace("x", new Number(7)).toStringRepresentation());
		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Node n : ex.splitOnAddition()){
			try {
				System.out.println(n.toStringRepresentation());
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}