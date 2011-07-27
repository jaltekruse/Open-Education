/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package expression;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class NodeParser {
	
	private boolean allowLongIdentifiers = false;
	
	private List<String> unaryFunctions =
			Arrays.asList("log", "sin", "cos", "tan");
	
	private List<String> identifiers = unaryFunctions;
	
	private List<String> additionOps = 
			Arrays.asList("+", "-");
	
	private List<String> multiplicationOps =
			Arrays.asList("*", "/", "", "-"); // unary negation
	
	private List<String> exponentOps =
			Arrays.asList("^");
	
	private List<String> binaryOps =
			Arrays.asList("+", "*", "", "/", "^"); // no thought, just split at index
	
	private List<String> operatorStrings =
			Arrays.asList("log", "sin", "cos", "tan", "+", "-", "*", "/", "-", "^");
	
	private int lowestPrecedence = 0;
	private int highestPrecedence = 3;
	
	/** List of open brackets. */
	private List<String> openBrackets =
			Arrays.asList("(", "[", "{");
	
	/** List of closed brackets. */
	private List<String> closeBrackets =
			Arrays.asList(")", "]", "}");
	
	private String exEmptyString = "Empty string or missing argument";
	
	public NodeParser() {
		
	}
	
	public boolean longIdentifiers() {
		return allowLongIdentifiers;
	}

	public void setLongIdentifiers(boolean allowLongIdentifiers) {
		this.allowLongIdentifiers = allowLongIdentifiers;
	}

	public List<String> getOperators(int level) {
		switch (level) {
			case 0:
				return additionOps;
			case 1:
				return multiplicationOps;
			case 2:
				return exponentOps;
			case 3:
				return unaryFunctions;
		}
		return null;
	}
	
	public Node parseNode(String expression) {
		expression = format(expression);
		return parse(expression);
	}
	
	private String format(String expression) {
		String s = expression.replaceAll("\\s", "");
		
		if (expression.equals("")) // string is whitespace
			throw new NodeException(exEmptyString);
		
		return s;
	}
	
	private Node parse(String expression) {
		return parse(expression, lowestPrecedence, expression.length() - 1);
	}
	
	private Node parse(String expression, int initialPrecedence, int offset) {
		if (openBrackets.contains(expression.charAt(0) + "") &&
				closeBrackets.contains(expression.charAt(expression.length() - 1) + ""))
			// expression is surrounded by parens
			return parseNode(expression.substring(1, expression.length() - 1));
		
		for (int precedence = initialPrecedence ; precedence <= highestPrecedence ; precedence++) {
			List<String> operators = getOperators(precedence);
			
			int[] lastPositions = new int[operators.size()];
			
			for (int i = 0 ; i < operators.size() ; i++) {		// get the last position of each operator
				if (precedence == initialPrecedence)
					lastPositions[i] = seekFromLast(expression, operators.get(i), offset);
				else
					lastPositions[i] = seekFromLast(expression, operators.get(i));
			}
			
			int max = 0;
			int maxPos = -1;
			int maxRightPos = -1;
			int rightPos = -1;
			for (int i = 0 ; i < lastPositions.length ; i++) {
				if (lastPositions[i] == -1)
					continue;
				rightPos = lastPositions[i] + operators.get(i).length();
				if ((rightPos > maxRightPos) || 
					((rightPos == maxRightPos) && (operators.get(max).length() < operators.get(i).length())))
				{
					maxPos = lastPositions[i];
					maxRightPos = rightPos;
					max = i;
				}
			}
			// maxPos now holds the position furthest to the right
			// of any operator in this precedence level
			
			if (maxPos == -1)
				continue;		// no operators of this precedence level
			
			String symbol = operators.get(max);
			int index = maxPos;
			
			if (binaryOps.contains(symbol)) {
				Vector<Node> children = splitAtIndex(expression, index, symbol.length());
				Operator o = null;
				if (symbol.equals("+"))
					o = new Operator.Addition();
				if (symbol.equals("*"))
					o = new Operator.Multiplication(false);
				if (symbol.equals(""))
					o = new Operator.Multiplication(true);
				if (symbol.equals("/"))
					o = new Operator.Division();
				if (symbol.equals("^"))
					o = new Operator.Exponent();
				
				return new Expression(o, children);
			}
			
			if (unaryFunctions.contains(symbol)) {
				if (index == 0) {
					Vector<Node> child = new Vector<Node>();
					child.add(parse(expression.substring(symbol.length()+1, expression.length()-1)));
					Operator o = null;
					if (symbol.equals("log"))
						o = new Operator.Logarithm();
					if (symbol.equals("sin"))
						o = new Operator.Sine();
					if (symbol.equals("cos"))
						o = new Operator.Cosine();
					if (symbol.equals("tan"))
						o = new Operator.Tangent();
					return new Expression(o, child);
				}
			}
			
			if (symbol.equals("-")) {
				if (operators == additionOps) {  // subtraction
					try {
						Vector<Node> children = splitAtIndex(expression, index, symbol.length());
						return new Expression(new Operator.Subtraction(), children);
					} catch (NodeException e) {
						if (e.getMessage().equals(exEmptyString)) {
							return parse(expression, 1, index - 1);
						}
						else
							throw e;
					}
				} else {								// negation
					Vector<Node> child = new Vector<Node>();
					child.add(parse(expression.substring(1)));
					return new Expression(new Operator.Negation(), child);
				}
			}
		}
		
		return Value.parseValue(expression);
	}
	
	private int seekFromLast(String expression, String symbol) {
		return seekFromLast(expression, symbol, expression.length() - 1);
	}

	public int seekFromLast(String expression, String symbol, int endpoint) {
		// no idea how this method works anymore (or if it even does)
		int depth = 0;
		boolean lastWasNumber = false;
		boolean lastWasLetter = false;
		boolean lastWasEnd = false;  // possibly no longer necessary
		boolean thisIsNumber;
		boolean thisIsLetter;
		for (int i = endpoint ; i >= 0 ; i--) {
			if (openBrackets.contains(expression.charAt(i) + ""))
				depth++;
			if (closeBrackets.contains(expression.charAt(i) + ""))
				depth--;
			if ((i == 0) && (symbol.length() == 0))
				break;
			if (symbol.length() == 0) {
				thisIsNumber = Number.isNumeric(expression.charAt(i-1));
				thisIsLetter = Identifier.isValidChar(expression.charAt(i-1));
				lastWasNumber = Number.isNumeric(expression.charAt(i));
				lastWasLetter = Identifier.isValidChar(expression.charAt(i));
			} else {
				thisIsNumber = Number.isNumeric(expression.charAt(i));
				thisIsLetter = Identifier.isValidChar(expression.charAt(i));
			}
			if ((depth == 0) && (i <= expression.length() - symbol.length())) {
				if (!(lastWasEnd && (symbol.length() == 0)) 
						&& !((i == 0) && (symbol.length() == 0)) // because this is just stupid
						&& !(lastWasNumber && thisIsNumber) 
						&& !(lastWasLetter && thisIsLetter && allowLongIdentifiers)) {
					boolean inIdentifier = false;
					for (String id : identifiers) {
						if (!symbol.contains(id))
							inIdentifier = inIdentifier || indexIn(expression, i, id);
						if (inIdentifier)
							break;
					}
					for (String op: operatorStrings) {
						if (!symbol.contains(op)) {
							inIdentifier = inIdentifier || indexIn(expression, i, op);
							int j = i - op.length();
							if (j >= 0) {
								inIdentifier = 
										inIdentifier || expression.substring(j, j+op.length()).equals(op);
							}
						}
						if (inIdentifier)
							break;
					}
					if (!inIdentifier) {
						if (symbol.equals(expression.substring(i, i + symbol.length())))
							return i;
					}
				}
			}
			
			lastWasEnd = false;
			lastWasNumber = Number.isNumeric(expression.charAt(i));
			lastWasLetter = Identifier.isValidChar(expression.charAt(i));			
		}
		return -1;
	}
	
	private boolean indexIn(String expression, int index, String identifier) {
		for (int j = Math.max(0, index - identifier.length() + 1) 
				; (j < (expression.length() - identifier.length())) && (j <= index) ; j++) {
			if (expression.substring(j, j + identifier.length()).equals(identifier))
				return true;
		}
		return false;
	}

	private Vector<Node> splitAtIndex(String expression, int index, int symbolLength) {
		Vector<Node> children = new Vector<Node>();
		children.add(parseNode(expression.substring(0, index)));
		children.add(parseNode(expression.substring(index + symbolLength)));
		return children;
	}
}
