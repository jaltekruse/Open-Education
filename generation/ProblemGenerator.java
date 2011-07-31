package generation;

import expression.*;
import expression.Number;
import java.util.Random;

public class ProblemGenerator {

	private double ceiling = 50;
	private double floor = -50;
	private boolean allowReals = false;
	
	private static final Function linearFunction;
	
	static {
		Variable a = new Variable();
		Variable b = new Variable();
		Node linear = Node.parseNode("ax+b");
		linear = linear.replace("a", a);
		linear = linear.replace("b", b);
		linearFunction = new Function(linear, a, b);
	}
	
	public Equation generateLinear() {
		Node line = linearFunction.apply(Number.get(random()), Number.get(random()));
		return new Equation(line.simplify(), Number.get(0));
	}

	private double random() {
		Random r = new Random();
		if (allowReals) {
			return (r.nextDouble() * (ceiling - floor) + floor);
		} else {
			return r.nextInt((int) (ceiling - floor + 1)) + (int) floor;
		}
	}
}
