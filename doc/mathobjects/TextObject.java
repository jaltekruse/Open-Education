/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.util.Vector;

import doc.Page;
import doc_gui.attributes.BooleanAttribute;
import doc_gui.attributes.IntegerAttribute;
import doc_gui.attributes.StringAttribute;

public class TextObject extends MathObject {
	
	public TextObject(Page p) {
		super(p);
		getAttributeWithName("fontSize").setValue(12);
		getAttributeWithName("text").setValue("");
		getAttributeWithName("showBox").setValue(true);
	}
	
	public TextObject(Page p, int x, int y, int width, int height, int fontSize, String s){
		super(p, x, y, width, height);
		getAttributeWithName("fontSize").setValue(fontSize);
		getAttributeWithName("text").setValue(s);
		getAttributeWithName("showBox").setValue(true);
	}

	public TextObject() {
		getAttributeWithName("fontSize").setValue(12);
		getAttributeWithName("text").setValue("");
		getAttributeWithName("showBox").setValue(true);
	}

	@Override
	public void addDefaultAttributes() {
		addAttribute(new StringAttribute("text"));
		addAttribute(new IntegerAttribute("fontSize", 1, 50));
		addAttribute(new BooleanAttribute("showBox"));
	}

	public void setFontSize(int fontSize) throws AttributeException {
		setAttributeValue("fontSize", fontSize);
	}

	public int getFontSize() {
		return ((IntegerAttribute)getAttributeWithName("fontSize")).getValue();
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return TEXT_OBJECT;
	}
	
	public String getText(){
		return ((StringAttribute)getAttributeWithName("text")).getValue();
	}
	
	public void setText(String s) throws AttributeException{
		setAttributeValue("text", s);
	}

}
