/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package tree;

import java.util.*;

public class ExpressionParser {
	
	//an expression to keep track of the current position in the tree
	private Expression e;
	
	//temporary storage for values found that have not been attached to an expression 
	private ArrayList<Expression> vals;
	
	//persistent storage of variables, for more info see Var and Varstorage classes
	public VarStorage VARLIST;
	
	//persistent storage of constants, for more info see Constant and ConstantStorage classes
	public ConstantStorage CONSTLIST;
	
	private int currCharNum, elementCount;
	private char currChar;
	
	//counter for the number of parenthesis found, if open paren found add one, if close paren found subtract one
	private int matchedParens;
	
	//used to keep track of the units for angle measures, impacts the trig function evaluation
	private int angleUnits;
	public static final int RAD = 1;
	public static final int DEG = 2;
	public static final int GRAD = 3;
	
	//was used for creating a history, so the date last modified could be stored alongside results
	private static final String dateModified = "2-25-2011";

	//this value stores the number of characters in the string input that
	//an element takes up, other elements that are automatically added by
	//the parser, such as multiplications in expressions like 5(x), do not affect it
	private int lengthLast;

	/** 
	 * Creates a new <code> ExpressionParser</code> sets the angle units to Radians.
	 */
	public ExpressionParser() {
		lengthLast = 0;
		vals = new ArrayList<Expression>();
		VARLIST = new VarStorage(this);
		CONSTLIST = new ConstantStorage();
		angleUnits = RAD;
		
		//associates this object with all of the Decimal objects, so they can
		//find the current angleUnits
		Decimal staticDec = new Decimal(this);
	}

	public String getDateModified(){
		return dateModified;
	}
	
	/**
	 * Takes the equation to be parsed and returns the expression object representation of it.
	 * 
	 * @param eqtn - string to be parsed
	 * @throws ParseException  
	 */
	public Expression ParseExpression(String eqtn) throws ParseException{
		if(eqtn.equals("")){
			throw new ParseException("Empty expression entered");
		}
		
		matchedParens = 0;
		e = null;
		currCharNum = 0;
		elementCount = 0;
		currChar = eqtn.charAt(currCharNum);
		vals = new ArrayList<Expression>();
		Expression root;

		//the main loop
		while (currCharNum <= eqtn.length() - 1) {
			parseElement(eqtn, currCharNum);
			elementCount++;
			//System.out.println("lengthLast: " + lengthLast);
			currCharNum += lengthLast;
			
			//uncomment the next lines to print out the expression 
			//as the loop executes
//			if (e != null){
//				root = e;
//				while (root.hasParent()){
//					root = root.getParent();
//				}
//				System.out.println(root.toString());
//			}
		}
		if (matchedParens == 1)
		{//there was one open paren that did not get close, assume one at the end
			hitCloseParen();
		}
		
		
		else if (matchedParens != 0){
			throw new ParseException("Did not match parenthesis");
		}
		if (vals.size() == 1){
			return vals.get(0);
		}
		if (e == null){
			throw new ParseException("invalid expression");
		}
		if (e instanceof BinExpression){
			if (((BinExpression)e).getRightChild() == null){
				addValue(new MissingValue());
			}
		}
		if (e instanceof UnaryExpression){
			if (((UnaryExpression)e).getChild() == null){
				addValue(new MissingValue());
			}
		}
		while(e.hasParent()){
			e = e.getParent();
		}
//		System.out.println("end parsing:" + e.toString());
		return e;
	}
	
