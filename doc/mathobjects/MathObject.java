/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.util.Vector;

import doc.Page;

public abstract class MathObject {
	
	private Page parentPage;
	
	private Vector<MathObjectAttribute> attributes;
	
	public static final String ANSWER_BOX = "AnswerBox";
	public static final String EXPRESSION_OBJECT = "ExpressionObject";
	public static final String GRAPH_OBJECT = "GraphObject";
	public static final String HEXAGON_OBJECT = "HexagonObject";
	public static final String NUMBER_LINE_OBJECT = "NumberLineObject";
	public static final String OVAL_OBJECT = "OvalObject";
	public static final String RECTANGLE_OBJECT = "RectangleObject";
	public static final String TEXT_OBJECT = "TextObject";
	public static final String TRIANGLE_OBJECT = "TriangleObject";
	public static final String TRAPEZOID_OBJECT = "TrapezoidObject";
	public static final String PARALLELOGRAM_OBJECT = "ParallelogramObject";
	public static final String CUBE_OBJECT = "CubeObject";
	
	//holds a list of function names to manipulate objects, such as
	//flip horizontally/vertically, actual code to change the object is
	//written in the performFunction method
	private Vector<String> actions;
	
	private boolean studentSelectable;
	
	public MathObject(Page p){
		setParentPage(p);
		attributes = new Vector<MathObjectAttribute>();
		actions = new Vector<String>();
		addDefaultAttributes();
		addGenericDefaultAttributes();
		studentSelectable = false;
	}
	
	public MathObject(Page p, int x, int y, int w, int h){
		setParentPage(p);
		attributes = new Vector<MathObjectAttribute>();
		actions = new Vector<String>();
		addDefaultAttributes();
		addGenericDefaultAttributes();
		getAttributeWithName("xPos").setValue(x);
		getAttributeWithName("yPos").setValue(y);
		getAttributeWithName("width").setValue(w);
		getAttributeWithName("height").setValue(h);
		studentSelectable = false;
	}
	
	public void addGenericDefaultAttributes(){
		addAttribute(new IntegerAttribute("xPos", 1, 610));
		addAttribute(new IntegerAttribute("yPos", 1, 790));
		addAttribute(new IntegerAttribute("width", 1, 610));
		addAttribute(new IntegerAttribute("height", 1, 790));
	}
	
	public abstract void addDefaultAttributes();
	
	public abstract String getType();
	
	public String exportToXML(){
		String output = "";
		output += "<" + getType() + ">\n";
		for (MathObjectAttribute mAtt : attributes){
			output += "<" + mAtt.getType() + " name=\"" + mAtt.getName()
			+ "\" value=\"" + mAtt.getValue().toString() + "\"></" + mAtt.getType() + ">\n";
		}
		output += "</" + getType() + ">\n";
		return output;
	}
	
	public void setParentPage(Page parentPage) {
		this.parentPage = parentPage;
	}

	public Page getParentPage() {
		return parentPage;
	}

	public Vector<String> getActions(){
		return actions;
	}
	
	public void addAction(String s){
		if ( ! actions.contains(s)){
			actions.add(s);
		}
	}
	
	public void performAction(String s) {
		if ( ! actions.contains(s)){
			System.out.println("unrecognized action (MathObject)");
		}
	}
	
	
	public void setWidth(int width) {
		getAttributeWithName("width").setValue(width);
	}

	public int getWidth() {
		return ((IntegerAttribute)getAttributeWithName("width")).getValue();
	}

	public void setHeight(int height) {
		getAttributeWithName("height").setValue(height);
	}

	public int getHeight() {
		return ((IntegerAttribute)getAttributeWithName("height")).getValue();
	}

	public void setxPos(int xPos) {
		getAttributeWithName("xPos").setValue(xPos);
	}

	public int getxPos() {
		return ((IntegerAttribute)getAttributeWithName("xPos")).getValue();
	}

	public void setyPos(int yPos) {
		getAttributeWithName("yPos").setValue(yPos);
	}

	public int getyPos() {
		return ((IntegerAttribute)getAttributeWithName("yPos")).getValue();
	}

	public void setAttributes(Vector<MathObjectAttribute> attributes) {
		this.attributes = attributes;
	}

	public Vector<MathObjectAttribute> getAttributes() {
		return attributes;
	}
	
	public MathObjectAttribute getAttributeWithName(String n){
		for (MathObjectAttribute mAtt : attributes){
			if (mAtt.getName().equals(n)){
				return mAtt;
			}
		}
		return null;
	}
	
	public void setAttributeValue(String n, Object o) throws AttributeException{
		getAttributeWithName(n).setValue(o);
	}
	
	public boolean addAttribute(MathObjectAttribute a){
		if (getAttributeWithName(a.getName()) == null){
			attributes.add(a);
			return true;
		}
		return false;
	}

	public void setStudentSelectable(boolean studentSelectable) {
		this.studentSelectable = studentSelectable;
	}

	public boolean isStudentSelectable() {
		return studentSelectable;
	}
}
