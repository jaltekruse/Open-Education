/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import doc.Page;
import doc.attributes.DoubleAttribute;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;


public class NumberLineObject extends MathObject {
	
	public NumberLineObject(MathObjectContainer p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		setVerticallyResizable(false);
		getAttributeWithName("min").setValue(-5.0);
		getAttributeWithName("max").setValue(5.0);
		getAttributeWithName("step").setValue(1.0);
		getAttributeWithName("fontSize").setValue(8);
	}
	
	public NumberLineObject(MathObjectContainer p){
		super(p);
		setVerticallyResizable(false);
		getAttributeWithName("min").setValue(-5.0);
		getAttributeWithName("max").setValue(5.0);
		getAttributeWithName("step").setValue(1.0);
		getAttributeWithName("fontSize").setValue(8);
	}

	public NumberLineObject() {
		setVerticallyResizable(false);
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
		return NUMBER_LINE;
	}
	
	@Override
	public NumberLineObject clone() {
		NumberLineObject o = new NumberLineObject(getParentContainer());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		o.removeAllLists();
		for ( ListAttribute list : getLists()){
			o.addList(list.clone());
		}
		return o;
	}

}
