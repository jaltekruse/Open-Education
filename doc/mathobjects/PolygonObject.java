/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.Color;

import doc.GridPoint;
import doc.attributes.BooleanAttribute;
import doc.attributes.ColorAttribute;
import doc.attributes.IntegerAttribute;

public abstract class PolygonObject extends MathObject {


	public static final String FLIP_HORIZONTALLY = "flip horizontally", 
			FLIP_VERTICALLY = "flip vertically", ROTATE_CLOCKWISE_90 = "rotate clockwise (90)";
	public static final String LINE_THICKNESS = "line thickness", FILL_COLOR = "fill color",
			HORIZONTALLY_FLIPPED = "horizontally flipped", VERTICALLY_FLIPPED = "vertically flipped";

	public PolygonObject(MathObjectContainer p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h);
		addPolygonAttributes();
		getAttributeWithName(LINE_THICKNESS).setValue(t);
	}

	public PolygonObject(MathObjectContainer p){
		super(p);
		addPolygonAttributes();
	}

	public PolygonObject(){
		addPolygonAttributes();
	}
	
	private void addPolygonAttributes(){
		addAttribute(new IntegerAttribute(LINE_THICKNESS, 1, 1, 20));
		addAttribute(new ColorAttribute(FILL_COLOR));
		addAttribute(new BooleanAttribute(HORIZONTALLY_FLIPPED, false, false));
		getAttributeWithName(HORIZONTALLY_FLIPPED).setValue(false);
		addAttribute(new BooleanAttribute(VERTICALLY_FLIPPED, false, false));
		getAttributeWithName(VERTICALLY_FLIPPED).setValue(false);
	}

	public Color getColor(){
		return ((ColorAttribute)getAttributeWithName(FILL_COLOR)).getValue();
	}

	@Override
	public void performSpecialObjectAction(String s){
		if (s.equals(FLIP_HORIZONTALLY)){
			getAttributeWithName(HORIZONTALLY_FLIPPED).setValue( ! (Boolean)
					getAttributeWithName(HORIZONTALLY_FLIPPED).getValue());
		}
		else if (s.equals(FLIP_VERTICALLY)){
			getAttributeWithName(VERTICALLY_FLIPPED).setValue( ! (Boolean)
					getAttributeWithName(VERTICALLY_FLIPPED).getValue());
		}
	}

	protected GridPoint[] flipHorizontally(GridPoint[] points){
		
		GridPoint[] flipped = new GridPoint[points.length];

		int i = 0;
		for (GridPoint p : points){
			flipped[i] = flipPointHorizontally(p);
			i++;
		}
		return flipped;
	}

	protected GridPoint[] flipVertically(GridPoint[] points){
		
		GridPoint[] flipped = new GridPoint[points.length];
		
		int i = 0;
		for (GridPoint p : points){
			flipped[i] = flipPointVertically(p);
			i++;
		}
		return flipped;
	}
	
	public GridPoint[] getAdjustedVertices(){
		GridPoint[] points = getVertices();
		if ( this.isFlippedHorizontally() ){
			points = flipHorizontally(points);
		}
		if (this.isFlippedVertically()){
			points = flipVertically(points);
		}
		return points;
	}

	protected abstract GridPoint[] getVertices();

	public int countVertices(){
		return getVertices().length;	
	}
	
	public boolean isFlippedVertically(){
		return (Boolean) getAttributeWithName(VERTICALLY_FLIPPED).getValue();
	}
	
	public boolean isFlippedHorizontally(){
		return (Boolean) getAttributeWithName(HORIZONTALLY_FLIPPED).getValue();
	}

	public void setThickness(int t) {
		getAttributeWithName(LINE_THICKNESS).setValue(t);
	}

	public int getThickness() {
		return ((IntegerAttribute)getAttributeWithName(LINE_THICKNESS)).getValue();
	}

}
