/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package tree;

public class Var extends Expression implements ValueWithName {

	private String name;
	private Number value;
	private boolean isMaster;
	
	public Var(){
	}
	
	public Var(String s, Number n) {
		name = s;
		value = n;
	}

	private void syncValWithVarstorage(){
		value = getParser().getVarList().getVarVal(name);
	}
	
	public Expression setValue(Number n) {
		if (isMaster()){
			return value = n;
		}
		return getParser().getVarList().setVarVal(name, n);
	}

	public Number getValue(){
		if (isMaster){
			return value;
		}
		syncValWithVarstorage();
		return value;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public Expression eval() throws EvalException{
		syncValWithVarstorage();
		if (value == null){
			throw new EvalException("Variable \"" + getName() + "\" has not been given a value");
		}
		return value;
	}
	
	public String toString() {
		String varInfo = new String();
		varInfo += name;
//		varInfo += "[";
//		varInfo += varString + " = ";
//		if (num != null)
//		{
//			varInfo += num.toString();
//		}
//		else
//		{
//			varInfo += "null";
//		}
//		varInfo += "]";
		return varInfo;
	}
	
	@Override
	public Expression assign(Expression e) throws EvalException {
	// TODO Auto-generated method stub
		if (e instanceof Var){
			return setValue(((Var) e).getValue());
		}
		else
		{
			//going through the VarStorage object to assign variables allows additional
			//actions to be triggered for all assignments, such as redrawing the graph
			//if the value of xMin, yStep, etc. was assigned in the terminal
			//return parser.getVarList().setVarVal(varString, val);
		
			//correction, I would have used the above line to produce the result that I was
			//describing, but this made graphing slow, we can find some way to make it check for
			//regraphing only when the value was not assigned by the graphing algorithm itself
			if (e instanceof Number){
				
				return setValue((Number) e);
			}
				throw new EvalException("A non-number value cannot be assigned to a variable");
		}
	}

	public void updateValue(double d) throws EvalException {
	// TODO Auto-generated method stub
		value = (Number) value.add(new Decimal(d));
	}

	public Expression add(Expression e) throws EvalException
	{
		syncValWithVarstorage();
		return value.add(e);
	}

	public Expression subtract(Expression e) throws EvalException
	{
		syncValWithVarstorage();
		return value.subtract(e);
	}
	
	public Expression multiply(Expression e) throws EvalException
	{
		syncValWithVarstorage();
		return value.multiply(e);
	}
	
	public Expression divide(Expression e) throws EvalException
	{
		syncValWithVarstorage();
		return value.divide(e);
	}

	public Expression power(Expression e) throws EvalException
	{
		syncValWithVarstorage();
		return value.power(e);
	}

	@Override
	public Expression sin() throws EvalException
	{
		syncValWithVarstorage();
		return value.sin();
	}

	@Override
	public Expression cos() throws EvalException {
		// TODO Auto-generated method stub
		syncValWithVarstorage();
		return value.cos();
	}

	@Override
	public Expression tan() throws EvalException
	{
		syncValWithVarstorage();
		return value.tan();
	}

	@Override
	public Expression invSin() throws EvalException
	{
		syncValWithVarstorage();
		return value.invSin();
	}

	@Override
	public Expression invCos() throws EvalException
	{
		syncValWithVarstorage();
		return value.invCos();
	}

	@Override
	public Expression invTan() throws EvalException
	{
		syncValWithVarstorage();
		return value.invTan();
	}

	@Override
	public Expression neg() throws EvalException
	{
		syncValWithVarstorage();
		return value.neg();
	}

	@Override
	public Decimal toDec() throws EvalException
	{
		syncValWithVarstorage();
		return value.toDec();
	}

	@Override
	public Expression squareRoot() throws EvalException
	{
		syncValWithVarstorage();
		return value.squareRoot();
	}

	@Override
	public Expression log() throws EvalException
	{
		syncValWithVarstorage();
		return value.log();
	}

	@Override
	public Expression natLog() throws EvalException
	{
		syncValWithVarstorage();
		return value.natLog();
	}
	
	public Expression absoluteValue() throws EvalException
	{
		syncValWithVarstorage();
		return value.absoluteValue();
	}

	@Override
	public boolean isMaster()
	{
		return isMaster;
	}

	@Override
	public void setMaster(boolean b)
	{
		isMaster = b;
	}
}
