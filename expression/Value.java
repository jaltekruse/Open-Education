/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package expression;

public abstract class Value extends Node {
	
	@Override
	public Node collectLikeTerms() {
		// this means nothing for pure Values
		return this;
	}

	@Override
	public Node numericSimplify() {
		// this means nothing for pure Values
		return this;
	}

	public static Value parseValue(String expression) {
		try {
			return Number.parseNumber(expression);
		} catch (NumericException e) {
			return new Identifier(expression);
		}
	}
}
