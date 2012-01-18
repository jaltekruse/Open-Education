/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import doc.GridPoint;
import doc.Page;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class TrapezoidObject extends PolygonObject {

	public TrapezoidObject(MathObjectContainer p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h, t);
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
	}
	
	public TrapezoidObject(MathObjectContainer p){
		super(p);
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
	}

	public TrapezoidObject() {
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
	}

	@Override
	public void addInitialPoints() {
		addVertex(new GridPoint(.25, 0));
		addVertex(new GridPoint(.75, 0));
		addVertex(new GridPoint(1, 1));
		addVertex(new GridPoint(0, 1));
	}
	
	@Override
	public void addDefaultAttributes() {
		
	}

	@Override
	public TrapezoidObject clone() {
		TrapezoidObject o = new TrapezoidObject(getParentContainer());
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
		return TRAPEZOID_OBJ;
	}

}
