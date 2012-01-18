package doc.mathobjects;

import doc.Page;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class EquationGenerator extends MathObject {

	public EquationGenerator(MathObjectContainer p){
		
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
		EquationGenerator o = new EquationGenerator(getParentContainer());
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

}
