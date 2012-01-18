package doc.mathobjects;

import java.util.UUID;
import java.util.Vector;

import javax.swing.JOptionPane;

import doc.Document;
import doc.Page;
import doc.attributes.AttributeException;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;
import doc.attributes.StringAttribute;
import doc.attributes.UUIDAttribute;
import doc_gui.DocumentException;
import expression.*;
import expression.Number;

public class ProblemObject extends Grouping implements ProblemGenerator {

	public static final String REMOVE_PROBLEM = "Remove problem", GENERATE_NEW = "Generate problems";
	public static final String SCRIPTS = "scripts", UUID_STR = "uuid", TAGS = "tags(seperate with commas)";
	private static final int bufferSpace = 20;
	// store the parent document, allows access to document attributes from problems that
	// are in the list of generators but not on the page

	private Document parentDocument;

	public ProblemObject(MathObjectContainer p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		addAttribute(new StringAttribute(SCRIPTS));
		addAttribute(new UUIDAttribute(UUID_STR));
		getAttributeWithName(UUID_STR).setUserEditable(false);
		getAttributeWithName(UUID_STR).setValue(UUID.randomUUID());
		getAttributeWithName(SCRIPTS).setValue("");
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(Grouping.BRING_TO_LEFT);
		removeAction(Grouping.BRING_TO_TOP);
		removeAction(Grouping.BRING_TO_RIGHT);
		removeAction(Grouping.BRING_TO_BOTTOM);
		addAction(REMOVE_PROBLEM);
		addAction(GENERATE_NEW);

	}

