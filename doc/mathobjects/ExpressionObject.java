/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JOptionPane;

import doc.Page;
import doc.attributes.AttributeException;
import doc.attributes.ColorAttribute;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;
import doc.attributes.StringAttribute;
import expression.Expression;
import expression.Node;
import expression.NodeException;
import expression.Operator;
import expression.Operator.Multiplication;

public class ExpressionObject extends MathObject {

	public static String	COMBINE_LIKE_TERMS = "Combine like terms",
							SIMPLIFY = "Simplify",
							ADD_TO_BOTH_SIDES = "Add to both sides",
							SUBTRACT_FROM_BOTH_SIDES = "Subtract from both sides",
							MULTIPLY_BOTH_SIDES = "Multiply both sides",
							DIVIDE_BOTH_SIDES = "Divide both sides",
							SUB_IN_VALUE = "Substitute in value",
							MODIFY_EXPRESSION = "Modify Expression",
							MANUALLY_TYPE_STEP = "Manually type step",
							OTHER_OPERATIONS = "Other operations",
							UNDO_STEP = "Undo Step";
	
	public static String		EXPRESSION = "expression",		FONT_SIZE = "fontSize",
			STEPS = "steps",	FILL_COLOR = "fill color";
			
	
	public ExpressionObject(MathObjectContainer p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		setStudentSelectable(true);
		setVerticallyResizable(false);
		setHorizontallyResizable(false);
		setExpressionActions();
		getAttributeWithName(EXPRESSION).setValue("");
		getAttributeWithName(STEPS).setValue("");
		getAttributeWithName(FONT_SIZE).setValue(12);
		getAttributeWithName(FILL_COLOR).setValue(null);
	}

	public ExpressionObject(MathObjectContainer p){
		super(p);
		setStudentSelectable(true);
		setVerticallyResizable(false);
		setHorizontallyResizable(false);
		setExpressionActions();
		getAttributeWithName(EXPRESSION).setValue("");
		getAttributeWithName(STEPS).setValue("");
		getAttributeWithName(FONT_SIZE).setValue(12);
		getAttributeWithName(FILL_COLOR).setValue(null);
	}

	public ExpressionObject() {
		setStudentSelectable(true);
		setVerticallyResizable(false);
		setHorizontallyResizable(false);
		setExpressionActions();
		getAttributeWithName(EXPRESSION).setValue("");
		getAttributeWithName(STEPS).setValue("");
		getAttributeWithName(FONT_SIZE).setValue(12);
		getAttributeWithName(FILL_COLOR).setValue(null);
	}
	
	private void setExpressionActions(){
		removeAction(MAKE_SQUARE);
		addAction(MathObject.MAKE_INTO_PROBLEM);
//		addStudentAction(COMBINE_LIKE_TERMS);
//		addStudentAction(SIMPLIFY);
		addStudentAction(ADD_TO_BOTH_SIDES);
		addStudentAction(SUBTRACT_FROM_BOTH_SIDES);
		addStudentAction(MULTIPLY_BOTH_SIDES);
		addStudentAction(DIVIDE_BOTH_SIDES);
		addStudentAction(SUB_IN_VALUE);
		addStudentAction(OTHER_OPERATIONS);
		addStudentAction(MODIFY_EXPRESSION);
		addStudentAction(MANUALLY_TYPE_STEP);
		addStudentAction(UNDO_STEP);
	}

	@Override
	protected void addDefaultAttributes() {
		addAttribute(new StringAttribute(EXPRESSION));
		StringAttribute steps = new StringAttribute(STEPS, false);
		addList(new ListAttribute<StringAttribute>(STEPS, new StringAttribute("val"), false));
		addAttribute(steps);
		addAttribute(new IntegerAttribute(FONT_SIZE, 1, 50));
		addAttribute(new ColorAttribute(FILL_COLOR));
	}
	
