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

	public ExpressionObject(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		setStudentSelectable(true);
		addList(new ListAttribute<String>("steps"));
		addStudentAction(COMBINE_LIKE_TERMS);
		addStudentAction(SIMPLIFY);
		addStudentAction(ADD_TO_BOTH_SIDES);
		addStudentAction(SUBTRACT_FROM_BOTH_SIDES);
		addStudentAction(MULTIPLY_BOTH_SIDES);
		addStudentAction(DIVIDE_BOTH_SIDES);
		getAttributeWithName("expression").setValue("");
		getAttributeWithName("fontSize").setValue(12);
	}

	public ExpressionObject(Page p){
		super(p);
		setStudentSelectable(true);
		addList(new ListAttribute<String>("steps"));
		addStudentAction(COMBINE_LIKE_TERMS);
		addStudentAction(SIMPLIFY);
		addStudentAction(ADD_TO_BOTH_SIDES);
		addStudentAction(SUBTRACT_FROM_BOTH_SIDES);
		addStudentAction(MULTIPLY_BOTH_SIDES);
		addStudentAction(DIVIDE_BOTH_SIDES);
		getAttributeWithName("expression").setValue("");
		getAttributeWithName("fontSize").setValue(12);
	}

	public ExpressionObject() {
		setStudentSelectable(true);
		addList(new ListAttribute<String>("steps"));
		addStudentAction(COMBINE_LIKE_TERMS);
		addStudentAction(SIMPLIFY);
		addStudentAction(ADD_TO_BOTH_SIDES);
		addStudentAction(SUBTRACT_FROM_BOTH_SIDES);
		addStudentAction(MULTIPLY_BOTH_SIDES);
		addStudentAction(DIVIDE_BOTH_SIDES);
		getAttributeWithName("expression").setValue("");
		getAttributeWithName("fontSize").setValue(12);
	}

	@Override
	public void addDefaultAttributes() {
		addAttribute(new StringAttribute("expression"));
		addAttribute(new IntegerAttribute("fontSize", 1, 50));
	}
	
	public void setAttributeValue(String n, Object o) throws AttributeException{
		if (n.equals("expression")){
			getListWithName("steps").removeAll();
			getListWithName("steps").addValue(n.toString());
		}
		getAttributeWithName(n).setValue(o);
	}

	public void performSpecialObjectAction(String s){
		if (s.equals(COMBINE_LIKE_TERMS)){
			Node n;
			try {
				n = Node.parseNode( ((StringAttribute)getAttributeWithName("expression")).getValue());
				n = n.collectLikeTerms();
				n = n.numericSimplify();
				ExpressionObject exObj = new ExpressionObject(getParentPage(), getxPos(),
						getyPos() + getHeight(), this.getWidth() + 30, 30);
				exObj.getAttributeWithName("expression").setValue(n.toStringRepresentation());
				getParentPage().addObject(exObj);
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		if (s.equals(SIMPLIFY)){
			Node n = null;
			try {
				n = Node.parseNode( ((StringAttribute)getAttributeWithName("expression")).getValue());
				n = n.simplify();

				ExpressionObject exObj = new ExpressionObject(getParentPage(), getxPos(),
						getyPos() + getHeight(), this.getWidth() + 30, 30);
				exObj.getAttributeWithName("expression").setValue(n.toStringRepresentation());
				getParentPage().addObject(exObj);
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
				n = Node.parseNode( ((StringAttribute)getAttributeWithName("expression")).getValue());
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
		System.out.println("mult or divide both sides ( printed in expressionObject)");
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

		ExpressionObject exObj = new ExpressionObject(getParentPage(), getxPos(),
				getyPos() + getHeight(), this.getWidth() + 30, 30);
		exObj.getAttributeWithName("expression").setValue(ex.toStringRepresentation());
		getParentPage().addObject(exObj);
		
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

		ExpressionObject exObj = new ExpressionObject(getParentPage(), getxPos(),
				getyPos() + getHeight(), this.getWidth() + 30, 30);
		exObj.getAttributeWithName("expression").setValue(ex.toStringRepresentation());
		getParentPage().addObject(exObj);

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
