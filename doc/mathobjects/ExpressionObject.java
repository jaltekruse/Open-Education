/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import doc.Page;

public class ExpressionObject extends MathObject {

	public ExpressionObject(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		getAttributeWithName("expression").setValue("");
		getAttributeWithName("fontSize").setValue(12);
	}

	public ExpressionObject(Page p){
		super(p);
		getAttributeWithName("expression").setValue("");
		getAttributeWithName("fontSize").setValue(12);
	}

	@Override
	public void addDefaultAttributes() {
		addAttribute(new StringAttribute("expression"));
		addAttribute(new IntegerAttribute("fontSize", 1, 50));
	}
	
	public String getExpression(){
		return ((StringAttribute)getAttributeWithName("expression")).getValue();
	}
	
	public void setText(String s) throws AttributeException{
		setAttributeValue("expression", s);
	}
	
	public void setFontSize(int fontSize) throws AttributeException {
		setAttributeValue("fontSize", fontSize);
	}

	public int getFontSize() {
		return ((IntegerAttribute)getAttributeWithName("fontSize")).getValue();
	}

	@Override
	public String getType() {
		return EXPRESSION_OBJECT;
	}
}
