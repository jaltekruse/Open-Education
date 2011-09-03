package doc.mathobjects;

import java.util.Vector;

import javax.swing.JOptionPane;

import doc.Page;
import doc_gui.attributes.StringAttribute;
import expression.*;
import expression.Number;

public class ProblemObject extends Grouping {
	
//	private Grouping groupOfObjects;
	public static final String GENERATE_NEW = "Generate new problem";
	public static final String GENERATE_LIST = "Generate list of problems";
	private static final int bufferSpace = 20;
	
	public ProblemObject(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		addAttribute(new StringAttribute("scripts"));
		getAttributeWithName("scripts").setValue("");
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		addAction(GENERATE_NEW);
		addAction(GENERATE_LIST);
		// TODO Auto-generated constructor stub
	}
	
	public ProblemObject(Page p){
		super(p);
		addAttribute(new StringAttribute("scripts"));
		getAttributeWithName("scripts").setValue("");
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		addAction(GENERATE_NEW);
		addAction(GENERATE_LIST);
	}
	
	public ProblemObject() {
		getAttributeWithName("scripts").setValue("");
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		addAction(GENERATE_NEW);
		addAction(GENERATE_LIST);
	}
	
	public String getScripts(){
		return ((StringAttribute)getAttributeWithName("scripts")).getValue();
	}
	
	public void performSpecialObjectAction(String s) {
		if (s.equals(GENERATE_NEW)){
			getParentPage().addObject(generateProblem());
		}
		if (s.equals(GENERATE_LIST)){
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
				try{
					number = Integer.parseInt(num);
				} catch ( Exception e){
					JOptionPane.showMessageDialog(null,
						    "Input must be an integer between 2 and 20",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
				if (number < 2 && number > 20){
					JOptionPane.showMessageDialog(null,
						    "Input must be an integer between 2 and 20",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			} while ( number < 2 && number > 20);
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
			int bufferspace = 15;
			System.out.println("greatestWidth:" + greatestWidth);
			int numColumns = (int) ( (getParentPage().getWidth()
					- 2 * getParentPage().getxMargin()) / (greatestWidth + bufferSpace) );
			int currColumn = 0;
			int currYpos = getyPos() + getHeight() + bufferSpace;
			for (Grouping g : newProblems){
				g.setxPos(getParentPage().getxMargin() + currColumn * (greatestWidth + bufferSpace));
				g.setyPos(currYpos);
				g.unGroup();
//				getParentPage().addObject(g);
				currColumn++;
				if (currColumn > numColumns - 1){
					currYpos += greatestHeight + bufferSpace;
					currColumn = 0;
				}
			}

		}
		if (s.equals(STORE_IN_DATABASE)){
			this.getParentPage().getParentDoc().docPanel.getNotebook().getDatabase().addGrouping(this);
		}
	}
	
	public Grouping generateProblem(){
		System.out.println("yPos make new prob:" + getyPos());
		Grouping newProblem = new Grouping(getParentPage(), getxPos(),
				getyPos() + getHeight() + bufferSpace, getWidth(), getHeight());
		System.out.println("width Prob:" + getWidth());
		System.out.println("yPos new prob:" + newProblem.getyPos());
		String scriptsString = getScripts();
		Vector<String> scripts = new Vector<String>();
		int lastEnd = 0;
		for (int i = 0; i < scriptsString.length(); i++){
			if (scriptsString.charAt(i) == ';'){
				scripts.add(scriptsString.substring(lastEnd, i));
				lastEnd = i + 1;
			}
			else if ( i == scriptsString.length() - 1){
				scripts.add(scriptsString.substring(lastEnd, i + 1));
			}
		}
		Node n =  null;
		Vector<String> varNames = new Vector<String>();
		Vector<Number> varVals = new Vector<Number>();
		for (String str : scripts){
			System.out.println("script:" + str );
			try {
				n = n.parseNode(str);
				
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
								System.out.println("add var");
								varNames.add(var.getIdentifier());
								varVals.add((Number) ex.getChild(1));
							}
						}
					}
				}
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String exString, textString;
		Node expression;
		Character prevChar, postChar;
		
		for (MathObject mathObj : getObjects()){
			mathObj.setxPos(getxPos() + (int) Math.round(mathObj.getxPos()/1000.0 * getWidth()) );
			mathObj.setyPos(getyPos() + (int) Math.round(mathObj.getyPos()/1000.0 * getHeight()) );
			mathObj.setWidth((int) Math.round(mathObj.getWidth()/1000.0 * getWidth()) );
			mathObj.setHeight((int) Math.round(mathObj.getHeight()/1000.0 * getHeight()) );
		}
		
		MathObject newObj;
		
		for (MathObject mObj : getObjects()){
			newObj = mObj.clone();
			if ( newObj instanceof ExpressionObject){
				exString = ((ExpressionObject)newObj).getExpression();
				try {
					expression = Node.parseNode(exString);

					for ( int i = 0 ; i < varNames.size(); i++){
						expression = expression.replace(varNames.get(i), varVals.get(i));
						((ExpressionObject)newObj).setExpression(expression.toStringRepresentation());
					}
				} catch (NodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AttributeException e) {
					// TODO Auto-generated catch block
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
					System.out.println(textString);
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
			getParentPage().getParentDoc().docPanel.drawObjectInBackgorund(newObj);
			newProblem.addObjectFromPage(newObj);
		}
		
		for (MathObject mathObj : getObjects()){
			mathObj.setWidth( (int) ((double)mathObj.getWidth()/getWidth() * 1000) );
			mathObj.setHeight( (int) ((double)mathObj.getHeight()/getHeight() * 1000) );
			mathObj.setxPos( (int) ( ((double) mathObj.getxPos() - getxPos())/getWidth() * 1000) );
			mathObj.setyPos( (int) ( ((double) mathObj.getyPos() - getyPos())/getHeight() * 1000) );
		}
		
		return newProblem;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return PROBLEM_OBJECT;
	}
}
