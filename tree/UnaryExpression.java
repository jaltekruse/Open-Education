/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package tree;

public class UnaryExpression extends Expression {
	
	private Expression child;
	
	public UnaryExpression(Operator o){
		super(o);
	}
	
	public void setChild(Expression v){
		child = v;
		child.setParent(this);
	}
	
	public Expression getChild(){
		return child;
	}
	
	public boolean hasChild(){
		if (child != null)
			return true;
		return false;
	}
	
	
	@Override
	public Expression eval() throws EvalException{
		if(child != null){
			
			Expression childVal = child.eval();
			
			switch(op){
				case SIN:		
					return childVal.sin();
				case COS:
					return childVal.cos();
				case TAN:
					return childVal.tan();
				case INV_SIN:
					return childVal.invSin();
				case INV_COS:
					return childVal.invCos();
				case INV_TAN:
					return childVal.invTan();
				case PAREN:
					return childVal.eval();
				case NEG:
					return childVal.neg();
				case SQRT:
				case SQRT_SYMBOL:
					return childVal.squareRoot();
				case LOG:
					return childVal.log();
				case LN:
					return childVal.natLog();
				case ABS:
					return childVal.absoluteValue();
				default:
					throw new EvalException("unrecognized operation");
			}
		}
		throw new EvalException("a unary operator did not have a value");
	}
	
	@Override
	public String toString(){
		String result = new String();
//		System.out.println("op:");
		if (op != null)
			if (op == Operator.PAREN){
				result += "(";
			}
			else
			{
				result += op.getSymbol() + " "; 
			}
		if (child != null)
			result += child.toString();
		if (op == Operator.PAREN){
			result += ")";
		}
		return result;
	}
}
