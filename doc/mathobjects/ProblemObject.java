package doc.mathobjects;

import java.util.Vector;

import javax.swing.JOptionPane;

import doc.Page;
import doc_gui.DocumentException;
import doc_gui.attributes.MathObjectAttribute;
import doc_gui.attributes.StringAttribute;
import expression.*;
import expression.Number;

public class ProblemObject extends Grouping implements ProblemGenerator {
	
	public static final String REMOVE_PROBLEM = "Remove problem";
	public static final String GENERATE_NEW = "Generate problems";
	
	public static final String SCRIPTS = "scripts";
	
	/*
	 a = randomInt(5, 10);
	 b=randomInt(2,5);
	 d=randomInt(3,6);
	 e=randomInt(3,9);
	 p=2*(a*x-c) + 2*(d*x+e);
	 */
	private static final int bufferSpace = 20;
	
	public ProblemObject(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		addAttribute(new StringAttribute(SCRIPTS));
		getAttributeWithName(SCRIPTS).setValue("");
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(Grouping.BRING_TO_LEFT);
		removeAction(Grouping.BRING_TO_TOP);
		removeAction(Grouping.BRING_TO_RIGHT);
		removeAction(Grouping.BRING_TO_BOTTOM);
		addAction(REMOVE_PROBLEM);
		addAction(GENERATE_NEW);

	}
	
	public ProblemObject(Page p){
		super(p);
		addAttribute(new StringAttribute(SCRIPTS));
		getAttributeWithName(SCRIPTS).setValue("");
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(Grouping.BRING_TO_LEFT);
		removeAction(Grouping.BRING_TO_TOP);
		removeAction(Grouping.BRING_TO_RIGHT);
		removeAction(Grouping.BRING_TO_BOTTOM);
		addAction(REMOVE_PROBLEM);
		addAction(GENERATE_NEW);
	}
	
	public ProblemObject() {
		getAttributeWithName(SCRIPTS).setValue("");
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(Grouping.BRING_TO_LEFT);
		removeAction(Grouping.BRING_TO_TOP);
		removeAction(Grouping.BRING_TO_RIGHT);
		removeAction(Grouping.BRING_TO_BOTTOM);
		addAction(REMOVE_PROBLEM);
		addAction(GENERATE_NEW);
	}
	
	public String getScripts(){
		return ((StringAttribute)getAttributeWithName(SCRIPTS)).getValue();
	}
	
