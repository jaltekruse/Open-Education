/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.Rectangle;
import java.util.Vector;

import doc.Page;
import doc.attributes.AttributeException;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public abstract class MathObject {

	protected MathObjectContainer parentContainer;

	private Vector<MathObjectAttribute<?>> attributes;

	private Vector<ListAttribute<?>> lists;

	private boolean actionCancelled = false;

	public static final String ANSWER_BOX_OBJ = "AnswerBox",
			EXPRESSION_OBJ = "Expression", GRAPH_OBJ = "Graph",
			NUMBER_LINE = "NumberLine", OVAL_OBJ = "Oval",
			RECTANGLE = "Rectangle", TEXT_OBJ = "Text",
			TRIANGLE_OBJ = "Triangle", TRAPEZOID_OBJ = "Trapezoid",
			PARALLELOGRAM_OBJ = "Parallelogram", CUBE_OBJECT = "Cube",
			GROUPING = "Grouping", VAR_INSERTION_PROBLEM = "VarInsertionProblem",
			CYLINDER_OBJ = "Cylinder", CONE_OBJECT = "Cone",
			REGULAR_POLYGON_OBJECT = "RegularPolygon", ARROW_OBJECT = "Arrow",
			PYRAMID_OBJECT = "Pyramid", GENERATED_PROBLEM = "GeneratedProblem";

	// the order of these three arrays is very important, they are parallel
	// arrays used to create new instances of objects when all you have is
	// the type string. They also are used to generate the toolbar for creating
	// new objects
	public static final String[] objectTypes = { RECTANGLE, OVAL_OBJ,
			TRIANGLE_OBJ, REGULAR_POLYGON_OBJECT, TRAPEZOID_OBJ,
			PARALLELOGRAM_OBJ, ARROW_OBJECT, CUBE_OBJECT, CYLINDER_OBJ,
			NUMBER_LINE, GRAPH_OBJ, TEXT_OBJ, EXPRESSION_OBJ, ANSWER_BOX_OBJ,
			CONE_OBJECT, PYRAMID_OBJECT, GROUPING, VAR_INSERTION_PROBLEM,
			GENERATED_PROBLEM };

	public static final String[] imgFilenames = { "rectangle.png", "oval.png",
			"triangle.png", "regularPolygon.png", "trapezoid.png",
			"parallelogram.png", "arrow.png", "cube.png", "cylinder.png",
			"numberLine.png", "graph.png", "text.png", "expression.png",
			"answerBox.png", "cone.png", "pyramid.png", null, null, null };

	public static final MathObject[] objects = { new RectangleObject(),
			new OvalObject(), new TriangleObject(), new RegularPolygonObject(),
			new TrapezoidObject(), new ParallelogramObject(),
			new ArrowObject(), new CubeObject(), new CylinderObject(),
			new NumberLineObject(), new GraphObject(), new TextObject(),
			new ExpressionObject(), new AnswerBoxObject(), null, null,
			new Grouping(), new VariableValueInsertionProblem(), new GeneratedProblem() };

	public static final String MAKE_SQUARE = "Make Height and Width equal",
			MAKE_INTO_PROBLEM = "Make into Problem",
			ADJUST_SIZE_AND_POSITION = "Adjust size and position";

	public static final String WIDTH = "width", HEIGHT = "height",
			X_POS = "xPos", Y_POS = "yPos";

	// holds a list of function names to manipulate objects, such as
	// flip horizontally/vertically, actual code to change the object is
	// written in the performFunction method
	private Vector<String> actions;
	private Vector<String> studentActions;
	private boolean studentSelectable, isHorizontallyResizable,
			isVerticallyResizable;
	// flag to indicate that the object has just been removed from the document,
	// it prevents actions from
	// being performed as the fields in the frame are being unfocused by
	// clicking delete
	private boolean justDeleted = false;

	public MathObject() {
		attributes = new Vector<MathObjectAttribute<?>>();
		actions = new Vector<String>();
		lists = new Vector<ListAttribute<?>>();
		studentActions = new Vector<String>();

		setHorizontallyResizable(true);
		setVerticallyResizable(true);

		addAction(MAKE_SQUARE);
		addGenericDefaultAttributes();
		addDefaultAttributes();
		getAttributeWithName(X_POS).setValue(1);
		getAttributeWithName(Y_POS).setValue(1);
		getAttributeWithName(WIDTH).setValue(1);
		getAttributeWithName(HEIGHT).setValue(1);
	}

	public MathObject(MathObjectContainer c) {
		this();
		setParentContainer(c);
	}

	public MathObject(MathObjectContainer c, int x, int y, int w, int h) {
		this(c);
		getAttributeWithName(X_POS).setValue(x);
		getAttributeWithName(Y_POS).setValue(y);
		getAttributeWithName(WIDTH).setValue(w);
		getAttributeWithName(HEIGHT).setValue(h);
	}

	public void addGenericDefaultAttributes() {
		addAttribute(new IntegerAttribute(X_POS, 1, 610, false, false));
		addAttribute(new IntegerAttribute(Y_POS, 1, 790, false, false));
		addAttribute(new IntegerAttribute(WIDTH, 1, 610, false, false));
		addAttribute(new IntegerAttribute(HEIGHT, 1, 790, false, false));
	}

	/**
	 * Method to add attributes in subclasses of {@code MathObject}. Is
	 * automatically called by MathObject constructor. Only works for classes
	 * directly inheriting from {@code MathObject} as additional attributes
	 * added in the classes between {@code MathObject} and the class at the
	 * bottom of the inheritance tree will not be added, as their versions of
	 * this method will be overridden by the ones below them in the inheritance
	 * tree.
	 */
	protected void addDefaultAttributes() {
	}

	public abstract String getType();

	public static MathObject getNewInstance(String type) {
		int i = 0;
		for (String s : objectTypes) {
			if (type.equals(s)) {
				return objects[i].clone();
			}
			i++;
		}
		return null;
	}

	public static String getObjectImageName(String type) {
		int i = 0;
		for (String s : objectTypes) {
			if (type.equals(s)) {
				return imgFilenames[i];
			}
			i++;
		}
		return null;
	}

	public static boolean isMathObjectType(String type) {
		for (String s : objectTypes) {
			if (type.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public String exportToXML() {
		String output = "";
		output += "<" + getType() + ">\n";
		for (MathObjectAttribute mAtt : attributes) {
			output += mAtt.exportToXML();
		}
		for (ListAttribute lAtt : lists) {
			output += lAtt.exportToXML();
		}
		output += "</" + getType() + ">\n";
		return output;
	}

	public void setParentContainer(MathObjectContainer c) {
		this.parentContainer = c;
	}

	public MathObjectContainer getParentContainer() {
		return parentContainer;
	}

	public Page getParentPage() {
		if (parentContainer == null) {
			return null;
		}
		if (parentContainer instanceof Page) {
			return (Page) parentContainer;
		} else {
			return parentContainer.getParentPage();
		}
	}

	public Vector<String> getActions() {
		return actions;
	}

	public boolean addAction(String s) {
		if (!actions.contains(s)) {
			actions.add(s);
			return true;
		}
		return false;
	}

	@Override
	public abstract MathObject clone();

	public void removeAllAttributes() {
		attributes = new Vector<MathObjectAttribute<?>>();
	}

	public void removeAllLists() {
		lists = new Vector<ListAttribute<?>>();
	}

	public boolean removeAction(String s) {
		return actions.remove(s);
	}

	public void addStudentAction(String s) {
		if (!studentActions.contains(s)) {
			studentActions.add(s);
		}
	}

	/**
	 * Checks to see if this object is within the margins of the page it belongs
	 * to.
	 * 
	 * @return
	 */
	public boolean isOnPage() {
		Rectangle objRect = getBounds();
		if (getParentContainer() == null) {
			// something should be done to handle this error
		}
		if (getParentContainer() instanceof Page) {
			Rectangle pageRect = new Rectangle(
					((Page) getParentContainer()).getxMargin(),
					((Page) getParentContainer()).getyMargin(),
					getParentContainer().getWidth() - 2
							* ((Page) getParentContainer()).getxMargin(),
					getParentContainer().getHeight() - 2
							* ((Page) getParentContainer()).getyMargin());
			if (pageRect.contains(objRect)) {
				return true;
			}
			return false;
		}
		else{
			return false;
		}
	}

	public void performAction(String s) {
		setActionCancelled(false);
		if (justDeleted) {
			return;
		}
		if (s.equals(MAKE_SQUARE)) {
			int area = this.getWidth() * this.getHeight();
			int sideLength = (int) Math.sqrt(area);
			this.setWidth(sideLength);
			this.setHeight(sideLength);
		} else {
			// this call will send the request down to the object specific
			// actions
			performSpecialObjectAction(s);
		}
	}

	public void performSpecialObjectAction(String s) {

		if (!actions.contains(s)) {
			System.out.println("unrecognized action (MathObject)");
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(getxPos(), getyPos(), getWidth(), getHeight());
	}

	public void setWidth(int width) {
		getAttributeWithName(WIDTH).setValue(width);
	}

	public int getWidth() {
		return ((IntegerAttribute) getAttributeWithName(WIDTH)).getValue();
	}

	public void setHeight(int height) {
		getAttributeWithName(HEIGHT).setValue(height);
	}

	public int getHeight() {
		return ((IntegerAttribute) getAttributeWithName(HEIGHT)).getValue();
	}

	public void setxPos(int xPos) {
		getAttributeWithName(X_POS).setValue(xPos);
	}

	public int getxPos() {
		return ((IntegerAttribute) getAttributeWithName(X_POS)).getValue();
	}

	public void setyPos(int yPos) {
		getAttributeWithName(Y_POS).setValue(yPos);
	}

	public int getyPos() {
		return ((IntegerAttribute) getAttributeWithName(Y_POS)).getValue();
	}

	public void setAttributes(Vector<MathObjectAttribute<?>> attributes) {
		this.attributes = attributes;
	}

	public Vector<MathObjectAttribute<?>> getAttributes() {
		return attributes;
	}

	public Vector<ListAttribute<?>> getLists() {
		return lists;
	}

	public ListAttribute<?> getListWithName(String n) {
		for (ListAttribute<?> list : lists) {
			if (list.getName().equals(n)) {
				return list;
			}
		}
		return null;
	}

	public boolean addList(ListAttribute l) {
		if (getListWithName(l.getName()) == null) {// the name is not in use
			lists.add(l);
			l.setParentObject(this);
			return true;
		}
		return false;
	}

	public MathObjectAttribute getAttributeWithName(String n) {
		for (MathObjectAttribute mAtt : attributes) {
			if (mAtt.getName().equals(n)) {
				return mAtt;
			}
		}
		return null;
	}

	public Object getAttributeValue(String n) {
		for (MathObjectAttribute mAtt : attributes) {
			if (mAtt.getName().equals(n)) {
				return mAtt.getValue();
			}
		}
		return null;
	}

	public void setAttributeValueWithString(String s, String val)
			throws AttributeException {
		if (getAttributeWithName(s) == null) {
			throw new AttributeException(
					"Object does not have an attribute with that name");
		}
		setAttributeValue(s, getAttributeWithName(s).readValueFromString(val));
	}

	public boolean setAttributeValue(String n, Object o)
			throws AttributeException {
		getAttributeWithName(n).setValue(o);
		return true;
	}

	public boolean addAttribute(MathObjectAttribute a) {
		if (getAttributeWithName(a.getName()) == null) {
			attributes.add(a);
			a.setParentObject(this);
			return true;
		}
		return false;
	}

	public void removeAttribute(MathObjectAttribute a) {
		attributes.remove(a);
	}

	public void removeList(String s) {
		for (int i = 0; i < lists.size(); i++) {
			if (lists.get(i).getName().equals(s)) {
				lists.remove(i);
				return;
			}
		}
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

	protected void setHorizontallyResizable(boolean isHorizontallyResizable) {
		this.isHorizontallyResizable = isHorizontallyResizable;
	}

	public boolean isHorizontallyResizable() {
		return isHorizontallyResizable;
	}

	protected void setVerticallyResizable(boolean isVerticallyResizable) {
		this.isVerticallyResizable = isVerticallyResizable;
	}

	public boolean isVerticallyResizable() {
		return isVerticallyResizable;
	}

	public void setJustDeleted(boolean b) {
		justDeleted = b;
	}

	public boolean actionWasCancelled() {
		return actionCancelled;
	}

	protected void setActionCancelled(boolean actionCancelled) {
		this.actionCancelled = actionCancelled;
	}
}