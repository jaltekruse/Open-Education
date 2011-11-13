/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import doc.Page;
import doc_gui.attributes.BooleanAttribute;
import doc_gui.attributes.DoubleAttribute;
import doc_gui.attributes.IntegerAttribute;
import doc_gui.attributes.MathObjectAttribute;
import doc_gui.attributes.StringAttribute;

public class GraphObject extends MathObject {
	
	public static final String DEFAULT_GRID = "default grid";
	public static final String ZOOM_IN = "zoom in";
	public static final String ZOOM_OUT = "zoom out";
	
	public GraphObject(Page p, int x, int y, int width, int height) {
		super(p, x, y, width, height);
		setDefaults();
		getAttributeWithName("y=").setValue("");
		addAction(DEFAULT_GRID);
		addAction(ZOOM_IN);
		addAction(ZOOM_OUT);
//		addStudentAction("Graph point");
	}
	
	public GraphObject(Page p){
		super(p);
		setDefaults();
		getAttributeWithName("y=").setValue("");
		addAction(DEFAULT_GRID);
		addAction(ZOOM_IN);
		addAction(ZOOM_OUT);
//		addStudentAction("Graph point");
	}
	
	public GraphObject() {
		setDefaults();
		getAttributeWithName("y=").setValue("");
		addAction(DEFAULT_GRID);
		addAction(ZOOM_IN);
		addAction(ZOOM_OUT);
//		addStudentAction("Graph point");
	}

	@Override
	public void performSpecialObjectAction(String s){
		if (s.equals(DEFAULT_GRID)){
			setDefaults();
		}
		else if (s.equals(ZOOM_IN)){
			zoom(110);
		}
		else if (s.equals(ZOOM_OUT)){
			zoom(90);
		}
	}
	
	public void zoom(double rate){
		double xMin = getxMin(), yMin = getyMin(), xMax = getxMax(), yMax = getyMax();
		
		//hacked solution to prevent drawing the grid, the auto-rescaling of the 
		//grid stops working after the numbers get too big
		if (xMin < -7E8 || xMax > 7E8 || yMin < -7E8 || yMax > 7E8){
			if (rate < 100)
			{//if the user is trying to zoom out farther, do nothing
				return;
			}
		}
		
		try {
			setxMin( xMin + (-1 * (xMax-xMin)*(100-rate)/100) );
			setxMax( xMax + ( (xMax-xMin)*(100-rate)/100) );
			setyMin( yMin + (-1 * (yMax-yMin)*(100-rate)/100) );
			setyMax( yMax + ((yMax-yMin)*(100-rate)/100) );
		} catch (AttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public double getxMin(){
		return ((DoubleAttribute) getAttributeWithName("xMin")).getValue();
	}
	
	public double getyMin(){
		return ((DoubleAttribute) getAttributeWithName("yMin")).getValue();
	}
	
	public double getxMax(){
		return ((DoubleAttribute) getAttributeWithName("xMax")).getValue();
	}
	
	public double getyMax(){
		return ((DoubleAttribute) getAttributeWithName("yMax")).getValue();
	}
	
	public void setxMin(double d) throws AttributeException{
		setAttributeValue("xMin", d);
	}
	
	public void setyMin(double d) throws AttributeException{
		setAttributeValue("yMin", d);
	}
	
	public void setxMax(double d) throws AttributeException{
		setAttributeValue("xMax", d);
	}
	
	public void setyMax(double d) throws AttributeException{
		setAttributeValue("yMax", d);
	}


	public void setDefaults(){
		try{
			addAttribute(new StringAttribute("y="));
			addAttribute(new DoubleAttribute("xMin", -100000, 100000));
			getAttributeWithName("xMin").setValueWithString("-5");
			addAttribute(new DoubleAttribute("xMax", -100000, 100000));
			getAttributeWithName("xMax").setValueWithString("5");
			addAttribute(new DoubleAttribute("yMin", -100000, 100000));
			getAttributeWithName("yMin").setValueWithString("-5");
			addAttribute(new DoubleAttribute("yMax", -100000, 100000));
			getAttributeWithName("yMax").setValueWithString("5");
			addAttribute(new DoubleAttribute("xStep", -50000, 50000));
			getAttributeWithName("xStep").setValueWithString("1");
			addAttribute(new DoubleAttribute("yStep", -50000, 50000));
			getAttributeWithName("yStep").setValueWithString("1");
			addAttribute(new IntegerAttribute("fontSize", 1, 20));
			getAttributeWithName("fontSize").setValueWithString("8");
			addAttribute(new BooleanAttribute("showAxis"));
			getAttributeWithName("showAxis").setValue(true);
			addAttribute(new BooleanAttribute("showNumbers"));
			getAttributeWithName("showNumbers").setValue(true);
			addAttribute(new BooleanAttribute("showGrid"));
			getAttributeWithName("showGrid").setValue(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getExpression(){
		return ((StringAttribute)getAttributeWithName("y=")).getValue();
	}
	@Override
	public void setAttributeValue(String n, Object o) throws AttributeException{
		if (n.equals("xMin")){
			if (o instanceof Double){
				if (((Double)o) < 
						((DoubleAttribute)getAttributeWithName("xMax")).getValue()){
					getAttributeWithName("xMin").setValue(o);
				}
				else{
					throw new AttributeException("xMin must be less than xMax");
				}
			}
		}
		getAttributeWithName(n).setValue(o);
	}

	@Override
	public void addDefaultAttributes() {
		
	}
	
	@Override
	public GraphObject clone() {
		GraphObject o = new GraphObject(getParentPage());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		return o;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return GRAPH_OBJECT;
	}
}