	@Override
	public void performSpecialObjectAction(String s) {
		if (s.equals(REMOVE_PROBLEM)){
			Object[] options = {"Continue", "Cancel"};
			int n = JOptionPane.showOptionDialog(null,
			    "This operation will ungroup the objects in this problem and\n" +
			    "place them back on the page. It will also delete the problem's\n" +
			    "script data.",
			    "Revert a Problem",
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.WARNING_MESSAGE,
			    null,
			    options,
			    options[1]);

			if (n == 0){ 
				this.unGroup();
				this.getParentPage().removeObject(this);
			}
		}
		else if (s.equals(STORE_IN_DATABASE)){
			getParentPage().getParentDoc().getDocViewerPanel().getNotebook().getDatabase().addGrouping(this);
		}
		else if (s.equals(GENERATE_NEW)){
			int number = 0;
			do {
				String num = (String)JOptionPane.showInputDialog(
						null,
						"Number of problems",
						"Number of problems.",
						JOptionPane.PLAIN_MESSAGE,
						null,
						null,
						null);
				if (num == null)
				{// the user hit cancel or the exit button on the dialog
					return;
				}
				try{
					number = Integer.parseInt(num);
				} catch ( Exception e){
					JOptionPane.showMessageDialog(null,
						    "Input must be an integer between 1 and 50",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
				if (number < 1 || number > 50){
					JOptionPane.showMessageDialog(null,
						    "Input must be an integer between 1 and 50",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			} while ( number < 1 || number > 50);
			
			int greatestWidth = 0, greatestHeight = 0;
			Vector<Grouping> newProblems = new Vector<Grouping>();
			for (int i = 0; i < number; i++){
				Grouping newProb = generateProblem();
				newProblems.add(newProb);
				if (newProb.getWidth() > greatestWidth){
					greatestWidth = newProb.getWidth();
				}
				if (newProb.getHeight() > greatestHeight){
					greatestHeight = newProb.getHeight();
				}
			}
			int numColumns = ( (getParentPage().getWidth()
					- 2 * getParentPage().getxMargin() - bufferSpace) / (greatestWidth + bufferSpace) );
			int totalExtraSpace = ( (getParentPage().getWidth()
					- 2 * getParentPage().getxMargin() - bufferSpace) % (greatestWidth + bufferSpace) );
			
			int extraColumnSpace = totalExtraSpace / (numColumns + 1);
			int currColumn = 0;
			int curryPos = getyPos() + getHeight() + bufferSpace;
			Page currentPage = getParentPage();
			
			for (Grouping g : newProblems){
				g.setxPos(currentPage.getxMargin() + bufferSpace + extraColumnSpace + 
						currColumn * (greatestWidth + bufferSpace + extraColumnSpace));
				g.setyPos(curryPos);
				if ( ! g.isOnPage()){
					System.out.println("not on page");
					try {
						if ( currentPage.getParentDoc().getNumPages() < currentPage.getParentDoc().getPageIndex(currentPage) + 2)
						{// a new page must be added to add the objects
							currentPage.getParentDoc().addBlankPage();
							currentPage = currentPage.getParentDoc().getPage(currentPage.getParentDoc().getNumPages() - 1);
							currentPage.getParentDoc().getDocViewerPanel().resizeViewWindow();
						}
						else
						{// there is a next page on the document that the new objects can be added to
							currentPage = currentPage.getParentDoc().getPage( currentPage.getParentDoc().getPageIndex(currentPage) + 1);
						}
						
						curryPos = currentPage.getyMargin() + bufferSpace;
						g.setyPos(curryPos);
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				currentPage.addObject(g);
				g.setParentPage(currentPage);
				currColumn++;
				if (currColumn > numColumns - 1){
					curryPos += greatestHeight + bufferSpace;
					currColumn = 0;
				}
			}

		}
	}
	
	public GeneratedProblem generateProblem(){
		Grouping newProblem = new Grouping(getParentPage(), getxPos(),
				getyPos() + getHeight() + bufferSpace, getWidth(), getHeight());
		String scriptsString = getScripts();
		String[] scripts = scriptsString.split(";");
		Node n =  null;
		Vector<String> varNames = new Vector<String>();
		Vector<Number> varVals = new Vector<Number>();
		for (String str : scripts){
			if (str == null || str.equals("")){
				continue;
			}
			try {
				n = Node.parseNode(str);
				
				//sub in variables already assigned in previous scripts
				for ( int i = 0 ; i < varNames.size(); i++){
					n = n.replace(varNames.get(i), varVals.get(i));
				}
				
				n = n.numericSimplify();
				
				if (n instanceof Expression){
					Expression ex = (Expression) n;
					if (ex.getOperator() instanceof Operator.Equals){
						if (ex.getChild(0) instanceof Identifier){
							Identifier var = (Identifier)ex.getChild(0);
							System.out.println("found var " + ex.getChild(1).toStringRepresentation());
							if (ex.getChild(1) instanceof Number){
								varNames.add(var.getIdentifier());
								varVals.add((Number) ex.getChild(1));
								System.out.println(var.getIdentifier() 
										+ " = " + ex.getChild(1).toStringRepresentation());
							}
						}
					}
				}
			} catch (NodeException e) {
				JOptionPane.showMessageDialog(null,
					    "Error generating a problem, check scripts.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
		
		String exString, textString;
		Node expression;
		Character prevChar, postChar;
		
		MathObject newObj;
		
		for (MathObject mObj : getObjects()){
			newObj = mObj.clone();
			newObj.setParentPage(mObj.getParentPage());
			mObj.getParentPage().addObject(newObj);
			if ( newObj instanceof ExpressionObject){
				exString = ((ExpressionObject)newObj).getExpression();
				try {
					expression = Node.parseNode(exString);

					for ( int i = 0 ; i < varNames.size(); i++){
						expression = expression.replace(varNames.get(i), varVals.get(i));
						((ExpressionObject)newObj).setExpression(expression.toStringRepresentation());
					}
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			
			}
			else if ( newObj instanceof TextObject){
				textString = ((TextObject) newObj).getText();
				for ( int i = 0 ; i < varNames.size(); i++){
					for (int j = 0; j < textString.length(); j++){
						if ( varNames.get(i).equals(textString.charAt(j) + "" ) ){
							System.out.println("matched char");
							if ( (j == 0 || ! Character.isLetter(textString.charAt(j - 1)) ) && 
									( j == textString.length() - 1 ||
									! Character.isLetter(textString.charAt(j + 1)) ) ){
								if ( j != 0 && j != textString.length()){
									textString = textString.substring(0, j) + varVals.get(i).toStringRepresentation() + 
									textString.substring(j+1);
								}
								else{
									if ( j == 0){
										System.out.println("at start, length: " + textString.length());
										if (textString.length() > 1)
											textString = varVals.get(i).toStringRepresentation() + textString.substring(j + 1);
										else
											textString = varVals.get(i).toStringRepresentation() + "";
									}
									else if ( j == textString.length()){
										if (textString.length() > 1)
											textString = textString.substring(0, j - 1) +  varVals.get(i).toStringRepresentation();
										else
											textString = varVals.get(i).toStringRepresentation() + "";
									}
								}
							}
						}
					}
				}
				try {
					((TextObject) newObj).setText(textString);
				} catch (AttributeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//shift object down so it doesn't overlap the current problem
			newObj.setyPos( newObj.getyPos() + getHeight() + bufferSpace);
			
			//expressions can change their bounds when the random values are substituted in
			//this line set the bounds to the actual space it takes to render them
			getParentPage().getParentDoc().getDocViewerPanel().drawObjectInBackgorund(newObj);
			newProblem.addObjectFromPage(newObj);
			newObj.getParentPage().removeObject(newObj);
		}

		return new GeneratedProblem(newProblem.getParentPage(), this, newProblem);
	}
	
	@Override
	public ProblemObject clone() {
		ProblemObject o = new ProblemObject(getParentPage());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		for ( MathObject mObj : getObjects()){
			o.addObjectFromPage(mObj);
		}
		return o;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return PROBLEM_OBJECT;
	}
}
