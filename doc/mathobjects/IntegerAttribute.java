/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import doc.DocViewerPanel;

public class IntegerAttribute extends MathObjectAttribute<Integer> {
	
	private int minimum, maximum;
	
	public IntegerAttribute(String n, int min, int max) {
		super(n);
		minimum = min;
		maximum = max;
	}
	
	public IntegerAttribute(String n){
		super(n);
		minimum = Integer.MIN_VALUE;
		maximum = Integer.MAX_VALUE;
	}

	public String export(){
		return "<attribute type=\"Integer\" name=\"" + getName() + "\"" +
				"value=\"" + getValue().toString() + "\">";
	}
	
	public void setValueWithString(String s) throws AttributeException{
		try{
			int val = Integer.parseInt(s);
			if (val >= minimum && val <= maximum){
				setValue(val);
			}
			else{
				throw new AttributeException(getName() + " must be an integer in the range (" + 
					minimum + " - " + maximum + ")");
			}
		}catch(Exception e){
			throw new AttributeException(getName() + " must be an integer in the range (" + 
					minimum + " - " + maximum + ")");
		}
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMinimum() {
		return minimum;
	}

	@Override
	public String getType() {
		return INTEGER_ATTRIBUTE;
	}
}
