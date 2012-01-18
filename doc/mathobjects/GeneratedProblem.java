package doc.mathobjects;

import java.util.UUID;

import doc.Page;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;
import doc.attributes.UUIDAttribute;

public class GeneratedProblem extends Grouping{

	public static final String GENERATE_NEW_PROBLEM = "Gernerate new",
			REMOVE_PROBLEM = "remove problem", UUID_STR = "uuid", VIEW_PROBLEM_FORMULA = "View problem formula";	
	
	public GeneratedProblem(MathObjectContainer p, UUID genID, Grouping contents){
		super(p);
		
		// the attributes of the previous grouping shouldn't need to be
		// cloned as the grouping will likely get destroyed after the
		// creation of this object. However, they are copied here to prevent
		// future problems
		removeAllAttributes();
		for ( MathObjectAttribute mAtt : contents.getAttributes()){
			addAttribute(mAtt.clone());
		}
		addAttribute(new UUIDAttribute(UUID_STR, genID));
		getAttributeWithName(UUID_STR).setUserEditable(false);
		getAttributeWithName(UUID_STR).setValue(genID);
		
		for ( MathObject mObj : contents.getObjects()){
			mObj.setParentContainer(getParentContainer());
			addObjectFromPage(mObj.clone());
		}
		
		// remove actions that are added in the Grouping constructor
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(STORE_IN_DATABASE);
		removeAction(BRING_TO_LEFT);
		removeAction(BRING_TO_RIGHT);
		removeAction(BRING_TO_TOP);
		removeAction(BRING_TO_BOTTOM);
		
		addAction(GENERATE_NEW_PROBLEM);
		addAction(VIEW_PROBLEM_FORMULA);
//		addAction(REMOVE_PROBLEM);
	}
	
	public GeneratedProblem(MathObjectContainer p) {
		super(p);
		
		addAttribute(new UUIDAttribute(UUID_STR));
		getAttributeWithName(UUID_STR).setUserEditable(false);
		// remove actions that are added in the Grouping constructor
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(STORE_IN_DATABASE);
		removeAction(BRING_TO_LEFT);
		removeAction(BRING_TO_RIGHT);
		removeAction(BRING_TO_TOP);
		removeAction(BRING_TO_BOTTOM);
		
		addAction(GENERATE_NEW_PROBLEM);
		addAction(VIEW_PROBLEM_FORMULA);
//		addAction(REMOVE_PROBLEM);
	}
	
	public GeneratedProblem() {		
		super();
		
		addAttribute(new UUIDAttribute(UUID_STR));
		getAttributeWithName(UUID_STR).setUserEditable(false);
		// remove actions that are added in the Grouping constructor
		removeAction(MathObject.MAKE_INTO_PROBLEM);
		removeAction(STORE_IN_DATABASE);
		removeAction(BRING_TO_LEFT);
		removeAction(BRING_TO_RIGHT);
		removeAction(BRING_TO_TOP);
		removeAction(BRING_TO_BOTTOM);
		
		addAction(GENERATE_NEW_PROBLEM);
		addAction(VIEW_PROBLEM_FORMULA);
//		addAction(REMOVE_PROBLEM);
	}

	public ProblemGenerator getProblemGenerator(){
		return getParentContainer().getParentDoc().getGeneratorWithID(
				(UUID) getAttributeWithName(UUID_STR).getValue());
	}
	
	/**
	 * Replaces this problems with a new one produced from the problems
	 * generator.
	 * @return 
	 */
	public GeneratedProblem generateNewProblem(){
		GeneratedProblem newProb = getProblemGenerator().generateProblem();
		newProb.setxPos(getxPos());
		newProb.setyPos(getyPos());
		getParentContainer().addObject(newProb);
		newProb.setParentContainer(getParentContainer());
		getParentContainer().removeObject(this);
		return newProb;
	}

	@Override
	public void addDefaultAttributes() {}

	@Override
	public void performSpecialObjectAction(String s){
		if (s.equals(GENERATE_NEW_PROBLEM)){
			getParentContainer().getParentDoc().getDocViewerPanel().setFocusedObject(generateNewProblem());
		}
		if (s.equals(VIEW_PROBLEM_FORMULA)){
			getParentContainer().getParentDoc().
				getDocViewerPanel().getNotebook().getNotebookPanel().
				viewProblemGnerator(getParentDoc().getGeneratorWithID(getUUID()));
		}
	}
	
	@Override
	public String getType() {
		return MathObject.GENERATED_PROBLEM;
	}

	public UUID getUUID(){
		return (UUID) getAttributeWithName(UUID_STR).getValue();
	}
	@Override
	public GeneratedProblem clone() {
		GeneratedProblem o = new GeneratedProblem(getParentContainer());
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
	
}
