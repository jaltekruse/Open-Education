/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import doc.Page;
import doc_gui.attributes.BooleanAttribute;
import doc_gui.attributes.IntegerAttribute;
import doc_gui.attributes.MathObjectAttribute;
import doc_gui.attributes.StringAttribute;

public class TextObject extends MathObject {
	
	public static final String TEXT = "text";
	public static final String FONT_SIZE = "fontSize";
	public static final String SHOW_BOX = "showBox";
	
	public TextObject(Page p) {
		super(p);
		getAttributeWithName(FONT_SIZE).setValue(12);
		getAttributeWithName(TEXT).setValue("");
		getAttributeWithName(SHOW_BOX).setValue(true);
	}
	
	public TextObject(Page p, int x, int y, int width, int height, int fontSize, String s){
		super(p, x, y, width, height);
		getAttributeWithName(FONT_SIZE).setValue(fontSize);
		getAttributeWithName(TEXT).setValue(s);
		getAttributeWithName(SHOW_BOX).setValue(true);
	}

	public TextObject() {
		getAttributeWithName(FONT_SIZE).setValue(12);
		getAttributeWithName(TEXT).setValue("");
		getAttributeWithName(SHOW_BOX).setValue(true);
	}

	@Override
	public void addDefaultAttributes() {
		addAttribute(new StringAttribute(TEXT));
		addAttribute(new IntegerAttribute(FONT_SIZE, 1, 50));
		addAttribute(new BooleanAttribute(SHOW_BOX));
	}

	public void setFontSize(int fontSize) throws AttributeException {
		setAttributeValue(FONT_SIZE, fontSize);
	}

	public int getFontSize() {
		return ((IntegerAttribute)getAttributeWithName(FONT_SIZE)).getValue();
	}
	
	@Override
	public TextObject clone() {
		TextObject o = new TextObject(getParentPage());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		return o;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return TEXT_OBJECT;
	}
	
	public String getText(){
		return ((StringAttribute)getAttributeWithName(TEXT)).getValue();
	}
	
	public void setText(String s) throws AttributeException{
		setAttributeValue(TEXT, s);
	}

}
