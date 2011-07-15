/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

public class StringAttribute extends MathObjectAttribute<String>{

	public static final String type = "StringAttribute";
	
	public StringAttribute(String n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValueWithString(String s) {
		// TODO Auto-generated method stub
		setValue(s);
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}

}
