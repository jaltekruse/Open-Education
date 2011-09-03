/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.Color;
import java.util.Vector;

import doc.GridPoint;
import doc.Page;
import doc_gui.attributes.ColorAttribute;
import doc_gui.attributes.GridPointAttribute;
import doc_gui.attributes.IntegerAttribute;
import doc_gui.attributes.MathObjectAttribute;

public abstract class PolygonObject extends MathObject {
	
	
	public static final String FLIP_HORIZONTALLY = "flip horizontally";
	
	public static final String FLIP_VERTICALLY = "flip vertically";
	
	public static final String ROTATE_CLOCKWISE_90 = "rotate clockwise (90)";
	
	public PolygonObject(Page p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h);
		addAttribute(new IntegerAttribute("thickness", 1, 20));
		addAttribute(new ColorAttribute("fill color"));
		getAttributeWithName("thickness").setValue(t);
	}
	
	public PolygonObject(Page p){
		super(p);
		addAttribute(new IntegerAttribute("thickness", 1, 20));
		addAttribute(new ColorAttribute("fill color"));
		getAttributeWithName("thickness").setValue(1);
	}
	
	public PolygonObject(){
		addAttribute(new IntegerAttribute("thickness", 1, 20));
		addAttribute(new ColorAttribute("fill color"));
		getAttributeWithName("thickness").setValue(1);
	}
	
	public abstract void addInitialPoints();
	
	public Color getColor(){
		return ((ColorAttribute)getAttributeWithName("fill color")).getValue();
	}
	
	public void performSpecialObjectAction(String s){
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
					//y is .5, should not be shifted
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
	
	public void removeAllVertices(){
		System.out.println("num verticies: " + getVertices().size());
		for (int i = 0; i < getAttributes().size(); i++){
			MathObjectAttribute mAtt = getAttributes().get(i);
			if (mAtt instanceof GridPointAttribute){
				System.out.println("remove vertex");
				removeAttribute(mAtt);
				i--;
			}
		}
	}
	
	public void addVertex(GridPoint p){
//		points.add(p);
		int index = countVertices() + 1;
		String name = "point" + index;
		GridPointAttribute point = new GridPointAttribute(name, 0.0, 1.0, 0.0, 1.0);
		point.setUserEditable(false);
		addAttribute(point);
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