	public void setAttributeValue(String n, Object o) throws AttributeException{
		if (n.equals(EXPRESSION)){
			if ( ! o.equals(getAttributeWithName(EXPRESSION).getValue()))
			{// if the value of the expression is different
				// need to prevent loss of steps, as the text box for setting the
				// expression is selected when an expression object gains focus
				// and it tries to set the value when the input field loses focus
				// thus selecting and de-selecting an expression would always result in
				// a loss of the steps
				getAttributeWithName(STEPS).setValue("");
				getListWithName(STEPS).removeAll();
			}
		}
		getAttributeWithName(n).setValue(o);
	}

	public void performSpecialObjectAction(String s){
		if (s.equals(MAKE_INTO_PROBLEM)){
			ProblemObject newProblem = new ProblemObject(getParentContainer(), getxPos(),
					getyPos(), getWidth(), getHeight() );
			this.getParentContainer().getParentDoc().getDocViewerPanel().setFocusedObject(newProblem);
			newProblem.addObjectFromPage(this);
			getParentContainer().addObject(newProblem);
			getParentContainer().removeObject(this);
			return;
		}
		else if ( ((StringAttribute)getAttributeWithName(EXPRESSION)).getValue() == null || 
				((StringAttribute)getAttributeWithName(EXPRESSION)).getValue().equals("") ){
			JOptionPane.showMessageDialog(null,
					"There is no expression to work with, enter one in the box below.",
					"Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		else if (s.equals(UNDO_STEP)){
			if ( ! ((StringAttribute)getAttributeWithName(STEPS)).getValue().equals("")){
				String stepsString = getAttributeWithName(STEPS).getValue().toString();
				String[] steps;
				steps = stepsString.split(";");
				String newSteps = "";
				for (int i = 0; i < steps.length - 1; i++){
					newSteps += steps[i] + ";";
				}
				getAttributeWithName(STEPS).setValue(newSteps);
			}
			else{
				JOptionPane.showMessageDialog(null,
						"No steps to undo.", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
			return;
		}
		else if (s.equals(SUB_IN_VALUE)){
			String variableStr = "";
			Node substitute = null;
			do{
				variableStr = (String)JOptionPane.showInputDialog(
						null, "Enter a variable to replace.", null,
						JOptionPane.PLAIN_MESSAGE, null, null, null);
				if ( variableStr == null){
					return;
				}
				if ( variableStr.length() != 1 || ! Character.isLetter(variableStr.charAt(0))){
					JOptionPane.showMessageDialog(null, "Need to enter a single character.",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
			} while(variableStr.length() != 1 || ! Character.isLetter(variableStr.charAt(0)));
			
			boolean validEx = false;
			String lastEx = "";
			while( ! validEx ){
				lastEx = (String)JOptionPane.showInputDialog(null,
						"Enter a value or expression to substitute in.",
						null, JOptionPane.PLAIN_MESSAGE, null, null, lastEx);
				if ( lastEx == null){
					return;
				}
				try{
					substitute = Node.parseNode(lastEx);
					validEx = true;
				} catch (NodeException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Error with expression.",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
				
			}
			substitute.setDisplayParentheses(true);
			try {
				Node newNode = Node.parseNode(getLastStep()).replace(variableStr, substitute);
				getAttributeWithName(STEPS).setValue(
						getAttributeWithName(STEPS).getValue().toString()
						+ newNode.toStringRepresentation() + ";");
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Error with expression.",
						"Warning", JOptionPane.WARNING_MESSAGE);
			}
			return;
		}
		else if (s.equals(MODIFY_EXPRESSION)){
			boolean validEx = false;
			String lastEx = getLastStep();
			while( ! validEx ){
				lastEx = (String)JOptionPane.showInputDialog( null,
						"Modify the expression.", null,
						JOptionPane.PLAIN_MESSAGE, null, null, lastEx);
				if (lastEx == null || lastEx.equals("")){
					return;
				}
				Node newNode = null;
				try {
					newNode = Node.parseNode(lastEx);
					getAttributeWithName(STEPS).setValue(
							getAttributeWithName(STEPS).getValue().toString()
							+ newNode.toStringRepresentation() + ";");
					return;
				} catch (NodeException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Error with expression.",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		else if (s.equals(MANUALLY_TYPE_STEP)){
			boolean validEx = false;
			String lastEx = "";
			while( ! validEx ){
				lastEx = (String)JOptionPane.showInputDialog( null,
						"Type the entire next line.", null,
						JOptionPane.PLAIN_MESSAGE, null, null, lastEx);
				if (lastEx == null || lastEx.equals("")){
					return;
				}
				Node newNode = null;
				try {
					newNode = Node.parseNode(lastEx);
					getAttributeWithName(STEPS).setValue(
							getAttributeWithName(STEPS).getValue().toString()
							+ newNode.toStringRepresentation() + ";");
					return;
				} catch (NodeException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,
							"Error with expression.",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		// disabled for now
//		else if (s.equals(COMBINE_LIKE_TERMS)){
//			Node n = null;
//			try {
//				if (((StringAttribute)getAttributeWithName(STEPS)).getValue().equals("")){
//					n = Node.parseNode( ((StringAttribute)getAttributeWithName(EXPRESSION)).getValue());
//				}
//				else{
//					String stepsString = getAttributeWithName(STEPS).getValue().toString();
//					Vector<String> steps = new Vector<String>();
//					int lastEnd = 0;
//					for (int i = 0; i < stepsString.length(); i++){
//						if (stepsString.charAt(i) == ';'){
//							steps.add(stepsString.substring(lastEnd, i));
//							lastEnd = i + 1;
//						}
//						else if ( i == stepsString.length() - 1){
//							steps.add(stepsString.substring(lastEnd, i + 1));
//						}
//					}
//					n = Node.parseNode(steps.get(steps.size() - 1));
//				}
//				n = n.collectLikeTerms();
//				n = n.numericSimplify();
//				getAttributeWithName(STEPS).setValue(
//						getAttributeWithName(STEPS).getValue().toString()
//						+ n.toStringRepresentation() + ";");
//			} catch (NodeException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return;
//		}

		// disabled for now
//		else if (s.equals(SIMPLIFY)){
//			Node n = null;
//			try {
//				if ( ((StringAttribute)getAttributeWithName(STEPS)).getValue().equals("")){
//					n = Node.parseNode( ((StringAttribute)getAttributeWithName(EXPRESSION)).getValue());
//				}
//				else{
//					String stepsString = getAttributeWithName(STEPS).getValue().toString();
//					Vector<String> steps = new Vector<String>();
//					int lastEnd = 0;
//					for (int i = 0; i < stepsString.length(); i++){
//						if (stepsString.charAt(i) == ';'){
//							steps.add(stepsString.substring(lastEnd, i));
//							lastEnd = i + 1;
//						}
//						else if ( i == stepsString.length() - 1){
//							steps.add(stepsString.substring(lastEnd, i + 1));
//						}
//					}
//					n = Node.parseNode(steps.get(steps.size() - 1));
//				}
//				n = n.numericSimplify();
//
//				getAttributeWithName(STEPS).setValue(
//						getAttributeWithName(STEPS).getValue().toString()
//						 + n.toStringRepresentation()+ ";");
//			}catch (NodeException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			return;
//		}
		
	
		// all of the rest of the operations require an equals sign
		Node n = null;
		try{
			String expression = ((StringAttribute)getAttributeWithName(EXPRESSION)).getValue();
			if ( ! expression.equals("")){
				if ( ((StringAttribute)getAttributeWithName(STEPS)).getValue().equals("")){
					n = Node.parseNode( ((StringAttribute)getAttributeWithName(EXPRESSION)).getValue());
				}
				else{
					String stepsString = getAttributeWithName(STEPS).getValue().toString();
					Vector<String> steps = new Vector<String>();
					int lastEnd = 0;
					for (int i = 0; i < stepsString.length(); i++){
						if (stepsString.charAt(i) == ';'){
							steps.add(stepsString.substring(lastEnd, i));
							lastEnd = i + 1;
						}
						else if ( i == stepsString.length() - 1){
							steps.add(stepsString.substring(lastEnd, i + 1));
						}
					}
					n = Node.parseNode(steps.get(steps.size() - 1));
				}
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,
					"Previous expression has an error.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if ( ! (n instanceof Expression && ((Expression)n).getOperator() instanceof Operator.Equals) ){
			//the expression does not have an equals sign
			JOptionPane.showMessageDialog(null,
					"Expression requires an equal sign for that operation",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		Expression ex = (Expression) n;
		if (s.equals(OTHER_OPERATIONS)){
			Object[] operations = {"sqrt", "sin", "cos", "tan"};
			String op = (String)JOptionPane.showInputDialog(
			                    null,
			                    "Choose an operation to apply to both sides",
			                    "Operation Selection",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    operations,
			                    "sqrt");
			if (op == null || op.equals("")){
				return;
			}
			Operator o = null;
			if (op.equals("sqrt")){
				o = new Operator.SquareRoot();
			}
			else if (op.equals("sin")){
				o = new Operator.Sine();
			}
			else if (op.equals("cos")){
				o = new Operator.Cosine();
			}
			else if (op.equals("tan")){
				o = new Operator.Tangent();
			}
			
			Expression newLeft = new Expression(o);
			Vector<Node> left = new Vector<Node>();
			Node newChild = ex.getChild(0);
			if ( ! op.equals("sqrt") ){
				newChild.setDisplayParentheses(true);
			}
			left.add(newChild);
			newLeft.setChildren(left);
			
			Expression newRight = new Expression(o);
			Vector<Node> right = new Vector<Node>();
			 newChild = ex.getChild(1);
				if ( ! op.equals("sqrt") ){
					newChild.setDisplayParentheses(true);
				}
			right.add(newChild);
			newRight.setChildren(right);
			
			Vector<Node> exChildren = new Vector<Node>();
			exChildren.add(newLeft);
			exChildren.add(newRight);
			ex.setChildren(exChildren);
			
			try {
				getAttributeWithName(STEPS).setValue(
						getAttributeWithName(STEPS).getValue().toString()
						 + ex.toStringRepresentation()+ ";");
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try{
			if (s.equals(ADD_TO_BOTH_SIDES)){
				applyOpToBothSides(ex, new Operator.Addition(), "Add to both sides");
			}
			else if (s.equals(SUBTRACT_FROM_BOTH_SIDES)){
				applyOpToBothSides(ex, new Operator.Subtraction(), "Subtract from both sides");
			}
			else if (s.equals(DIVIDE_BOTH_SIDES)){
				multiplyOrDivideBothSides(ex, new Operator.Division(), "Divide both sides by");
			}
			else if (s.equals(MULTIPLY_BOTH_SIDES)){
				multiplyOrDivideBothSides(ex, new Multiplication(), "Multiply both sides by");
			}
		}catch (Exception e){
			JOptionPane.showMessageDialog(null,
					"Error with operation.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public String getLastStep(){
		String stepsString = getAttributeWithName(STEPS).getValue().toString();
		String[] steps;
		if ( stepsString.equals("")){
			steps = new String[1];
			return getExpression();
		}
		else{
			steps = stepsString.split(";");
			return steps[steps.length - 1];
		}
	}
	
	public void multiplyOrDivideBothSides(Expression ex, Operator newOp, String message) throws NodeException{
		boolean validEx = false;
		String lastEx = "";
		Node newNode = null;
		while( ! validEx ){
			lastEx = (String)JOptionPane.showInputDialog(null,
					"Enter a value or expression to substitute in.",
					null, JOptionPane.PLAIN_MESSAGE, null, null, lastEx);
			if ( lastEx == null){
				break;
			}
			try{
				newNode = Node.parseNode(lastEx);
				validEx = true;
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Error with expression.",
						"Warning", JOptionPane.WARNING_MESSAGE);
			}
			
		}
		Node newLeft = new Expression(new Operator.Addition());
		Vector<Node> leftChildren = new Vector<Node>();
		for ( Node n : ex.getChild(0).splitOnAddition()){
			Expression newTerm = new Expression(newOp);
			Vector<Node> termChildren = new Vector<Node>();
			termChildren.add(n);
			termChildren.add(newNode);
			newTerm.setChildren(termChildren);
			leftChildren.add(newTerm);
		}
		newLeft = Expression.staggerAddition(leftChildren);
		
		Node newRight = new Expression(new Operator.Addition());
		Vector<Node> rightChildren = new Vector<Node>();
		for ( Node n : ex.getChild(1).splitOnAddition()){
			Expression newTerm = new Expression(newOp);
			Vector<Node> termChildren = new Vector<Node>();
			termChildren.add(n);
			termChildren.add(newNode);
			newTerm.setChildren(termChildren);
			rightChildren.add(newTerm);
		}
		newRight = Expression.staggerAddition(rightChildren);
		
		Vector<Node> exChildren = new Vector<Node>();
		exChildren.add(newLeft);
		exChildren.add(newRight);
		ex.setChildren(exChildren);

		getAttributeWithName(STEPS).setValue(
				getAttributeWithName(STEPS).getValue().toString()
				 + ex.toStringRepresentation()+ ";");
		
	}

	public void applyOpToBothSides(Expression ex, Operator newOp, String message) throws NodeException{
		boolean validEx = false;
		String lastEx = "";
		Node newNode = null;
		while( ! validEx ){
			lastEx = (String)JOptionPane.showInputDialog(null,
					"Enter a value or expression to substitute in.",
					null, JOptionPane.PLAIN_MESSAGE, null, null, lastEx);
			if ( lastEx == null){
				break;
			}
			try{
				newNode = Node.parseNode(lastEx);
				validEx = true;
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Error with expression.",
						"Warning", JOptionPane.WARNING_MESSAGE);
			}
			
		}
		try {
			Expression newLeft = new Expression(newOp);
			Vector<Node> leftchildren = new Vector<Node>();
	
			//grab the left hand size of the previous expression
			leftchildren.add(ex.getChild(0).cloneNode());
			leftchildren.add(newNode);
			newLeft.setChildren(leftchildren);
	
			Expression newRight = new Expression(newOp);
			Vector<Node> rightchildren = new Vector<Node>();
	
			//grab the right hand side of the previous expression
			rightchildren.add(ex.getChild(1).cloneNode());
			rightchildren.add(newNode);
			newRight.setChildren(rightchildren);
	
			Vector<Node> exChildren = new Vector<Node>();
			exChildren.add(newLeft);
			exChildren.add(newRight);
			ex.setChildren(exChildren);
		
		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		getAttributeWithName(STEPS).setValue(
				getAttributeWithName(STEPS).getValue().toString()
				 + ex.toStringRepresentation()+ ";");

	}
	
	@Override
	public ExpressionObject clone() {
		ExpressionObject o = new ExpressionObject(getParentContainer());
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
	
	public Color getColor(){
		return ((ColorAttribute)getAttributeWithName(FILL_COLOR)).getValue();
	}

	public String getExpression(){
		return ((StringAttribute)getAttributeWithName(EXPRESSION)).getValue();
	}

	public void setExpression(String s) throws AttributeException{
		setAttributeValue(EXPRESSION, s);
	}

	public void setFontSize(int fontSize) throws AttributeException {
		setAttributeValue(FONT_SIZE, fontSize);
	}

	public int getFontSize() {
		return ((IntegerAttribute)getAttributeWithName(FONT_SIZE)).getValue();
	}

	@Override
	public String getType() {
		return EXPRESSION_OBJ;
	}
}
