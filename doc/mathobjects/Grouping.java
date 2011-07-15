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

public class Grouping extends MathObject{
	
	public Grouping(Page p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	//should some restriction be put in place to prevent Groupings to be stored inside one another?
	//how would nested groupings be conveyed to the user
	private Vector<MathObject> childObjects;

	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
