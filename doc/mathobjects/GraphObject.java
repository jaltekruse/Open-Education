/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.util.Vector;

import doc.attributes.AttributeException;
import doc.attributes.BooleanAttribute;
import doc.attributes.DoubleAttribute;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;
import doc.attributes.StringAttribute;

public class GraphObject extends MathObject {
	
	public static final String X_MIN = "xMin", Y_MIN = "yMin",
			X_MAX = "xMax", Y_MAX = "yMax", EXPRESSION = "y=",
			X_STEP = "xStep", Y_STEP = "yStep", FONT_SIZE = "font size",
			SHOW_AXIS = "show axis", SHOW_NUMBERS = "show numbers",
			SHOW_GRID = "show grid", EXPRESSIONS = "Expressions";
	
	public static final String DEFAULT_GRID = "default grid",
			ZOOM_IN = "zoom in", ZOOM_OUT = "zoom out", MOVE_LEFT = "move left",
			MOVE_RIGHT = "move right", MOVE_DOWN = "move down", MOVE_UP = "move up";
	
	public GraphObject(MathObjectContainer p, int x, int y, int width, int height) {
		super(p, x, y, width, height);
		setDefaults();
		setStudentSelectable(true);
		addGraphActions();
	}
	
	public GraphObject(MathObjectContainer p){
		super(p);
		setDefaults();
		setStudentSelectable(true);
		addGraphActions();
	}
	
	public GraphObject() {
		setDefaults();
		setStudentSelectable(true);
		addGraphActions();
	}
	
	private void addGraphActions(){
		addAction(MAKE_INTO_PROBLEM);
		addStudentAction(DEFAULT_GRID);
		addStudentAction(ZOOM_IN);
		addStudentAction(ZOOM_OUT);
		addStudentAction(MOVE_UP);
		addStudentAction(MOVE_DOWN);
		addStudentAction(MOVE_LEFT);
		addStudentAction(MOVE_RIGHT);
//		addStudentAction("Graph point");
	}
	
	public void setDefaults(){
		try{
			addList(new ListAttribute<StringAttribute>(EXPRESSIONS,
					new StringAttribute(EXPRESSION), 3, true, false));
			addAttribute(new DoubleAttribute(X_MIN, -7E8, 7E8, true, true));
			getAttributeWithName(X_MIN).setValueWithString("-5");
			addAttribute(new DoubleAttribute(X_MAX, -7E8, 7E8, true, true));
			getAttributeWithName(X_MAX).setValueWithString("5");
			addAttribute(new DoubleAttribute("yMin", -7E8, 7E8, true, true));
			getAttributeWithName("yMin").setValueWithString("-5");
			addAttribute(new DoubleAttribute("yMax", -7E8, 7E8, true, true));
			getAttributeWithName("yMax").setValueWithString("5");
			addAttribute(new DoubleAttribute("xStep", -3E8, 3E8, true, true));
			getAttributeWithName("xStep").setValueWithString("1");
			addAttribute(new DoubleAttribute("yStep", -3E8, 3E8, true, true));
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

	@Override
	public void performSpecialObjectAction(String s){
		if (s.equals(MAKE_INTO_PROBLEM)){
			VariableValueInsertionProblem newProblem = new VariableValueInsertionProblem(getParentContainer(), getxPos(),
					getyPos(), getWidth(), getHeight() );
			this.getParentContainer().getParentDoc().getDocViewerPanel().setFocusedObject(newProblem);
			newProblem.addObjectFromPage(this);
			getParentContainer().addObject(newProblem);
			getParentContainer().removeObject(this);
			return;
		}
		else if (s.equals(DEFAULT_GRID)){
			setDefaults();
		}
		else if (s.equals(ZOOM_IN)){
			zoom(110);
		}
		else if (s.equals(ZOOM_OUT)){
			zoom(90);
		}
		else if (s.equals(MOVE_UP)){
			shiftGraph(0, 10);
		}
		else if (s.equals(MOVE_DOWN)){
			shiftGraph(0, -10);
		}
		else if (s.equals(MOVE_LEFT)){
			shiftGraph(-10, 0);
		}
		else if (s.equals(MOVE_RIGHT)){
			shiftGraph(10, 0);
		}
	}
	
	private void zoom(double rate){
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
	
	private void shiftGraph(int xPix, int yPix){
		double xMin = getxMin(), yMin = getyMin(), xMax = getxMax(), yMax = getyMax();
		double xPixel = (xMax - xMin) / getWidth();
		double yPixel = (yMax - yMin) / getHeight();
		try{
			setxMin( xMin + xPix * xPixel );
			setyMax( yMax + yPix * yPixel );
			setyMin( yMin + yPix * yPixel );
			setxMax( xMax + xPix * xPixel );
		} catch (Exception ex){
			;
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
	
	public void addExpression(String s) throws AttributeException{
		getListWithName(EXPRESSIONS).addValueWithString(s);
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

	public Vector<StringAttribute> getExpressions(){
		Vector<StringAttribute> expressions = new Vector<StringAttribute>();
		for ( Object strAtt : getListWithName(EXPRESSIONS).getValues()){
			expressions.add((StringAttribute)strAtt);
		}
		return expressions;
	}
	
	@Override
	public boolean setAttributeValue(String n, Object o) throws AttributeException{
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
		return true;
	}

	@Override
	public void addDefaultAttributes() {
		
	}
	
	@Override
	public GraphObject clone() {
		GraphObject o = new GraphObject(getParentContainer());
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
	public String getType() {
		// TODO Auto-generated method stub
		return GRAPH_OBJ;
	}
}