	/**
	 * Takes the string currently being parsed, and the position of
	 * the next character that needs to be parsed.
	 * 
	 * @param s - the string being parsed
	 * @throws ParseException 
	 */
	private void parseElement(String s, int pos) throws ParseException{
		
		currChar = s.charAt(pos);
		lengthLast = 1;
		//System.out.println("currChar: " + currChar);
		switch(currChar){
		case '+':
			addBinOp(Operator.ADD);
			break;
		case '-':
			if (vals.isEmpty())
			{// all values that have been read have been incorporated into the tree
				//therefore there is no left child for a subtraction, interpret as negation
				addUnaryOp(Operator.NEG);
				return;
			}
			else{
				addBinOp(Operator.SUBTRACT);
			}

			break;
		case '*':
			if (currCharNum < s.length() - 1 && s.charAt(currCharNum + 1) == '*'){
				lengthLast += 1; 
				addBinOp(Operator.POWER);
				break;
			}
			else{
				addBinOp(Operator.MULTIPLY);
				break;
			}
		case '/':
			addBinOp(Operator.DIVIDE);
			break;
		case '=':
			addBinOp(Operator.ASSIGN);
			break;
		case '^':
			addBinOp(Operator.POWER);
			break;
		case '!':
			if(vals.size() == 1 || (e instanceof UnaryExpression) && ((UnaryExpression)e).hasChild())
				addUnaryOp(Operator.FACT);
			else
				addUnaryOp(Operator.NOT);
			break;
		case '(':
			matchedParens++;
			if (vals.size() == 1){
				addBinOp(Operator.IMP_MULT);
			}
			addUnaryOp(Operator.PAREN);
			break;
		case ')':
			matchedParens--;
			hitCloseParen();
			break;
		}

		if ((currChar <= '9' && currChar >= '0') || currChar == '.') {
			scanNum(s, currCharNum);
			return;
		}
		
		else if ((currChar <= 'Z' && currChar >= 'A')
				|| (currChar <= 'z' && currChar >= 'a') || currChar == '_') {
			scanVar(s, pos);
			return;
		}

		else if (currChar == ' ') {
			;
		}
		else { //if hit any unrecognized character, skip it and go on
			;
		}
	}
	
	private void hitCloseParen() throws ParseException {
		// TODO Auto-generated method stub
		if (e.isContainerOp())
		{
			if (vals.size() == 1){
				((UnaryExpression)e).setChild(vals.get(0));
				vals = new ArrayList<Expression>();
				return;
			}
			else if (((UnaryExpression)e).getChild() == null)
			{
				((UnaryExpression)e).setChild(new MissingValue());
				return;
			}
		}
		else if (e instanceof BinExpression)
		{
			if (((BinExpression)e).getRightChild() == null)
			{
				addValue(new MissingValue());
			}
		}
		int numParensHit = 0;
		while (e.hasParent() && numParensHit < 1)
		{
			e = e.getParent();
			if (e != null && e.getOperator() == Operator.PAREN){
				numParensHit++;;
			}
		}
		if (numParensHit == 2)
			e = ((UnaryExpression)e).getChild();
		
	}

	/**
	 * Scans a Number. Takes the string being parsed, and the position at which
	 * to begin. Actual scanning is done by the standard java Double scan
	 * function. Length of the number in the string is determined in a basic
	 * loop.
	 * 
	 * @param s - string to parse
	 * @param pos - current position
	 * @return a Decimal object
	 * @throws ParseException 
	 */
	public void scanNum(String s, int pos) throws ParseException {
		int length = 0, numDecimalPts = 0;
		boolean hasPowOfTen = false, hasNegPower = false;
		
		//this loop determines the length of the number, it is scanned
		//in by a standard Java function
		for (int i = 0; pos + i < s.length(); i++) {

			currChar = s.charAt(pos + length);
			if ((currChar >= '0' && currChar <= '9') || currChar == '.'
					|| currChar == 'E' || currChar == '-') {
				length++;
				if (currChar == '.'){
					if (numDecimalPts > 0){
						throw new ParseException("number formatted improperly, too many \".\"");
					}
					else{
						numDecimalPts++;
					}
				}
				if ((currChar == 'E' || currChar == '.') && hasPowOfTen) {
					length--;
					break;
				} else if (currChar == 'E' && !hasPowOfTen)
					hasPowOfTen = true;
				else if (currChar == '-') {
					if (hasPowOfTen && !hasNegPower) {
						hasNegPower = true;
					} else {
						length--;
						break;
					}
				}
			}
			else
				break;
		}
		if (s.substring(pos, pos+ length).equals(".")){
			throw new ParseException("Error with a '.'");
		}
		lengthLast = length;
		double number = Double.parseDouble(s.substring(pos, pos + length));
		Decimal newNum = new Decimal(number);
		if (!hasPowOfTen && length < 9 && number % 1 == 0){
			Expression newFrac = Fraction.Dec2Frac(newNum);
			addValue(newFrac);
		}
		else{
			addValue(newNum);
		}
	}
	
