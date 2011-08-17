package doc.mathobjects;

import java.awt.Color;
import java.util.Vector;

import doc.GridPoint;
import doc.Page;
import doc_gui.attributes.ColorAttribute;
import doc_gui.attributes.IntegerAttribute;

public class CubeObject extends MathObject{
	
	private Vector<GridPoint> outsidePoints;
	private GridPoint innerPoint;
	private GridPoint side1Pt, cornerPt, side2Pt;
	
	public CubeObject(Page p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h);
		outsidePoints = new Vector<GridPoint>();
		addInitialPoints();
		innerPoint = new GridPoint(.75, .25);
		getAttributeWithName("thickness").setValue(t);
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	public CubeObject(Page p){
		super(p);
		outsidePoints = new Vector<GridPoint>();
		addInitialPoints();
		innerPoint = new GridPoint(.75, .25);
		getAttributeWithName("thickness").setValue(1);
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}

	public CubeObject() {
		outsidePoints = new Vector<GridPoint>();
		addInitialPoints();
		innerPoint = new GridPoint(.75, .25);
		getAttributeWithName("thickness").setValue(1);
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}

	public void addInitialPoints() {
		
		side1Pt = new GridPoint(0, .25);
		outsidePoints.add(side1Pt);
		
		outsidePoints.add(new GridPoint(0, 1));
		side2Pt = new GridPoint(.75, 1);
		outsidePoints.add(side2Pt);
		
		outsidePoints.add(new GridPoint(1, .75));
		cornerPt = new GridPoint(1, 0);
		outsidePoints.add(cornerPt);
		
		outsidePoints.add(new GridPoint(.25, 0));
	}
	
	protected void flipHorizontally(){
		
		for (GridPoint p : outsidePoints){
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
		GridPoint p = innerPoint;
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
	
	public void performSpecialObjectAction(String s){
		if (s.equals(PolygonObject.FLIP_HORIZONTALLY)){
			flipHorizontally();
		}
		else if (s.equals(PolygonObject.FLIP_VERTICALLY)){
			flipVertically();
		}
	}
	
	protected void flipVertically(){	
		for (GridPoint p : outsidePoints){
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
		GridPoint p = innerPoint;
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
	@Override
	public void addDefaultAttributes() {
		addAttribute(new IntegerAttribute("thickness"));
		addAttribute(new ColorAttribute("fill color", null));
	}
	
	public int getThickness(){
		return ((IntegerAttribute)getAttributeWithName("thickness")).getValue();
	}
	
	public Color getColor(){
		return ((ColorAttribute)getAttributeWithName("fill color")).getValue();
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return CUBE_OBJECT;
	}

	public GridPoint getInnerPoint() {
		return innerPoint;
	}

	public void setOutsidePoints(Vector<GridPoint> outsidePoints) {
		this.outsidePoints = outsidePoints;
	}

	public Vector<GridPoint> getOutsidePoints() {
		return outsidePoints;
	}

	public void setSide1Pt(GridPoint side1Pt) {
		this.side1Pt = side1Pt;
	}

	public GridPoint getSide1Pt() {
		return side1Pt;
	}

	public void setCornerPt(GridPoint cornerPt) {
		this.cornerPt = cornerPt;
	}

	public GridPoint getCornerPt() {
		return cornerPt;
	}

	public void setSide2Pt(GridPoint side2Pt) {
		this.side2Pt = side2Pt;
	}

	public GridPoint getSide2Pt() {
		return side2Pt;
	}
}
