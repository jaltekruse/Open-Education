package doc.mathobjects;

import doc.GridPoint;
import doc.Page;
import doc_gui.attributes.DoubleAttribute;
import doc_gui.attributes.IntegerAttribute;

public class RegularPolygonObject extends PolygonObject {
	
	public RegularPolygonObject(Page p){
		super(p);
		getAttributeWithName("numSides").setValue(8);
		addInitialPoints();

	}
	
	public RegularPolygonObject() {
		getAttributeWithName("numSides").setValue(8);
		addInitialPoints();
	}
	
	public RegularPolygonObject(int n){
		getAttributeWithName("numSides").setValue(n);
		generateVertices(n);
	}
	
	@Override
	public void addInitialPoints() {
		// TODO Auto-generated method stub

	}
	
	private void generateVertices(int n){
		double initialAngle = .5 * Math.PI + (Math.PI - ( (n-2) * Math.PI )/n) / 2;
		for (int i = 0; i < n; i++){
			addVertex(new GridPoint(.5 + .5 * Math.cos(initialAngle + 2.0*Math.PI*i/n),
					.5 + .5 * Math.sin(initialAngle + 2.0*Math.PI*i/n) ) ); 
		}
	}
	
	public void setAttributeValue(String n, Object o) throws AttributeException{
		if (n.equals("numSides")){
			System.out.println("val of att after reading from sting:" + o);
			getAttributeWithName(n).setValue(o);
			removeAllVertices();
			System.out.println("att found " + getAttributeWithName(n).getValue());
			generateVertices(((IntegerAttribute)getAttributeWithName(n)).getValue());
		}
		else{
			getAttributeWithName(n).setValue(o);
		}
	}

	@Override
	public void addDefaultAttributes() {
		addAttribute(new IntegerAttribute("numSides", 2, 30));
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return REGULAR_POLYGON_OBJECT;
	}

}
