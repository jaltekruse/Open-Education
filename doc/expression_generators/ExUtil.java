package doc.expression_generators;

import java.util.Random;

import expression.Expression;
import expression.Node;
import expression.NodeException;
import expression.Number;
import expression.Operator;
import expression.VarList;

public class ExUtil {

	private static Random rand = new Random();;

	public static String USE_INTEGERS = "integers";
	public static String USE_DECIALS = "deciamls";
	public static String USE_VARIABLES = "variables";

	public static String INTEGER_ANSWER = "intAnswer";
	public static String FRACTION_ANSWER = "fractionAnswer";
	public static String DECIMAL_ANSWER = "decimalAnswer";

	public static String ADDITION = "addition";
	public static String MULTIPLICATION = "multiplication";
	public static String DIVISION = "division";
	public static String SUBTRACTION = "subtraction";
	public static String NEGATION = "negation";
	public static String ABSOLUTE_VALUE = "absolute value";

	public static void main(String[] args){
		generateRandomExpressions();
	}

	public static String[] generateRandomExpressions(){
		String[] ops = { ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION };
		String[] vars = { "x", "y", "z", "a", "b", "c", "d", "s", "t", "w", "v", "m", "n"};
		int numTrials = 10;

		String[] expressions = new String[numTrials];
		int numZeros = 0;
		int numNegatives = 0;
		int minNumOps = 2;
		int maxNumOps = 5;
		int maxAbsVal = 20;
		int minGeneratedVal = 1;
		int maxGeneratedVal = 10;
		int numOps;
		System.out.println(7.84);
		System.out.println(2.8 * 2.8);

		for (int j = 0; j < numTrials; j++){
			numOps = (int) randomInt(minNumOps, maxNumOps, false);
			Node n = randomExpression(ops, vars, numOps, maxAbsVal, minGeneratedVal, 
					maxGeneratedVal, true, false, false, true);
			try {
				expressions[j] = n.toStringRepresentation();
				if ( ((Number)n.numericSimplify()).getValue() == 0 ){
					numZeros++;
				}
				if ( ((Number)n.numericSimplify()).getValue() < 0){
					numNegatives++;
				}
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println( numZeros + " out of " + numTrials + " trials evaluated to 0.");
		System.out.println(numNegatives + " out of " + numTrials + " trials evaluated to a negative.");
		return expressions;
	}

	public static Node randomExpression( String[] ops, String[] vars, int numOps,
			double maxAbsVal, double minNumVal, double maxNumVal, 
			boolean excludeZero, boolean subtractNegatives, boolean addNegatives, boolean indcludeFractions ){
		// integers, decimals, variables ( list of valid ), operations, number of Operations
		// answer type - will influence how operations are added

		// start with a number or a variable

		// to add complexity, generate another number within the bounds, or a variable, or an entire term
		// then choose an operation and randomly make the new operand come first or second in the operation

		// or, make the existing part of the tree one complete node, decide on an operation
		// and then start the process fresh to create another expression
		// for (x+y) / (x-y) ;  (x+5) + (3+(x-4))  and other situations with parenthesis

		// first trial, random expression using 4 major operations, and only integers from 1 to 12, integer answers
		// problems :
		// -----------
		// - division by 0, fixed
		// - numbers get too big :
		//		solution1 :
		//			if number is too big, make sure a division is the next thing added
		// 			make division require a small result, or try again to add a different
		//			operation ( could cause infinite loop) but would require it to keep
		//			picking multiplication with random number generator
		// 		solution2 :
		//			try generating expression through creating terms out of just numbers
		//			or a few simple multiplications of divisions and then randomly
		//			add or subtract them

		Node n;
		n = new Number( randomInt( minNumVal, maxNumVal, excludeZero));

		for (int i = 0; i < numOps; i++){
			n = addRandomOp( n, ops, vars, minNumVal, maxNumVal, maxAbsVal, excludeZero, subtractNegatives, addNegatives);
		}
		return n;
	}
	
	public Node randomPolynomial(int maxDegree, int minCoefficient, int maxCoefficient, int minNumberTerms,
			int maxNumberTerms, VarList variables){
		Node n = null;
		int numTerms = (int) randomInt( minNumberTerms, maxNumberTerms, true);

		for (int i = 0; i < numTerms; i++){
			// add a new term
		}

		return n;
	}

	public static double[] getFactors( double d){

		double[] factors = new double[0];
		int index = 0;

		for ( double num = 2; num < d / 2 + 1; d++){
			if ( d / num == 0){
				index = factors.length;
				System.arraycopy(factors, 0, new double[index + 1], 0, index);
				factors[index] = num;
			}
		}
		return factors;
	}

	public static int getPrec( Operator o){

		if ( o instanceof Operator.Addition || o instanceof Operator.Subtraction){
			return 1;
		}

		else if ( o instanceof Operator.Division || o instanceof Operator.Multiplication){
			return 2;
		}
		else return 0;
	}

	public static void addChild( Expression ex, Node n ){
		
		if ( n instanceof Number){
			Number numChild = (Number) n;
			if ( numChild.getValue() < 0)
			{// if the number is a negative add parenthesis around it
				n.setDisplayParentheses(true);
			}
		}

		if ( ex.getOperator() instanceof Operator.BinaryOperator){
			if ( ex.getChildren().size() == 0 || ex.getChild(0) == null)
			{ // there are no children added yet (this is the first operand of a binary operator) 

				if ( n instanceof Expression ){
					Expression childEx = (Expression) n;
					if ( getPrec( childEx.getOperator() ) < getPrec( ex.getOperator() ) )
					{ // the expression has a greater precedence than the
						// new child, it needs parenthesis
						n.setDisplayParentheses(true);
					}
				}
				ex.getChildren().add(n);
				return;
			}
			else
			{// there is already one operand, this is the second one added

				if ( n instanceof Expression ){
					Expression childEx = (Expression) n;
					if ( getPrec( childEx.getOperator() ) < getPrec( ex.getOperator() ) )
					{ // the child that was already added to the expression has a lesser precedence than the
						// new child, it does not need parenthesis
						n.setDisplayParentheses(true);
					}
				}
				ex.getChildren().add(n);
				return;

			}
		}
	}

	public static Node addRandomOp(Node n, String[] ops, String[] vars, double min, double max,
			double maxAbsVal, boolean excludeZero, boolean subtractNegatives, boolean addNegatives){

		int opIndex = rand.nextInt( ops.length );
		String opCode = ops[opIndex];
		Node newChild = new Number( randomInt(min, max, excludeZero) );
		Operator op = null;
		Number newNum;
		Expression newEx;
		double expressionVal = 0;
		boolean numberTooBig = false;

		try {
//			System.out.println(n.toStringRepresentation());
			expressionVal = ((Number)n.numericSimplify()).getValue();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if ( Math.abs(expressionVal) > maxAbsVal){
			numberTooBig = true;
		}

		if ( opCode.equals(DIVISION)){
			
			// getting too many divisions all of the time, expressions end up bizarre and tall with
			// horizontal display
			if ( rand.nextBoolean() ){
				return addRandomOp(n, ops, vars, min, max, maxAbsVal, excludeZero, subtractNegatives, addNegatives);
			}
			
			op = new Operator.Division();
			newEx = new Expression( op );

			if ( ! (newChild instanceof Number) ){
				// the new child being added is not a number, it will not have to be adjusted
				// to keep the answer clean
				if ( rand.nextBoolean() ){
					addChild(newEx, n);
					addChild(newEx, newChild);
				}
				else{
					addChild(newEx, newChild);
					addChild(newEx, n);
				}
				return newEx;
			}

			else if ( expressionVal == 0){
				newNum = (Number) newChild; 
				do{ 
					newChild = new Number( randomInt(min, max, excludeZero));
				} while( newNum.getValue() == 0);

				addChild(newEx, n);
				addChild(newEx, newChild);
			}
			else if ( isPrime( expressionVal ) || ( rand.nextBoolean() && ! numberTooBig) )
			{// the expression evaluates to a prime number, so it must be the divisor
				// or the expression was randomly selected to be the divisor when it wasn't prime
				newChild = new Number( expressionVal * randomInt(min, max, excludeZero));
				addChild(newEx, newChild);
				addChild(newEx, n);
			}
			else
			{// the expression evaluates to a non-prime number,and was randomly chosen to be the dividend
				// need to find a divisor
				if ( numberTooBig ){
					return addRandomOp(n, ops, vars, min, max, maxAbsVal, excludeZero, subtractNegatives, addNegatives);
				}
				double[] factors = getFactors(expressionVal);
				int factorIndex = rand.nextInt( factors.length );
				newChild = new Number( factors[factorIndex]);
				addChild(newEx, n);
				addChild(newEx, newChild);
			}

			return newEx;
		}
		else if ( opCode.equals(ADDITION)){
			op = new Operator.Addition();
			newEx = new Expression( op );

			if ( ! (newChild instanceof Number) ){
				// the new child being added is not a number, it will not have to be adjusted
				// to keep the answer clean
				if ( rand.nextBoolean() ){
					addChild(newEx, n);
					addChild(newEx, newChild);
				}
				else{
					addChild(newEx, newChild);
					addChild(newEx, n);
				}
				return newEx;
			}

			if ( ! addNegatives)
			{// negative numbers are not supposed to be added, generate new positive number

				if ( max < 0)
				{// the settings contradict one another, the user wanted only negative numbers generated
					//do nothing
				}
				else{
					// the minimum is below zero, so exclude those values between 0 and the minimum while finding a new number
					double tempMin = 0;
					newNum = (Number) newChild; 
					if ( min > 0)
					{
						tempMin = min;
					}
					while ( newNum.getValue() < 0){
						newNum = new Number( randomInt(tempMin, max, excludeZero) );
					}
				}
			}
		}
		else if ( opCode.equals(SUBTRACTION)){
			op = new Operator.Subtraction();
			newEx = new Expression( op );

			if ( ! (newChild instanceof Number) ){
				// the new child being added is not a number, it will not have to be adjusted
				// to keep the answer clean
				if ( rand.nextBoolean() ){
					addChild(newEx, n);
					addChild(newEx, newChild);
				}
				else{
					addChild(newEx, newChild);
					addChild(newEx, n);
				}
				return newEx;
			}

			if ( ! subtractNegatives)
			{// negative numbers are not supposed to be subtracted, generate new positive number
				if ( max < 0)
				{// the settings contradict one another, the user wanted only negative numbers generated
					//do nothing, ignore the value of subtractNegatives
				}
				else{
					// the minimum is below zero, so exclude those values between 0 and the minimum while finding a new number
					double tempMin = 0;
					newNum = (Number) newChild; 
					if ( min > 0)
					{
						tempMin = min;
					}
					while ( newNum.getValue() < 0){
						newNum = new Number( randomInt(tempMin, max, excludeZero) );
					}
				}
			}
		}
		else if ( opCode.equals(MULTIPLICATION)){
			op = new Operator.Multiplication();
			newEx = new Expression( op );

			if ( ! (newChild instanceof Number) ){
				// the new child being added is not a number, it will not have to be adjusted
				// to keep the answer clean
				if ( rand.nextBoolean() ){
					addChild(newEx, n);
					addChild(newEx, newChild);
				}
				else{
					addChild(newEx, newChild);
					addChild(newEx, n);
				}
				return newEx;
			}

//			newNum = (Number) newChild; 
//			if ( expressionVal > maxAbsVal)
//			{// a multiplication cannot be performed on the current expression without making the result too large
//				// try again
//				return addRandomOp(n, ops, vars, min, max, maxAbsVal, excludeZero, subtractNegatives, addNegatives);
//			}
//			while ( Math.abs(expressionVal * newNum.getValue()) > maxAbsVal){
//				newNum = new Number( randomInt(min, max, excludeZero) );
//			}

		}
		else{
			System.out.println("unknown op");
		}

		newEx = new Expression( op );
		if ( rand.nextBoolean() ){
			addChild(newEx, newChild);
			addChild(newEx, n);
		}
		else{
			addChild(newEx, n);
			addChild(newEx, newChild);
		}
		return newEx;
	}

	public static boolean isPrime( double d){

		if (d % 1 != 0){
			return true;
		}

		for (double i = 2 ; i < (d / 2) % 1 ; i++ ){
			if ( d / i == 0){
				return false;
			}
		}

		return true;
	}

	public Node randomFraction(int min, int max, boolean allowImproper, boolean allowZero){
		if ( min > max){
			// min must be smaller
			return null;
		}
		int denom = rand.nextInt(max - min) + min;
		Number denominator = new Number( denom );

		return null;
	}

	public static Node randomEquation(){

		return null;
	}

	public static Expression getExpression(String s){
		if (s.equals(ADDITION)){
			return new Expression(new Operator.Addition());
		}
		else if (s.equals(MULTIPLICATION)){
			return new Expression(new Operator.Multiplication());
		}
		else if (s.equals(DIVISION)){
			return new Expression(new Operator.Division());
		}
		else if (s.equals(SUBTRACTION)){
			return new Expression(new Operator.Subtraction());
		}
		return null;
	}

	public static double randomInt(double a, double b, boolean excludeZero){
		double randVal = rand.nextDouble() * ( b - a );
		double randNum = ( (int) (a + (int) Math.round(randVal) ) );
		if ( a < 0 && b > 0 && excludeZero){
			while ( randNum == 0){
				randNum = ( (int) (a + rand.nextDouble() * ( b - a )) );
			}
		}
		return randNum;

	}

	public static double randomDecimal(double a, double b, double round){
		double randNum = a + rand.nextDouble() * ( b - a);
		return randNum - randNum % round;

		//		double numberPossiblities = ( b - a )/ round;
		//		System.out.println(numberPossiblities);
		//		if (numberPossiblities < 100000){
		//			return a + round * rand.nextInt( (int) numberPossiblities);
		//		}
		//		else{
		//			return a + Math.random() * ( b - a );
		//		}
	}
}
