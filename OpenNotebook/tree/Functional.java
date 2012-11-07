/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package tree;

import java.util.ArrayList;

public abstract class Functional extends Expression {

		ArrayList<Expression> inputs;
		Operator op;
		
		public Functional(Operator o){
			super(o);
		}

		@Override
		public Expression eval() throws EvalException {
			// TODO Auto-generated method stub
			return null;
		}
}