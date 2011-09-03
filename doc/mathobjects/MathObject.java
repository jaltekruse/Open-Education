/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import doc.Document;
import doc.Page;
import doc_gui.attributes.IntegerAttribute;
import doc_gui.attributes.ListAttribute;
import doc_gui.attributes.MathObjectAttribute;

public abstract class MathObject {
	
	private Page parentPage;
	
	private Vector<MathObjectAttribute> attributes;
	
	private Vector<ListAttribute> lists;
	
	public static final String ANSWER_BOX = "AnswerBox";
	public static final String EXPRESSION_OBJECT = "Expression";
	public static final String GRAPH_OBJECT = "Graph";
	public static final String HEXAGON_OBJECT = "Hexagon";
	public static final String NUMBER_LINE_OBJECT = "NumberLine";
	public static final String OVAL_OBJECT = "Oval";
	public static final String RECTANGLE_OBJECT = "Rectangle";
	public static final String TEXT_OBJECT = "Text";
	public static final String TRIANGLE_OBJECT = "Triangle";
	public static final String TRAPEZOID_OBJECT = "Trapezoid";
	public static final String PARALLELOGRAM_OBJECT = "Parallelogram";
	public static final String CUBE_OBJECT = "Cube";
	public static final String GROUPING = "Grouping";
	public static final String PROBLEM_OBJECT = "Problem";
	public static final String CYLINDER_OBJECT = "Cylinder";
	public static final String CONE_OBJECT = "Cone";
	public static final String REGULAR_POLYGON_OBJECT = "RegularPolygon";
	public static final String ARROW_OBJECT = "Arrow";
	public static final String PYRAMID_OBJECT = "Pyramid";
	
	
	public static final String MAKE_INTO_PROBLEM = "Make into Problem";
	public static final String MAKE_SQUARE = "Make Square";
	
	//holds a list of function names to manipulate objects, such as
	//flip horizontally/vertically, actual code to change the object is
	//written in the performFunction method
	private Vector<String> actions;
	
	private Vector<String> studentActions;
	
	private boolean studentSelectable;
	
	public MathObject(){
		attributes = new Vector<MathObjectAttribute>();
		actions = new Vector<String>();
		lists = new Vector<ListAttribute>();
		studentActions = new Vector<String>();
		addAction(MAKE_SQUARE);
		addDefaultAttributes();
		addGenericDefaultAttributes();
		studentSelectable = false;
	}
	
	public MathObject(Page p){
		setParentPage(p);
		attributes = new Vector<MathObjectAttribute>();
		actions = new Vector<String>();
		lists = new Vector<ListAttribute>();
		studentActions = new Vector<String>();
		addAction(MAKE_SQUARE);
		addDefaultAttributes();
		addGenericDefaultAttributes();
		studentSelectable = false;
	}
	
