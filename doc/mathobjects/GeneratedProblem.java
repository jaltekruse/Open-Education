package doc.mathobjects;

import doc.Page;
import doc_gui.attributes.MathObjectAttribute;

public class GeneratedProblem extends Grouping{

	private ProblemGenerator generator;
	public static final String GENERATE_NEW_PROBLEM = "gernerate new";
	public static final String EDIT_GENERATOR = "edit generator";
	public static final String REMOVE_PROBLEM = "remove problem";
	
	public GeneratedProblem(Page p, ProblemGenerator gen, Grouping contents){
		super(p);
		generator = gen;
		
		// the attributes of the previous grouping shouldn't need to be
		// copied, as the grouping will likely get destroyed after the
		// creation of this object. However, they are copied here to prevent
		// future problems
		for ( MathObjectAttribute mAtt : contents.getAttributes()){
			addAttribute(mAtt.clone());
		}
		for ( MathObject mObj : contents.getObjects()){
			addObjectFromPage(mObj);
		}
		
		// remove actions that are added in the Grouping constructor
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(STORE_IN_DATABASE);
		removeAction(BRING_TO_LEFT);
		removeAction(BRING_TO_RIGHT);
		removeAction(BRING_TO_TOP);
		removeAction(BRING_TO_BOTTOM);
		
		addAction(GENERATE_NEW_PROBLEM);
//		addAction(REMOVE_PROBLEM);
	}
	
	public GeneratedProblem(Page p) {
		super(p);
		
		// remove actions that are added in the Grouping constructor
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(STORE_IN_DATABASE);
		removeAction(BRING_TO_LEFT);
		removeAction(BRING_TO_RIGHT);
		removeAction(BRING_TO_TOP);
		removeAction(BRING_TO_BOTTOM);
		
		addAction(GENERATE_NEW_PROBLEM);
//		addAction(REMOVE_PROBLEM);
	}
	
	public GeneratedProblem() {
		// TODO Auto-generated constructor stub
		
		super();
		
		// remove actions that are added in the Grouping constructor
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(STORE_IN_DATABASE);
		removeAction(BRING_TO_LEFT);
		removeAction(BRING_TO_RIGHT);
		removeAction(BRING_TO_TOP);
		removeAction(BRING_TO_BOTTOM);
		
		addAction(GENERATE_NEW_PROBLEM);
//		addAction(REMOVE_PROBLEM);
	}

	public ProblemGenerator getProblemGenerator(){
		return generator;
	}
	
	public GeneratedProblem generateNewProblem(){
		return generator.generateProblem();
	}

	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performSpecialObjectAction(String s){
		if (s.equals(GENERATE_NEW_PROBLEM)){
			GeneratedProblem newProb = generator.generateProblem();
			newProb.setxPos(getxPos());
			newProb.setyPos(getyPos());
			newProb.setHeight(getHeight());
			newProb.setWidth(getWidth());
			getParentPage().addObject(newProb);
			getParentPage().removeObject(this);
			getParentPage().getParentDoc().getDocViewerPanel().setFocusedObject(newProb);
		}
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return MathObject.GENERATED_PROBLEM;
	}

	@Override
	public GeneratedProblem clone() {
		// TODO Auto-generated method stub
		GeneratedProblem o = new GeneratedProblem(getParentPage(), generator, this);
		for ( MathObjectAttribute mAtt : getAttributes()){
			addAttribute(mAtt.clone());
		}
		return o;
	}
	
}
