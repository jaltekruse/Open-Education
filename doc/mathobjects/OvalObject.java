/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import doc.Page;

public class OvalObject extends MathObject {
	
	public OvalObject(Page p, int x, int y, int width, int height, int thickness) {
		super(p,x, y, width, height);
		getAttributeWithName("thickness").setValue(thickness);
	}
	
	public OvalObject(Page p) {
		super(p);
		getAttributeWithName("thickness").setValue(1);
		// TODO Auto-generated constructor stub
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
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return OVAL_OBJECT;
	}

}
