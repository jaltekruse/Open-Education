package doc.mathobjects;

import doc.Page;
import doc_gui.attributes.BooleanAttribute;
import doc_gui.attributes.DoubleAttribute;
import doc_gui.attributes.IntegerAttribute;
import doc_gui.attributes.MathObjectAttribute;
import doc_gui.attributes.StringAttribute;

import doc.expression_generators.ExpressionGenerator;

public class ExpressionGeneratorObject extends MathObject {

//	public static final ExpressionGenerator generator = new ExpressionGenerator();
	
	public ExpressionGeneratorObject(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		removeAction(MathObject.MAKE_INTO_PROBLEM);
	}
	
	public ExpressionGeneratorObject(Page p){
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
		// double minNumVal, double maxNumVal, 
		// boolean excludeZero, boolean subtractNegatives, boolean addNegatives, boolean indcludeFractions 
	}
	
	@Override
	public ArrowObject clone() {
		ArrowObject o = new ArrowObject(getParentPage());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		return o;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
