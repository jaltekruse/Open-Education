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

public abstract class PolygonObject extends MathObject {
	
	private int numPoints;
	
	protected static final String FLIP_HORIZONTALLY = "flip horizontally";
	
	protected static final String FLIP_VERTICALLY = "flip vertically";
	
	protected static final String ROTATE_CLOCKWISE_90 = "rotate clockwise (90)";
	
	private Vector<GridPoint> points;
	
	public PolygonObject(Page p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h);
		numPoints = 0;
		points = new Vector<GridPoint>();
		addAttribute(new IntegerAttribute("thickness", 1, 20));
		getAttributeWithName("thickness").setValue(t);
	}
	
	public PolygonObject(Page p){
		super(p);
		numPoints = 0;
		points = new Vector<GridPoint>();
		addAttribute(new IntegerAttribute("thickness", 1, 20));
		getAttributeWithName("thickness").setValue(1);
	}
	
	public abstract void addInitialPoints();
	
	public void performAction(String s){
		if (s.equals(FLIP_HORIZONTALLY)){
			flipHorizontally();
		}
		else if (s.equals(FLIP_VERTICALLY)){
			flipVertically();
		}
	}
	
	protected void flipHorizontally(){
		GridPoint p;
		
		for (MathObjectAttribute mAtt : getAttributes()){
			if (mAtt instanceof GridPointAttribute){
//			for (GridPoint p : points){
				p = ((GridPointAttribute)mAtt).getValue();
				if (p.getx() < .5){
					p.setx(p.getx() + 2 * (.5 - p.getx()));
				}
				else if (p.getx() > .5){
					p.setx(p.getx() + 2 * (.5 - p.getx()));
				}
				else{
					//x is .5, should not be shifted
				}
			}
		}
	}
	
	protected void flipVertically(){
		GridPoint p;
//		
		for (MathObjectAttribute mAtt : getAttributes()){
			if (mAtt instanceof GridPointAttribute){
//		for (GridPoint p : points){
				p = ((GridPointAttribute)mAtt).getValue();
				if (p.gety() < .5){
					p.sety(p.gety() + 2 * (.5 - p.gety()));
				}
				else if (p.gety() > .5){
					p.sety(p.gety() + 2 * (.5 - p.gety()));
				}
				else{
					//x is .5, should not be shifted
				}
			}
		}
	}
	
	public Vector<GridPoint> getVertices(){
		Vector<GridPoint> pts = new Vector<GridPoint>();
		for (MathObjectAttribute mAtt : getAttributes()){
			if (mAtt instanceof GridPointAttribute){
				pts.add(((GridPointAttribute)mAtt).getValue());
			}
		}
		return pts;
//		return points;
	}
	
	public void addVertex(GridPoint p){
//		points.add(p);
		int index = countVertices() + 1;
		String name = "point" + index;
		addAttribute(new GridPointAttribute(name, 0.0, 1.0, 0.0, 1.0));
		getAttributeWithName(name).setValue(p);
	}
	
	public void addVertexBeforePos(GridPoint p, int pos){
		//have to adjust the list positions of the subsequent points
	}

	public int countVertices(){
		int n = 0;
		for (MathObjectAttribute mAtt : getAttributes()){
			if (mAtt instanceof GridPointAttribute){
				n++;
			}
		}
		return n;
//		return points.size();
	}
	
	public void setThickness(int t) {
		getAttributeWithName("thickness").setValue(t);
	}

	public int getThickness() {
		return ((IntegerAttribute)getAttributeWithName("thickness")).getValue();
	}

}
