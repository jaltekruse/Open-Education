/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui.attributes;

import doc.mathobjects.AttributeException;

public class DoubleAttribute extends MathObjectAttribute<Double> {

	private double minimum, maximum;
	
	public static final double LIMIT_NOT_SET = Double.MAX_VALUE;
	
	public DoubleAttribute(String n, double min, double max) {
		super(n);
		minimum = min;
		maximum = max;
	}
	
	public DoubleAttribute(String n, double val, double min, double max) {
		super(n);
		setValue(val);
		minimum = min;
		maximum = max;
	}
	
	public DoubleAttribute(String n){
		super(n);
		minimum = LIMIT_NOT_SET;
		maximum = LIMIT_NOT_SET;
	}
	
	public DoubleAttribute(String n, double val){
		super(n);
		minimum = LIMIT_NOT_SET;
		maximum = LIMIT_NOT_SET;
		setValue(val);
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	public double getMinimum() {
		return minimum;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return DOUBLE_ATTRIBUTE;
	}

	@Override
	public Double readValueFromString(String s) throws AttributeException {
		// TODO Auto-generated method stub
		try{
			double val = Double.parseDouble(s);
			if ( (val >= minimum || minimum == LIMIT_NOT_SET ) &&
					( val <= maximum || maximum == LIMIT_NOT_SET) ){
				return val;
			}
			else{
				throw new AttributeException(getName() + " must be an decimal in the range (" + 
					minimum + " - " + maximum + ")");
			}
		}catch(Exception e){
			throw new AttributeException(getName() + " must be an decimal in the range (" + 
					minimum + " - " + maximum + ")");
		}
	}
}