	public ProblemObject(MathObjectContainer p){
		super(p);
		addAttribute(new StringAttribute(SCRIPTS));
		addAttribute(new UUIDAttribute(UUID_STR));
		getAttributeWithName(UUID_STR).setUserEditable(false);
		getAttributeWithName(UUID_STR).setValue(UUID.randomUUID());
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
		addAttribute(new StringAttribute(SCRIPTS));
		getAttributeWithName(SCRIPTS).setValue("");
		addAttribute(new UUIDAttribute(UUID_STR));
		getAttributeWithName(UUID_STR).setUserEditable(false);
		getAttributeWithName(UUID_STR).setValue(UUID.randomUUID());
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

	public MathObject subInVal(String s, Node val, MathObject mObj){
		MathObject newObj = mObj.clone();

		String exString, textString = null;
		Node expression;
		Character prevChar, postChar;
		
		if (newObj instanceof Grouping){
			Grouping newGroup = (Grouping) newObj.clone();
			newGroup.removeAllObjects();
			for ( MathObject mathObj : ((Grouping)newObj).getObjects()){
				newGroup.addObjectFromPage(subInVal(s, val, mathObj));
			}
			return newGroup;
		}
		else if ( newObj instanceof ExpressionObject){
			exString = ((ExpressionObject)newObj).getExpression();
			if (exString != null && ! exString.equals("")){
				try {
					expression = Node.parseNode(exString);

					expression = expression.replace(s, val);
					((ExpressionObject)newObj).setExpression(expression.toStringRepresentation());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else if ( newObj instanceof GraphObject){
			for ( StringAttribute exAtt : ((GraphObject)newObj).getExpressions()){
				if (exAtt.getValue() != null && ! exAtt.getValue().equals("")){
					try {
						expression = Node.parseNode(exAtt.getValue());
						expression = expression.replace(s, val);

						exAtt.setValue(expression.toStringRepresentation());
					} catch (Exception e) {	
						e.printStackTrace();
					}
				}
			}
		}
		else if ( newObj instanceof TextObject){
			textString = ((TextObject) newObj).getText();
			try {
				for (int j = 0; j < textString.length(); j++)
				{// loop through all characters in text
					if ( s.equals(textString.charAt(j) + "" ) )
					{// if the variable char was found
						if ( (j == 0 || ! Character.isLetter(textString.charAt(j - 1)) ) && 
								( j == textString.length() - 1 ||
								! Character.isLetter(textString.charAt(j + 1)) ) )
						{// if the character is surrounded by non-alphabetic chars
							if ( j != 0 && j != textString.length())
							{// if the char is not at the end or beginning
								textString = textString.substring(0, j) 
										+ val.toStringRepresentation() + 
										textString.substring(j+1);
							}
							else{
								if ( j == 0){
									if (textString.length() > 1)
										textString = val.toStringRepresentation() 
										+ textString.substring(j + 1);
									else
										textString = val.toStringRepresentation();
								}
								else if ( j == textString.length()){
									if (textString.length() > 1)
										textString = textString.substring(0, j - 1) +  
										val.toStringRepresentation();
									else
										textString = val.toStringRepresentation();
								}
							}
						}
					}
				}
				((TextObject) newObj).setText(textString);
			} catch (AttributeException e) {
				// TODO Auto-generated catch block
				// should not be thrown, as a text object can have any string as its child
				System.out.println("error that should not happen in ProblemObject");
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				System.out.println("node error that should not happen in ProblemObject");
			}
		}
		return newObj;
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
				this.getParentContainer().removeObject(this);
				this.getParentContainer().getParentDoc().getDocViewerPanel().setFocusedObject(null);
			}
		}
		else if (s.equals(STORE_IN_DATABASE)){
			getParentContainer().getParentDoc().getDocViewerPanel().getNotebook().getDatabase().addGrouping(this);
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
					if (number < 1 || number > 50){
						JOptionPane.showMessageDialog(null,
								"Input must be an integer between 1 and 50",
								"Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch ( Exception e){
					JOptionPane.showMessageDialog(null,
							"Input must be an integer between 1 and 50",
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} while ( number < 1 || number > 50);

			int greatestWidth = 0, greatestHeight = 0;
			Vector<GeneratedProblem> newProblems = new Vector<GeneratedProblem>();
			for (int i = 0; i < number; i++){
				GeneratedProblem newProb = generateProblem();
				newProblems.add(newProb);
				if (newProb.getWidth() > greatestWidth){
					greatestWidth = newProb.getWidth();
				}
				if (newProb.getHeight() > greatestHeight){
					greatestHeight = newProb.getHeight();
				}
			}

			// add this generator to the documents background list of generators for later
			// reference by the children
			try {
				getParentContainer().getParentDoc().addGenerator(this);
			} catch (Exception e1) {
				// What to do if generator ID collides with another, or if
				// the same generator tries to get added twice
				System.out.println(e1.getMessage());
			}

			int numColumns = ( (getParentContainer().getWidth()
					- 2 * getParentPage().getxMargin() - bufferSpace) / (greatestWidth + bufferSpace) );
			int totalExtraSpace = ( (getParentContainer().getWidth()
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
				}
				currentPage.addObject(g);
				g.setParentContainer(currentPage);
				currColumn++;
				if (currColumn > numColumns - 1){
					curryPos += greatestHeight + bufferSpace;
					currColumn = 0;
				}
			}

		}
	}

	public GeneratedProblem generateProblem(){
		Grouping newProblem = new Grouping(getParentContainer(), getxPos(),
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
							if (ex.getChild(1) instanceof Number){
								varNames.add(var.getIdentifier());
								// this causes a lot of unneeded parenthesis
								// but without it, you cannot sub in a value
								// where there is an implied parenthesis
								// ex.getChild(1).setDisplayParentheses(true);
								varVals.add((Number) ex.getChild(1));
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


		MathObject newObj = null;

		for (MathObject mObj : getObjects()){
			newObj = mObj.clone();
			for ( int i = 0 ; i < varNames.size(); i++){
				newObj = subInVal(varNames.get(i), varVals.get(i), newObj);
			}
			//shift object down so it doesn't overlap the current problem
			newObj.setyPos( newObj.getyPos() + getHeight() + bufferSpace);

			//expressions can change their bounds when the random values are substituted in
			//this line sets the bounds to the actual space it takes to render them
			if ( getParentContainer() != null){
				getParentDoc().getDocViewerPanel().drawObjectInBackgorund(newObj);
			}
			else{
				getParentDocument().getDocViewerPanel().drawObjectInBackgorund(newObj);
			}
			newObj.setParentContainer(newProblem.getParentContainer());
			newProblem.addObjectFromPage(newObj);
		}

		return new GeneratedProblem(newProblem.getParentContainer(), this.getUUID(), newProblem);
	}

	@Override
	public ProblemObject clone() {
		ProblemObject o = new ProblemObject(getParentContainer());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		o.removeAllLists();
		for ( ListAttribute list : getLists()){
			o.addList(list.clone());
		}
		for ( MathObject mObj : getObjects()){
			o.addObjectFromPage(mObj.clone());
		}
		return o;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return PROBLEM_OBJ;
	}

	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return ((UUIDAttribute)getAttributeWithName(UUID_STR)).getValue();
	}

	public Document getParentDocument() {
		return parentDocument;
	}

	public void setParentDocument(Document parentDocument) {
		this.parentDocument = parentDocument;
	}
}
