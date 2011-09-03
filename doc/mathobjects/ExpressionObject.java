/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.util.Vector;

import javax.swing.JOptionPane;

import doc.Page;
import doc_gui.attributes.IntegerAttribute;
import doc_gui.attributes.ListAttribute;
import doc_gui.attributes.StringAttribute;
import expression.Expression;
import expression.Node;
import expression.NodeException;
import expression.Operator;
import expression.Operator.Multiplication;

public class ExpressionObject extends MathObject {

	public static String COMBINE_LIKE_TERMS = "Combine like terms";
	public static String SIMPLIFY = "Simplify";
	public static String ADD_TO_BOTH_SIDES = "Add to both sides";
	public static String SUBTRACT_FROM_BOTH_SIDES = "Subtract from both sides";
	public static String MULTIPLY_BOTH_SIDES = "Multiply both sides";
	public static String DIVIDE_BOTH_SIDES = "Divide both sides";
	public static String UNDO_STEP = "Undo Step";
	public static String MANUALLY_TYPE_STEP = "Manually type step";
	public static String APPLY_OTHER_OPERATION = "Apply other operation";

	public ExpressionObject(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		setStudentSelectable(true);
		addList(new ListAttribute<String>("steps"));
		removeAction(MAKE_SQUARE);
		addAction(MathObject.MAKE_INTO_PROBLEM);
		addStudentAction(COMBINE_LIKE_TERMS);
		addStudentAction(SIMPLIFY);
		addStudentAction(ADD_TO_BOTH_SIDES);
		addStudentAction(SUBTRACT_FROM_BOTH_SIDES);
		addStudentAction(MULTIPLY_BOTH_SIDES);
		addStudentAction(DIVIDE_BOTH_SIDES);
		addStudentAction(APPLY_OTHER_OPERATION);
		addStudentAction(MANUALLY_TYPE_STEP);
		addStudentAction(UNDO_STEP);
		getAttributeWithName("expression").setValue("");
		getAttributeWithName("steps").setValue("");
		getAttributeWithName("fontSize").setValue(12);
	}

	public ExpressionObject(Page p){
		super(p);
		setStudentSelectable(true);
		addList(new ListAttribute<String>("steps"));
		removeAction(MAKE_SQUARE);
		addAction(MathObject.MAKE_INTO_PROBLEM);
		addStudentAction(COMBINE_LIKE_TERMS);
		addStudentAction(SIMPLIFY);
		addStudentAction(ADD_TO_BOTH_SIDES);
		addStudentAction(SUBTRACT_FROM_BOTH_SIDES);
		addStudentAction(MULTIPLY_BOTH_SIDES);
		addStudentAction(DIVIDE_BOTH_SIDES);
		addStudentAction(APPLY_OTHER_OPERATION);
		addStudentAction(MANUALLY_TYPE_STEP);
		addStudentAction(UNDO_STEP);
		getAttributeWithName("expression").setValue("");
		getAttributeWithName("steps").setValue("");
		getAttributeWithName("fontSize").setValue(12);
	}

	public ExpressionObject() {
		setStudentSelectable(true);
		addList(new ListAttribute<String>("steps"));
		removeAction(MAKE_SQUARE);
		addAction(MathObject.MAKE_INTO_PROBLEM);
		addStudentAction(COMBINE_LIKE_TERMS);
		addStudentAction(SIMPLIFY);
		addStudentAction(ADD_TO_BOTH_SIDES);
		addStudentAction(SUBTRACT_FROM_BOTH_SIDES);
		addStudentAction(MULTIPLY_BOTH_SIDES);
		addStudentAction(DIVIDE_BOTH_SIDES);
		addStudentAction(APPLY_OTHER_OPERATION);
		addStudentAction(MANUALLY_TYPE_STEP);
		addStudentAction(UNDO_STEP);
		getAttributeWithName("expression").setValue("");
		getAttributeWithName("steps").setValue("");
		getAttributeWithName("fontSize").setValue(12);
	}

	@Override
	public void addDefaultAttributes() {
		addAttribute(new StringAttribute("expression"));
		StringAttribute steps = new StringAttribute("steps");
		steps.setUserEditable(false);
		addAttribute(steps);
		addAttribute(new IntegerAttribute("fontSize", 1, 50));
	}
	
	public void setAttributeValue(String n, Object o) throws AttributeException{
		if (n.equals("expression")){
			getAttributeWithName("steps").setValue("");
			getListWithName("steps").removeAll();
			getListWithName("steps").addValue(n.toString());
		}
		getAttributeWithName(n).setValue(o);
	}

