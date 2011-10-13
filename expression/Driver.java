package expression;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ParseException {
		NodeParser p = new NodeParser();
		p.allowLongIdentifiers(false);
		p.treatIdentifiersAsVariables(true);
		Node n = p.parseNode("5^(x+3)+2");
		System.out.println(n);
		System.out.println(n.simplify());
	}
}