	/**
	 * Scans a Variable. Checks if it is an existing variable or constant
	 * and if it is adds the respective element to the expression.
	 * addNewValue
	 * @param s - string to parse
	 * @param pos - current position
	 * @return an Operator object
	 * @throws ParseException 
	 */

	public void scanVar(String s, int pos) throws ParseException{
		int length = 0;

		for (int i = 0; pos + i < s.length(); i++) {

			currChar = s.charAt(pos + length);
			if ((currChar <= 'Z' && currChar >= 'A')
					|| (currChar <= 'z' && currChar >= 'a')
					|| (currChar == '_')
					|| (currChar <= '9' && currChar >= '0'))
				length++;
			else
				break;
		}
		lengthLast = length;
		
		String varElm = s.substring(pos, pos + length);
		
		//adds "-1" to the operator for inverse trig functions
		if ("sin".equals(varElm) || "cos".equals(varElm)
				|| "tan".equals(varElm)) {
			if(s.length() - pos > 3){
				if (s.charAt(pos + length) == '-'
						&& s.charAt(pos + length + 1) == '1') {
					varElm += "-1";
					pos += 2;
					lengthLast += 2;
				}
			}
		}
		
		if(varElm.equals("sin")){
			addUnaryOp(Operator.SIN);
			return;
		}
		else if(varElm.equals("cos")){
			addUnaryOp(Operator.COS);
			return;
		}
		else if(varElm.equals("tan")){
			addUnaryOp(Operator.TAN);
			return;
		}
		else if(varElm.equals("sin-1")){
			addUnaryOp(Operator.INV_SIN);
			return;
		}
		else if(varElm.equals("cos-1")){
			addUnaryOp(Operator.INV_COS);
			return;
		}
		else if(varElm.equals("tan-1")){
			addUnaryOp(Operator.INV_TAN);
			return;
		}
		else if(varElm.equals("log")){
			addUnaryOp(Operator.LOG);
			return;
		}
		else if(varElm.equals("ln")){
			addUnaryOp(Operator.LN);
			return;
		}
		else if(varElm.equals("neg")){
			addUnaryOp(Operator.NEG);
			return;
		}
		else if(varElm.equals("int")){
			addUnaryOp(Operator.INT);
			return;
		}
//		else if(varElm.equals("integral")){
//			addUnaryOp(Operator.INTEGRAL);
//			return;
//		}
		else if(varElm.equals("floor")){
			addUnaryOp(Operator.FLOOR);
			return;
		}
		else if(varElm.equals("ceil")){
			addUnaryOp(Operator.CEILING);
			return;
		}
		else if(varElm.equals("sqrt")){
			addUnaryOp(Operator.SQRT);
			return;
		}
		else if(varElm.equals("abs")){
			addUnaryOp(Operator.ABS);
			return;
		}
		else if(varElm.equals("frac")){
			//think of how to add things like this, functions/values with multiple inputs
			//such as frac(2,3)
		}
		else if(varElm.equals("solve")){
			//will be the evaluate algebraically function, not quite done yet...
		}
		else{
			Constant tempElm = (Constant) CONSTLIST.findIfStored(varElm);
			if (tempElm != null){ //if name associated with a current Constant
				//make another constant with the same name
				tempElm = new Constant(tempElm.getName(), null);
				addValue(tempElm);
				return;
			}
			
			Var newVar = VARLIST.storeVar(varElm, null);
			newVar = new Var(newVar.getName(), null);
			newVar.setMaster(false);
			addValue(newVar);
		}
	}
	
