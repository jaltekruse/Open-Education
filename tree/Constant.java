/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package tree;

/**
* A Constant is used to represent a mathematical constant. It extends
* from the {@link ValueWithName} which stores its name. Stored here are
* a double for its value.
*/
public class Constant extends NumberWithName {

	private Number value;
	private boolean isMaster;
	
	/**
	 * Constructor takes a string to pass to the superclass's constructor.
	 * The only other param is a double that is stored here. The precedence value is 
	 * hard coded.
	 * @param s
	 * @param val
	 */
	public Constant(String s, Number val) {
		super(s);
		value = val;
		
	}
	
	private void syncValWithConststorage(){
		value = getParser().getConstantList().getConstVal(getName());
	}

	/**
	 * Gets the value stored with this constant.
	 * 
	 * @return - a double represented by this constant
	 */
	public Number getValue() {
		if (isMaster()){
			return value;
		}
		syncValWithConststorage();
		return value;
	}

	/**
	 * Returns a string representation of this constant.
	 * 
	 * @return the name of the constant"
	 */
	public String toString() {
		String varInfo = new String();
		varInfo += getName();
//		varInfo += "[";
//		varInfo += getName();
//		varInfo += ": ";
//		varInfo += getValue();
//		varInfo += "]";
		return varInfo;
	}
	
	public Number eval(){
		syncValWithConststorage();
		return value;
	}
	
	public Expression add(Expression e) throws EvalException
	{
		syncValWithConststorage();
		return value.add(e);
	}

	public Expression subtract(Expression e) throws EvalException
	{
		syncValWithConststorage();
		return value.subtract(e);
	}
	
	public Expression multiply(Expression e) throws EvalException
	{
		syncValWithConststorage();
		return value.multiply(e);
	}
	
	public Expression divide(Expression e) throws EvalException
	{
		syncValWithConststorage();
		return value.divide(e);
	}

	public Expression power(Expression e) throws EvalException
	{
		syncValWithConststorage();
		return value.power(e);
	}

	@Override
	public Expression sin() throws EvalException
	{
		syncValWithConststorage();
		return value.sin();
	}

	@Override
	public Expression cos() throws EvalException {
		// TODO Auto-generated method stub
		syncValWithConststorage();
		return value.cos();
	}

	@Override
	public Expression tan() throws EvalException
	{
		syncValWithConststorage();
		return value.tan();
	}

	@Override
	public Expression invSin() throws EvalException
	{
		syncValWithConststorage();
		return value.invSin();
	}

	@Override
	public Expression invCos() throws EvalException
	{
		syncValWithConststorage();
		return value.invCos();
	}

	@Override
	public Expression invTan() throws EvalException
	{
		syncValWithConststorage();
		return value.invTan();
	}

	@Override
	public Expression neg() throws EvalException
	{
		syncValWithConststorage();
		return value.neg();
	}

	@Override
	public Decimal toDec() throws EvalException
	{
		syncValWithConststorage();
		return value.toDec();
	}

	@Override
	public Expression squareRoot() throws EvalException
	{
		syncValWithConststorage();
		return value.squareRoot();
	}

	@Override
	public Expression log() throws EvalException
	{
		syncValWithConststorage();
		return value.log();
	}

	@Override
	public Expression natLog() throws EvalException
	{
		syncValWithConststorage();
		return value.natLog();
	}
	
	public Expression absoluteValue() throws EvalException
	{
		syncValWithConststorage();
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
