/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.Color;

import doc.Page;
import doc.attributes.ColorAttribute;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class OvalObject extends MathObject {
	
	public OvalObject(MathObjectContainer p, int x, int y, int width, int height, int thickness) {
		super(p,x, y, width, height);
		getAttributeWithName("thickness").setValue(thickness);
	}
	
	public OvalObject(MathObjectContainer p) {
		super(p);
		getAttributeWithName("thickness").setValue(1);
		// TODO Auto-generated constructor stub
	}

	public OvalObject() {
		// TODO Auto-generated constructor stub
		getAttributeWithName("thickness").setValue(1);
	}

	public void setThickness(int t) {
		getAttributeWithName("thickness").setValue(t);
	}

	public int getThickness() {
		return ((IntegerAttribute)getAttributeWithName("thickness")).getValue();
	}

	@Override
	public void addDefaultAttributes() {
		addAttribute(new IntegerAttribute("thickness", 1, 20));
		addAttribute(new ColorAttribute("fill color"));
	}
	
	public Color getColor(){
		return ((ColorAttribute)getAttributeWithName("fill color")).getValue();
	}
	
	@Override
	public OvalObject clone() {
		OvalObject o = new OvalObject(getParentContainer());
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

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return OVAL_OBJ;
	}

}