	public void addValue(Expression v) throws ParseException{
		if(e instanceof BinExpression){
			if(((BinExpression)e).getRightChild() == null){
				((BinExpression)e).setRightChild(v);
				return;
			}
			else{
				addBinOp(Operator.IMP_MULT);
				addValue(v);
				return;
			}
		}
		else if(e instanceof UnaryExpression){
			if (vals.size() == 1 || ((UnaryExpression)e).hasChild()){
				addBinOp(Operator.IMP_MULT);
				addValue(v);
				vals = new ArrayList<Expression>();
				return;
			}
			else if (e.isContainerOp()){
				vals.add(v);
				return;
			}
			else if(((UnaryExpression)e).getChild() == null){
				((UnaryExpression)e).setChild(v);
				return;
			}
			else{
				throw new ParseException("Two values following a unary operator");
			}
		}
		else if(vals.size() == 1){
			addBinOp(Operator.IMP_MULT);
			addValue(v);
			return;
		}
		vals.add(v);
	}

	public void addBinOp(Operator o) throws ParseException{
		BinExpression newEx = new BinExpression(o);
		
		if (e instanceof BinExpression  && ((BinExpression)e).getRightChild() == null)
		{// there are two binary operators adjacent, add a missing value node between them
			addValue(new MissingValue());
		}
		if (e instanceof UnaryExpression && ((UnaryExpression)e).getChild() == null)
		{
			if (vals.size() == 1 && e.isContainerOp())
			{// a value is in temporary storage, and the last element that was added to the tree
				//was a container, add the new binary expression to 
				newEx.setLeftChild(vals.remove(0));
				((UnaryExpression)e).setChild(newEx);
				e = newEx;
				vals = new ArrayList<Expression>();
				return;
			}
			else
			{
				if (vals.size() == 0)
				{
					addValue(new MissingValue());
				}
				((UnaryExpression)e).setChild(newEx);
				
				newEx.setLeftChild(vals.get(0));
				vals = new ArrayList<Expression>();
				e = newEx;
				return;
			}
		}
		else{
			newEx = new BinExpression(o);
			if(e == null){
				if (o != Operator.ASSIGN){
					e = newEx;
					if (vals.size() == 1){
						newEx.setLeftChild(vals.get(0));
					}
					else
					{
						newEx.setLeftChild(new MissingValue());
					}
					vals = new ArrayList<Expression>();
					return;
				}
				else{//it is a assignment used properly
					e = newEx;
					if (vals.size() == 1){
						newEx.setLeftChild(vals.get(0));
					}
					else
					{
						newEx.setLeftChild(new MissingValue());
					}
					vals = new ArrayList<Expression>();
					return;
				}
			}
//			if(e.getOp() == null){
//				if (vals.size() == 1){
//					e = newEx;
//					newEx.setLeftChild(vals.get(0));
//					vals = new ArrayList<Value>();
//					return;
//				}
//			}
			if (vals.size() == 1){
				newEx.setLeftChild(vals.get(0));
				vals = new ArrayList<Expression>();
			}
			else{
				if (newEx.getOperator().getPrec() > e.getOperator().getPrec())
				{// the precedence of the new operation is greater than that
					//of the last added to the tree
					if (e instanceof BinExpression){
						newEx.setLeftChild(((BinExpression)e).getRightChild());
						((BinExpression)e).setRightChild(newEx);
						e = newEx;
						return;
					}
					else if (e instanceof UnaryExpression)
					{//
						newEx.setLeftChild(e);
						e = newEx;
						return;
					}
				}
				else {
					while(e.hasParent() && newEx.getOperator().getPrec() < e.getOperator().getPrec()
							&& !(e.getParent().isContainerOp())){
						e = e.getParent();
					}
					if (e instanceof BinExpression)
					{
						if (e.getOperator().getPrec() < newEx.getOperator().getPrec())
						{
							(newEx).setLeftChild(((BinExpression)e).getRightChild());
							((BinExpression)e).setRightChild(newEx);
							e = newEx;
							return;
						}
						else
						{
							if (e.hasParent())
							{
								Expression parent = e.getParent();
								if (parent instanceof BinExpression){
									((BinExpression) parent).setRightChild(newEx);
								}
								else if(parent instanceof UnaryExpression){
									((UnaryExpression) parent).setChild(newEx);
								}
							}
							newEx.setLeftChild(e);
							e = newEx;
							return;
						}
					}
					else if (e instanceof UnaryExpression){
						if (e.hasParent())
						{
							Expression parent = e.getParent();
							if (parent instanceof BinExpression){
								((BinExpression) parent).setRightChild(newEx);
							}
							else if(parent instanceof UnaryExpression){
								((UnaryExpression) parent).setChild(newEx);
							}
						}
						newEx.setLeftChild(e);
						e = newEx;
						return;
					}
				}
			}
		}
	}
	
