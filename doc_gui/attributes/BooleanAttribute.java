/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui.attributes;

import doc.mathobjects.AttributeException;

public class BooleanAttribute extends MathObjectAttribute<Boolean> {
	
	public BooleanAttribute(String n){
		super(n);
	}
	
	public BooleanAttribute(String n, boolean b){
		super(n);
		setValue(b);
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return BOOLEAN_ATTRIBUTE;
	}

	@Override
	public Boolean readValueFromString(String s) throws AttributeException {
		// TODO Auto-generated method stub
		if (s.equals("true")){
			return true;
		}
		else if (s.equals("false")){
			return false;
		}
		throw new AttributeException(getName() + " must be 'true' or 'false' without single quotes");
	}
}
