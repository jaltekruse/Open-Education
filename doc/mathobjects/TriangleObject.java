/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.util.Vector;

import doc.GridPoint;
import doc.Page;
import doc_gui.attributes.MathObjectAttribute;

public class TriangleObject extends PolygonObject {
	
	public static final String MAKE_RIGHT_TRIANGLE = "make right triangle";
	
	public static final String MAKE_ISOSCELES_TRIANGLE = "make isosceles triangle";
		
	public TriangleObject(Page p){
		super(p);
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
		addAction(MAKE_RIGHT_TRIANGLE);
		addAction(MAKE_ISOSCELES_TRIANGLE);
	}
	
	public TriangleObject(Page p, int x, int y, int w, int h, int thickness) {
		super(p, x, y, w, h, thickness);
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
		addAction(MAKE_RIGHT_TRIANGLE);
		addAction(MAKE_ISOSCELES_TRIANGLE);
	}

	public TriangleObject() {
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
		addAction(MAKE_RIGHT_TRIANGLE);
		addAction(MAKE_ISOSCELES_TRIANGLE);
	}
	
	@Override
	public void addInitialPoints() {
		// TODO Auto-generated method stub
		addVertex(new GridPoint(.5, 0));
		addVertex(new GridPoint(0, 1));
		addVertex(new GridPoint(1, 1));
	}
	
	@Override
	public void addDefaultAttributes() {
		
	}
	
	@Override
	public void performSpecialObjectAction(String s){
		if (s.equals(FLIP_HORIZONTALLY)){
			flipHorizontally();
		}
		else if (s.equals(FLIP_VERTICALLY)){
			flipVertically();
		}
		else if(s.equals(MAKE_RIGHT_TRIANGLE)){
			makeRightTriangle();
		}
		else if(s.equals(MAKE_ISOSCELES_TRIANGLE)){
			makeIsoscelesTriangle();
		}
	}
	
	private void makeRightTriangle(){
		Vector<GridPoint> verticies = getVertices();
		verticies.get(0).setx(0);
		verticies.get(0).sety(0);
		verticies.get(1).setx(0);
		verticies.get(1).sety(1);
		verticies.get(2).setx(1);
		verticies.get(2).sety(1);
	}
	
	private void makeIsoscelesTriangle(){
		Vector<GridPoint> verticies = getVertices();
		verticies.get(0).setx(.5);
		verticies.get(0).sety(0);
		verticies.get(1).setx(1);
		verticies.get(1).sety(1);
		verticies.get(2).setx(0);
		verticies.get(2).sety(1);
	}
	
	@Override
	public TriangleObject clone() {
		TriangleObject o = new TriangleObject(getParentPage());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		return o;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return TRIANGLE_OBJECT;
	}
	
	
}
