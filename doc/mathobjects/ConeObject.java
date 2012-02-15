package doc.mathobjects;

import java.awt.Color;

import doc.GridPoint;
import doc.attributes.AttributeException;
import doc.attributes.BooleanAttribute;
import doc.attributes.ColorAttribute;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class ConeObject extends MathObject {
	private GridPoint[] trianglePts = { new GridPoint(0, .865), new GridPoint(.5, 0), new GridPoint(1, .865)};
	
	private GridPoint insideEdgeOfDisk = new GridPoint(.5, .25);
	
	private double heightHalfDisk = .25;
	
	private GridPoint pointBehindCone = new GridPoint(.5, .75);
	
	public ConeObject(MathObjectContainer p){
		super(p);
		addAction(PolygonObject.FLIP_VERTICALLY);
	}
	
	public ConeObject(){
		addAction(PolygonObject.FLIP_VERTICALLY);
	}
	
	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub
		addAttribute(new IntegerAttribute(PolygonObject.LINE_THICKNESS, 1, 1, 20));
		addAttribute(new ColorAttribute(PolygonObject.FILL_COLOR, null));
		BooleanAttribute flipped = new BooleanAttribute(PolygonObject.FLIP_VERTICALLY, false);
		flipped.setUserEditable(false);
		addAttribute(flipped);
	}

	@Override
	public String getType() {
		return CONE_OBJECT;
	}
	
	@Override
	public void performSpecialObjectAction(String s){
		if (s.equals(PolygonObject.FLIP_VERTICALLY)){
			try {
				setAttributeValue(PolygonObject.FLIP_VERTICALLY, ! isFlippedVertically());
			} catch (AttributeException e) {
				// should not happen, just setting boolean
				System.out.println("error in CylinderGUI.performSpecialObjectAction");
			}
		}
	}
	
	@Override
	public ConeObject clone() {
		ConeObject o = new ConeObject(getParentContainer());
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
	
	public boolean isFlippedVertically(){
		return ((BooleanAttribute)getAttributeWithName(PolygonObject.FLIP_VERTICALLY)).getValue();
	}

	public GridPoint[] getTrianglePts() {
		return trianglePts;
	}
	
	public int getThickness(){
		return ((IntegerAttribute)getAttributeWithName(PolygonObject.LINE_THICKNESS)).getValue();
	}
	
	public Color getColor(){
		return ((ColorAttribute)getAttributeWithName(PolygonObject.FILL_COLOR)).getValue();
	}

	public void setInsideEdgeOfDisk(GridPoint insideEdgeOfDisk) {
		this.insideEdgeOfDisk = insideEdgeOfDisk;
	}

	public GridPoint getInsideEdgeOfDisk() {
		return insideEdgeOfDisk;
	}

	public void setPointBehindCylinder(GridPoint pointBehindCylinder) {
		this.pointBehindCone = pointBehindCylinder;
	}

	public GridPoint getPointBehindCone() {
		return pointBehindCone;
	}

	public void setHeightHalfDisk(double heightHalfDisk) {
		this.heightHalfDisk = heightHalfDisk;
	}

	public double getHalfDiskHeight() {
		return heightHalfDisk;
	}

}
