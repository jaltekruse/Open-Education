package doc.mathobjects;

import doc.GridPoint;
import doc.Page;
import doc.attributes.AttributeException;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class RegularPolygonObject extends PolygonObject {
	
	public static final String NUM_SIDES = "number of sides";
	
	public RegularPolygonObject(MathObjectContainer p){
		super(p);
		getAttributeWithName(NUM_SIDES).setValue(6);
	}
	
	public RegularPolygonObject() {
		getAttributeWithName(NUM_SIDES).setValue(6);
	}
	
	public RegularPolygonObject(int n){
		getAttributeWithName(NUM_SIDES).setValue(n);
	}
	
	private GridPoint[] generateVertices(){
		int n = getNumSides();
		GridPoint[] vertices = new GridPoint[getNumSides()];
		double initialAngle = .5 * Math.PI + (Math.PI - ( (n-2) * Math.PI )/n) / 2;
		for (int i = 0; i < n; i++){
			vertices[i] = new GridPoint(.5 + .5 * Math.cos(initialAngle + 2.0*Math.PI*i/n),
					.5 + .5 * Math.sin(initialAngle + 2.0*Math.PI*i/n) );
		}
		return vertices;
	}
	
	public int getNumSides(){
		return ((IntegerAttribute)getAttributeWithName(NUM_SIDES)).getValue();
	}
	
	@Override
	public RegularPolygonObject clone() {
		RegularPolygonObject o = new RegularPolygonObject(getParentContainer());
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
	public void addDefaultAttributes() {
		addAttribute(new IntegerAttribute(NUM_SIDES, 3, 30));
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return REGULAR_POLYGON_OBJECT;
	}

	@Override
	public GridPoint[] getVertices() {
		// TODO Auto-generated method stub
		return generateVertices();
	}

}
