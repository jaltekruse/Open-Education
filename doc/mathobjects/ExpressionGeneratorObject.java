package doc.mathobjects;

import doc.Page;

import doc.attributes.BooleanAttribute;
import doc.attributes.DoubleAttribute;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;
import doc.attributes.StringAttribute;
import doc.expression_generators.ExpressionGenerator;

public class ExpressionGeneratorObject extends MathObject {

//	public static final ExpressionGenerator generator = new ExpressionGenerator();
	
	public ExpressionGeneratorObject(MathObjectContainer p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		removeAction(MathObject.MAKE_INTO_PROBLEM);
	}
	
	public ExpressionGeneratorObject(MathObjectContainer p){
		super(p);
		removeAction(MathObject.MAKE_INTO_PROBLEM);
	}
	
	public ExpressionGeneratorObject() {
		removeAction(MathObject.MAKE_INTO_PROBLEM);
	}
	
	public void performAction(){
		
	}
	
	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub
		addAttribute( new StringAttribute("operators"));
		addAttribute( new StringAttribute("variables"));
		addAttribute( new IntegerAttribute("numOperations"));
		addAttribute( new DoubleAttribute("maxAbsoluteValue"));
		addAttribute( new DoubleAttribute("minNumVal"));
		addAttribute( new DoubleAttribute("maxNumVal"));
		addAttribute( new BooleanAttribute("excludeZero"));
		addAttribute( new BooleanAttribute("subtractNegatives"));
		addAttribute( new BooleanAttribute("addNegatives"));
		addAttribute( new BooleanAttribute("includeFractions"));
		addAttribute( new BooleanAttribute("difficulty")); 
	}
	
	@Override
	public ArrowObject clone() {
		ArrowObject o = new ArrowObject(getParentContainer());
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
		return null;
	}

}
