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
import doc_gui.attributes.StringAttribute;

public class GraphObject extends MathObject {
	
	private static final String DEFAULT_GRID = "default grid";
	
	public GraphObject(Page p, int x, int y, int width, int height) {
		super(p, x, y, width, height);
		setDefaults();
		getAttributeWithName("y=").setValue("");
		addAction(DEFAULT_GRID);
		addStudentAction("Graph point");
	}
	
	public GraphObject(Page p){
		super(p);
		setDefaults();
		getAttributeWithName("y=").setValue("");
		addAction(DEFAULT_GRID);
		addStudentAction("Graph point");
	}
	
	public GraphObject() {
		setDefaults();
		getAttributeWithName("y=").setValue("");
		addAction(DEFAULT_GRID);
		addStudentAction("Graph point");
	}

	public void performSpecialObjectAction(String s){
		if (s.equals(DEFAULT_GRID)){
			setDefaults();
		}
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
	public String getType() {
		// TODO Auto-generated method stub
		return GRAPH_OBJECT;
	}
}
