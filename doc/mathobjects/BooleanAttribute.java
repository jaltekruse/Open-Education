/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import doc.DocViewerPanel;

public class BooleanAttribute extends MathObjectAttribute<Boolean> {
	
	public BooleanAttribute(String n){
		super(n);
	}

	public String export(){
		return "<attribute type=\"Integer\" name=\"" + getName() + "\"" +
				"value=\"" + getValue().toString() + "\">";
	}
	
	public void setValueWithString(String s) throws AttributeException{
		if (s.equals("true")){
			setValue(true);
		}
		else if (s.equals("false")){
			setValue(false);
		}
		else{
			throw new AttributeException(getName() + " must be 'true' or 'false' without single quotes");
		}
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return BOOLEAN_ATTRIBUTE;
	}
}
