/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import doc.Page;
import doc_gui.attributes.DoubleAttribute;
import doc_gui.attributes.IntegerAttribute;


public class NumberLineObject extends MathObject {
	
	public NumberLineObject(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
			getAttributeWithName("min").setValue(-5.0);
			getAttributeWithName("max").setValue(5.0);
			getAttributeWithName("step").setValue(1.0);
			getAttributeWithName("fontSize").setValue(8);
	}
	
	public NumberLineObject(Page p){
		super(p);
		getAttributeWithName("min").setValue(-5.0);
		getAttributeWithName("max").setValue(5.0);
		getAttributeWithName("step").setValue(1.0);
		getAttributeWithName("fontSize").setValue(8);
	}

	public NumberLineObject() {
		getAttributeWithName("min").setValue(-5.0);
		getAttributeWithName("max").setValue(5.0);
		getAttributeWithName("step").setValue(1.0);
		getAttributeWithName("fontSize").setValue(8);
	}

	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub
		addAttribute(new DoubleAttribute("min", -10000, 10000));
		addAttribute(new DoubleAttribute("max", -10000, 10000));
		addAttribute(new DoubleAttribute("step", 0, 5000));
		addAttribute(new IntegerAttribute("fontSize", 1, 20));
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return NUMBER_LINE_OBJECT;
	}

}
