package doc.expression_generators;

import java.util.UUID;

import doc.attributes.AttributeException;
import doc.attributes.Date;
import expression.Expression;
import expression.Node;

public class BasicAddition extends ExpressionGenerator {

	@Override
	protected Node generateExpression(int difficulty) {
		String[] ops = { ExUtil.ADDITION };
		String[] vars = {};
		int numOps;
		int maxAbsVal;
		int minGeneratedVal;
		int maxGeneratedVal;
		if ( difficulty  <= 10){
			numOps = 2;
			minGeneratedVal = 1;
			maxGeneratedVal = 12;
			maxAbsVal = 20;
		}
		else if ( difficulty <= 20){
			numOps = 4;
			minGeneratedVal = 5;
			maxGeneratedVal = 17;
			maxAbsVal = 60;
		}
		else {
			numOps = 4;
			minGeneratedVal = 9;
			maxGeneratedVal = 25;
			maxAbsVal = 100;
		}
		Node n = ExUtil.randomExpression(ops, vars, numOps, maxAbsVal, minGeneratedVal, 
				maxGeneratedVal, true, false, false, true);
		n = ExUtil.randomlyAddParenthesis( (Expression) n, 0, 3);
		return n;
	}

	@Override
	protected void setAttributes() {
		setName("Basic Addition");
		setAuthor("Open Notebook Staff");
		setDirections("Simplify.");
		try {
			this.setAttributeValue(UUID_STR, new UUID(2846184626069213495L, 3575720896865422970L));
			getTags().addValueWithString("Arithmetic");
			getTags().addValueWithString("Addition");
			getTags().addValueWithString("Integers");
		} catch (AttributeException e) {
			// should not be thrown
			System.out.println("error that should not happen in BaiscArithmaticExpression");
		}
		setDate(new Date(2,1,2011));
	}
}