	public void addUnaryOp(Operator o) throws ParseException{
		UnaryExpression newEx = new UnaryExpression(o);
		
		if (e == null){
			if (o.isUnaryPost()){
				newEx.setChild(vals.get(0));
				vals = new ArrayList<Expression>();
				e = newEx;
				return;
			}
			if (vals.size() == 1){
				addBinOp(Operator.IMP_MULT);
				addUnaryOp(o);
				return;
			}
			e = newEx;
			vals = new ArrayList<Expression>();
			return;
		}
		
		if (e instanceof BinExpression  && ((BinExpression)e).getRightChild() == null){
			//System.out.println("right child null");
			((BinExpression)e).setRightChild(newEx);
			e = newEx;
			return;
		}
		if (e instanceof BinExpression  && ((BinExpression)e).getRightChild() != null){
			if (o == Operator.NOT){
				newEx.setChild(((BinExpression)e).getRightChild());
				vals = new ArrayList<Expression>();
				newEx.setOperator(Operator.FACT);
				((BinExpression)e).setRightChild(newEx);
				e = newEx;
				return;
			}
			addBinOp(Operator.IMP_MULT);
			addValue(newEx);
			e = newEx;
			return;
		}
		else if (e instanceof UnaryExpression && newEx.isContainerOp()){
			if (vals.size() == 1){
				addBinOp(Operator.IMP_MULT);
				((BinExpression)e).setRightChild(newEx);
				e = newEx;
				return;
			}
			((UnaryExpression)e).setChild(newEx);
			e = newEx;
			return;
		}
		else if (e instanceof UnaryExpression){
			if (vals.size() == 1){
				addBinOp(Operator.IMP_MULT);
				((BinExpression)e).setRightChild(newEx);
				e = newEx;
				return;
			}
			else{
				if (((UnaryExpression)e).hasChild() && o.isUnaryPost()){
					vals = new ArrayList<Expression>();
					(newEx).setChild(e);
					e = newEx;
					return;
				}
				((UnaryExpression)e).setChild(newEx);
				e = newEx;
				return;
			}

		}
		else if (e instanceof UnaryExpression && ((UnaryExpression)e).getChild() == null){
			throw new ParseException("unary expression without a value");
		}
		else{
			if (vals.size() == 1){
				if (o.isUnaryPost()){
					newEx.setChild(vals.get(0));
					vals = new ArrayList<Expression>();
				}
				else{ //there is a value before the UnaryOp, such as with 4sin x
					addBinOp(Operator.IMP_MULT);
					addUnaryOp(o);
					return;
				}
			}
		}
	}
	
	/**
	 * Returns the length of the last element that was scanned.
	 * @return int - the length
	 */
	public int getLengthLast(){
		return lengthLast;
	}

	public VarStorage getVarList() {
		// TODO Auto-generated method stub
		return VARLIST;
	}

	public ConstantStorage getConstantList() {
		// TODO Auto-generated method stub
		return CONSTLIST;
	}

	public void setAngleUnits(int i) {
		// TODO Auto-generated method stub
		if (i != RAD || i != DEG || i != GRAD)
		{
			angleUnits = 1;
			return;
		}
		angleUnits = i;
	}
	
	public int getAngleUnits(){
		return angleUnits;
	}

}