	public MathObject(Page p, int x, int y, int w, int h){
		setParentPage(p);
		attributes = new Vector<MathObjectAttribute>();
		actions = new Vector<String>();
		lists = new Vector<ListAttribute>();
		studentActions = new Vector<String>();
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
			output += mAtt.export();
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
	
	public boolean addAction(String s){
		if ( ! actions.contains(s)){
			actions.add(s);
			return true;
		}
		return false;
	}
	
	public MathObject clone(){
		MathObject o = null;
		if (this instanceof TextObject){
			o = new TextObject(getParentPage());
		}
		else if (this instanceof OvalObject){
			o = new OvalObject(getParentPage());
		}
		else if (this instanceof GraphObject){
			o = new GraphObject(getParentPage());
		}
		else if (this instanceof CubeObject){
			o = new CubeObject(getParentPage());
		}
		else if (this instanceof TriangleObject){
			o = new TriangleObject(getParentPage());
		}
		else if (this instanceof HexagonObject){
			o = new HexagonObject(getParentPage());
		}
		else if (this instanceof ParallelogramObject){
			o = new ParallelogramObject(getParentPage());
		}
		else if (this instanceof TrapezoidObject){
			o = new TrapezoidObject(getParentPage());
		}
		else if (this instanceof RectangleObject){
			o = new RectangleObject(getParentPage());
		}
		else if(this instanceof ExpressionObject){
			o = new ExpressionObject(getParentPage());
		}
		else if (this instanceof NumberLineObject){
			o = new NumberLineObject(getParentPage());
		}
		else if (this instanceof AnswerBoxObject){
			o = new AnswerBoxObject(getParentPage());
		}
		else if (this instanceof CylinderObject){
			o = new CylinderObject(getParentPage());
		}
		else if (this instanceof RegularPolygonObject){
			o = new RegularPolygonObject(getParentPage());
		}
		else if (this instanceof ArrowObject){
			o = new ArrowObject(getParentPage());
		}
		else if (this instanceof Grouping){
			if (this instanceof ProblemObject){
				o = new ProblemObject(getParentPage());
			}
			else{
				o = new Grouping(this.getParentPage());
			}
			for (MathObject mObj : ((Grouping)this).getObjects()){
				((Grouping)o).addObjectFromPage(mObj.clone());
			}
		}
		else{
			System.out.println("unrecognized object type attempted to be cloned: " + o);
		}
		
		o.removeAllAttributes();
		for (MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute(mAtt.clone());
		}
		return o;
	}
	
	public void removeAllAttributes(){
		attributes = new Vector<MathObjectAttribute>();
	}
	
	public boolean removeAction(String s){
		return actions.remove(s);
	}
	
	public void addStudentAction(String s){
		if ( ! studentActions.contains(s)){
			studentActions.add(s);
		}
	}
	
	public void performAction(String s){
		if (s.equals(MAKE_INTO_PROBLEM) && ! ( this instanceof Grouping ) )
		{//grouping has its own code segment for this, it will be called below with performSpecialAction()
			ProblemObject newProblem = new ProblemObject(getParentPage(), getxPos(),getyPos(),
					getWidth(), getHeight());
			
			this.getParentPage().getParentDoc().docPanel.setFocusedObject(newProblem);
			Vector<MathObject> newObjects = new Vector<MathObject>();
			newObjects.add(this);
			newProblem.addObjectFromPage(this);
			getParentPage().addObject(newProblem);
			getParentPage().removeObject(this);
		}
		else if (s.equals(MAKE_SQUARE)){
			int area = this.getWidth() * this.getHeight();
			int sideLength = (int) Math.sqrt(area);
			this.setWidth(sideLength);
			this.setHeight(sideLength);
		}
		//add other actions that all MathObjects have the ability to do, like make square
		else{
			// this call will send the request down to the object specific actions
			performSpecialObjectAction(s);
		}
	}
	
	public void performSpecialObjectAction(String s) {

		if ( ! actions.contains(s)){
			System.out.println("unrecognized action (MathObject)");
		}
	}
	
	public Rectangle getBounds(){
		return new Rectangle(getxPos(), getyPos(), getWidth(), getHeight());
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
	
	public Vector<ListAttribute> getLists(){
		return lists;
	}
	
	public ListAttribute getListWithName(String n){
		for (ListAttribute list : lists){
			if (list.getName().equals(n)){
				return list;
			}
		}
		return null;
	}
	
	public boolean addList(ListAttribute l){
		if (getListWithName(l.getName()) == null)
		{//the name is not in use
			lists.add(l);
			return true;
		}
		return false;
	}
	
	public MathObjectAttribute getAttributeWithName(String n){
		for (MathObjectAttribute mAtt : attributes){
			if (mAtt.getName().equals(n)){
				return mAtt;
			}
		}
		return null;
	}
	
	public void setAttributeValueWithString(String s, String val) throws AttributeException{
		setAttributeValue(s, getAttributeWithName(s).readValueFromString(val));
	}
	
	public void setAttributeValue(String n, Object o) throws AttributeException{
		getAttributeWithName(n).setValue(o);
	}
	
	public boolean addAttribute(MathObjectAttribute a){
		if (getAttributeWithName(a.getName()) == null){
			attributes.add(a);
			a.setParentObject(this);
			return true;
		}
		return false;
	}
	
	public void removeAttribute(MathObjectAttribute a){
		attributes.remove(a);
	}

	public void setStudentSelectable(boolean studentSelectable) {
		this.studentSelectable = studentSelectable;
	}

	public boolean isStudentSelectable() {
		return studentSelectable;
	}

	public void setStudentActions(Vector<String> studentActions) {
		this.studentActions = studentActions;
	}

	public Vector<String> getStudentActions() {
		return studentActions;
	}
}
