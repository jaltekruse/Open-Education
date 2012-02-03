package doc.mathobjects;

import java.awt.Color;

import doc.GridPoint;
import doc.attributes.BooleanAttribute;
import doc.attributes.ColorAttribute;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class CubeObject extends MathObject{
	
	private static GridPoint side1Pt = new GridPoint(0, .25),
			cornerPt = new GridPoint(1,0), side2Pt = new GridPoint(.75, 1);
	private static GridPoint[] outsidePoints = { side1Pt, new GridPoint(0,1),
		side2Pt, new GridPoint(1, .75), cornerPt, new GridPoint(.25, 0)};
	
	private static GridPoint innerPoint = new GridPoint(.75, .25);

	
	public CubeObject(MathObjectContainer p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h);
		getAttributeWithName(PolygonObject.LINE_THICKNESS).setValue(t);
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	public CubeObject(MathObjectContainer p){
		super(p);
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}

	public CubeObject() {
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	@Override
	public void performSpecialObjectAction(String s){
		if (s.equals(PolygonObject.FLIP_HORIZONTALLY)){
			((BooleanAttribute)getAttributeWithName(
					PolygonObject.FLIP_HORIZONTALLY)).setValue( ! isFlippedHorizontally());
		}
		else if (s.equals(PolygonObject.FLIP_VERTICALLY)){
			((BooleanAttribute)getAttributeWithName(
					PolygonObject.FLIP_VERTICALLY)).setValue( ! isFlippedVertically());
		}
	}
	
	@Override
	public void addDefaultAttributes() {
		addAttribute(new IntegerAttribute(PolygonObject.LINE_THICKNESS, 1, 1, 20));
		addAttribute(new ColorAttribute(PolygonObject.FILL_COLOR, null));
		BooleanAttribute flippedVertically = new BooleanAttribute(PolygonObject.FLIP_VERTICALLY, false);
		flippedVertically.setUserEditable(false);
		addAttribute(flippedVertically);
		BooleanAttribute flippedHorizontally = new BooleanAttribute(PolygonObject.FLIP_HORIZONTALLY, false);
		flippedHorizontally.setUserEditable(false);
		addAttribute(flippedHorizontally);
	}
	
	public int getThickness(){
		return ((IntegerAttribute)getAttributeWithName(PolygonObject.LINE_THICKNESS)).getValue();
	}
	
	public Color getColor(){
		return ((ColorAttribute)getAttributeWithName(PolygonObject.FILL_COLOR)).getValue();
	}
	
	public boolean isFlippedVertically(){
		return ((BooleanAttribute)getAttributeWithName(PolygonObject.FLIP_VERTICALLY)).getValue();
	}
	
	public boolean isFlippedHorizontally(){
		return ((BooleanAttribute)getAttributeWithName(PolygonObject.FLIP_HORIZONTALLY)).getValue();
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
	
	@Override
	public CubeObject clone() {
		CubeObject o = new CubeObject(getParentContainer());
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
