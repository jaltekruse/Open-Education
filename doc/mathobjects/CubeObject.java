package doc.mathobjects;

import java.awt.Color;
import java.util.Vector;

import doc.GridPoint;
import doc.Page;
import doc_gui.attributes.BooleanAttribute;
import doc_gui.attributes.ColorAttribute;
import doc_gui.attributes.IntegerAttribute;

public class CubeObject extends MathObject{
	
	private static GridPoint side1Pt = new GridPoint(0, .25),
			cornerPt = new GridPoint(1,0), side2Pt = new GridPoint(.75, 1);
	private static GridPoint[] outsidePoints = { side1Pt, new GridPoint(0,1),
		side2Pt, new GridPoint(1, .75), cornerPt, new GridPoint(.25, 0)};
	
	private static GridPoint innerPoint = new GridPoint(.75, .25);

	
	public CubeObject(Page p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h);
		getAttributeWithName("thickness").setValue(t);
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	public CubeObject(Page p){
		super(p);
		getAttributeWithName("thickness").setValue(1);
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}

	public CubeObject() {
		getAttributeWithName("thickness").setValue(1);
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	public void performSpecialObjectAction(String s){
		if (s.equals(PolygonObject.FLIP_HORIZONTALLY)){
			((BooleanAttribute)getAttributeWithName("flip horizontally")).setValue( ! isFlippedHorizontally());
		}
		else if (s.equals(PolygonObject.FLIP_VERTICALLY)){
			((BooleanAttribute)getAttributeWithName("flip vertically")).setValue( ! isFlippedVertically());
		}
	}
	
	@Override
	public void addDefaultAttributes() {
		addAttribute(new IntegerAttribute("thickness"));
		addAttribute(new ColorAttribute("fill color", null));
		BooleanAttribute flippedVertically = new BooleanAttribute("flip vertically", false);
		flippedVertically.setUserEditable(false);
		addAttribute(flippedVertically);
		BooleanAttribute flippedHorizontally = new BooleanAttribute("flip horizontally", false);
		flippedHorizontally.setUserEditable(false);
		addAttribute(flippedHorizontally);
	}
	
	public int getThickness(){
		return ((IntegerAttribute)getAttributeWithName("thickness")).getValue();
	}
	
	public Color getColor(){
		return ((ColorAttribute)getAttributeWithName("fill color")).getValue();
	}
	
	public boolean isFlippedVertically(){
		return ((BooleanAttribute)getAttributeWithName("flip vertically")).getValue();
	}
	
	public boolean isFlippedHorizontally(){
		return ((BooleanAttribute)getAttributeWithName("flip horizontally")).getValue();
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return CUBE_OBJECT;
	}

	public static GridPoint getInnerPoint() {
		return innerPoint;
	}


	public static GridPoint[] getOutsidePoints() {
		return outsidePoints;
	}

	public static GridPoint getSide1Pt() {
		return side1Pt;
	}

	public static GridPoint getCornerPt() {
		return cornerPt;
	}

	public static GridPoint getSide2Pt() {
		return side2Pt;
	}
}
