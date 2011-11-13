package doc.mathobjects;

import doc_gui.attributes.MathObjectAttribute;
import doc.Page;

public class EquationGenerator extends MathObject {

	public EquationGenerator(Page p){
		
	}
	
	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public EquationGenerator clone() {
		EquationGenerator o = new EquationGenerator(getParentPage());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		return o;
	}

}