	public void performSpecialObjectAction(String s){
		if (s.equals(UNDO_STEP)){
			if ( ! ((StringAttribute)getAttributeWithName("steps")).getValue().equals("")){
				String stepsString = getAttributeWithName("steps").getValue().toString();
				System.out.println(stepsString);
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
				String newSteps = "";
				for (int i = 0; i < steps.size() - 1; i++){
					newSteps += steps.get(i) + ";";
				}
				getAttributeWithName("steps").setValue(newSteps);
			}
			else{
				JOptionPane.showMessageDialog(null,
						"No steps to undo.",
						"Warning",
						JOptionPane.WARNING_MESSAGE);
			}
			return;
		}
		if (s.equals(MANUALLY_TYPE_STEP)){
			String child1 = (String)JOptionPane.showInputDialog(
					null,
					"Type the entire next line",
					"Type the entire next line.",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					null);
			if (child1.equals("")){
				return;
			}
			Node newNode = null;
			try {
				newNode = Node.parseNode(child1);
				getAttributeWithName("steps").setValue(
						getAttributeWithName("steps").getValue().toString()
						+ newNode.toStringRepresentation() + ";");
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		if (s.equals(COMBINE_LIKE_TERMS)){
			Node n = null;
			try {
				if (((StringAttribute)getAttributeWithName("steps")).getValue().equals("")){
					n = Node.parseNode( ((StringAttribute)getAttributeWithName("expression")).getValue());
				}
				else{
					String stepsString = getAttributeWithName("steps").getValue().toString();
					System.out.println(stepsString);
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
				n = n.collectLikeTerms();
				n = n.numericSimplify();
				getAttributeWithName("steps").setValue(
						getAttributeWithName("steps").getValue().toString()
						+ n.toStringRepresentation() + ";");
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		if (s.equals(SIMPLIFY)){
			Node n = null;
			try {
				if ( ((StringAttribute)getAttributeWithName("steps")).getValue().equals("")){
					n = Node.parseNode( ((StringAttribute)getAttributeWithName("expression")).getValue());
				}
				else{
					String stepsString = getAttributeWithName("steps").getValue().toString();
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
				n = n.simplify();

				getAttributeWithName("steps").setValue(
						getAttributeWithName("steps").getValue().toString()
						 + n.toStringRepresentation()+ ";");
			}catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return;
		}
		Node n = null;
		// all of the rest of the operations require an equals sign
		try{
			String expression = ((StringAttribute)getAttributeWithName("expression")).getValue();
			if ( ! expression.equals("")){
				if ( ((StringAttribute)getAttributeWithName("steps")).getValue().equals("")){
					n = Node.parseNode( ((StringAttribute)getAttributeWithName("expression")).getValue());
				}
				else{
					String stepsString = getAttributeWithName("steps").getValue().toString();
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
			System.out.println("problem with parse");
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
		if (s.equals(APPLY_OTHER_OPERATION)){
			Object[] operations = {"sqrt", "sin", "cos", "tan"};
			String op = (String)JOptionPane.showInputDialog(
			                    null,
			                    "Choose an operation to apply to both sides",
			                    "Operation Selection",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    operations,
			                    "sqrt");

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
			left.add(ex.getChild(0));
			newLeft.setChildren(left);
			
			Expression newRight = new Expression(o);
			Vector<Node> right = new Vector<Node>();
			right.add(ex.getChild(1));
			newRight.setChildren(right);
			
			Vector<Node> exChildren = new Vector<Node>();
			exChildren.add(newLeft);
			exChildren.add(newRight);
			ex.setChildren(exChildren);
			
			try {
				getAttributeWithName("steps").setValue(
						getAttributeWithName("steps").getValue().toString()
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
	
	public void multiplyOrDivideBothSides(Expression ex, Operator newOp, String message) throws NodeException{
		String child1 = (String)JOptionPane.showInputDialog(
				null,
				message,
				message,
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				null);
		if (child1.equals("")){
			return;
		}
		Node newNode = null;
		try {
			newNode = Node.parseNode(child1);
		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		getAttributeWithName("steps").setValue(
				getAttributeWithName("steps").getValue().toString()
				 + ex.toStringRepresentation()+ ";");
		
	}

	public void applyOpToBothSides(Expression ex, Operator newOp, String message) throws NodeException{
		String child1 = (String)JOptionPane.showInputDialog(
				null,
				message,
				message,
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				null);
		if (child1.equals("")){
			return;
		}
		Node newNode;
		try {
			newNode = Node.parseNode(child1);
			Expression newLeft = new Expression(newOp);
			Vector<Node> leftchildren = new Vector<Node>();
	
			//grab the left hand of the previous expression
			leftchildren.add(ex.getChild(0).cloneNode());
			leftchildren.add(newNode);
			newLeft.setChildren(leftchildren);
	
			Expression newRight = new Expression(newOp);
			Vector<Node> rightchildren = new Vector<Node>();
	
			//grab the left hand of the previous expression
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

		getAttributeWithName("steps").setValue(
				getAttributeWithName("steps").getValue().toString()
				 + ex.toStringRepresentation()+ ";");

	}

	public String getExpression(){
		return ((StringAttribute)getAttributeWithName("expression")).getValue();
	}

	public void setExpression(String s) throws AttributeException{
		setAttributeValue("expression", s);
	}

	public void setFontSize(int fontSize) throws AttributeException {
		setAttributeValue("fontSize", fontSize);
	}

	public int getFontSize() {
		return ((IntegerAttribute)getAttributeWithName("fontSize")).getValue();
	}

	@Override
	public String getType() {
		return EXPRESSION_OBJECT;
	}
}
