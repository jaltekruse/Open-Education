package doc.mathobjects;

import java.awt.Color;

import doc.GridPoint;
import doc.attributes.AttributeException;
import doc.attributes.BooleanAttribute;
import doc.attributes.ColorAttribute;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class CylinderObject extends MathObject {

	private GridPoint[] side1pts = { new GridPoint(0, .125), new GridPoint(0, .875)};
	private GridPoint[] side2pts = { new GridPoint(1, .125), new GridPoint(1, .875)};
	
	private GridPoint insideEdgeOfDisk = new GridPoint(.5, .25);
	
	private double heightHalfDisk = .25;
	
	private GridPoint pointBehindCylinder = new GridPoint(.5, .75);
	
	public CylinderObject(MathObjectContainer p){
		super(p);
		addAction(PolygonObject.FLIP_VERTICALLY);
	}
	
	public CylinderObject(){
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
		// TODO Auto-generated method stub
		return CYLINDER_OBJ;
	}
	
	@Override
	public void performSpecialObjectAction(String s){
		if (s.equals(PolygonObject.FLIP_VERTICALLY)){
			try {
				setAttributeValue(PolygonObject.FLIP_VERTICALLY, ! isFlippedVertically());
			} catch (AttributeException e) {
				// TODO Auto-generated catch block
//				System.out.println("error in CylinderGUI.performSpecialObjectAction");
			}
		}
	}
	
	@Override
	public CylinderObject clone() {
		CylinderObject o = new CylinderObject(getParentContainer());
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

	public void setSide1pts(GridPoint[] side1pts) {
		this.side1pts = side1pts;
	}

	public GridPoint[] getSide1pts() {
		return side1pts;
	}

	public void setSide2pts(GridPoint[] side2pts) {
		this.side2pts = side2pts;
	}

	public GridPoint[] getSide2pts() {
		return side2pts;
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
		this.pointBehindCylinder = pointBehindCylinder;
	}

	public GridPoint getPointBehindCylinder() {
		return pointBehindCylinder;
	}

	public void setHeightHalfDisk(double heightHalfDisk) {
		this.heightHalfDisk = heightHalfDisk;
	}

	public double getHalfDiskHeight() {
		return heightHalfDisk;
	}

	
}
