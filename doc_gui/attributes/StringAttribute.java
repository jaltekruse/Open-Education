/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui.attributes;

import doc.mathobjects.AttributeException;

public class StringAttribute extends MathObjectAttribute<String>{
	
	public StringAttribute(String n) {
		super(n);
		// TODO Auto-generated constructor stub
	}
	
	public StringAttribute(String n, String s) {
		super(n);
		setValue(s);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return STRING_ATTRIBUTE;
	}

	@Override
	public String readValueFromString(String s) throws AttributeException {
		// TODO Auto-generated method stub
		return s;
	}

}
