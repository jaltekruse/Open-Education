/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

public class DoubleAttribute extends MathObjectAttribute<Double> {

	private double minimum, maximum;
	
	public DoubleAttribute(String n, double min, double max) {
		super(n);
		minimum = min;
		maximum = max;
	}
	
	public DoubleAttribute(String n){
		super(n);
		minimum = -100000.0;
		maximum = Double.MAX_VALUE;
	}

	public String export(){
		return "<attribute type=\"Integer\" name=\"" + getName() + "\"" +
				"value=\"" + getValue().toString() + "\">";
	}

	public void setValueWithString(String s) throws AttributeException{
		try{
			double val = Double.parseDouble(s);
			if (val >= minimum && val <= maximum){
				setValue(val);
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
}
