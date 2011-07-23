package expression;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeParser parser = new NodeParser();
		Node p = parser.parseNode("-x^(y+2z+-5w*200000-3+5-z2+7/(sqrt(9)+ln(2y^1+x^0-1+x)-ln(x+y*2)+cbrt(a^(2b-2b^0+7))+2+3+sin(0)*(57)))");
		System.out.println(p);
		System.out.println(p.simplify());
	}
}