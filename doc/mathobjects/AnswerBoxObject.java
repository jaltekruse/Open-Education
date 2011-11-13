package doc.mathobjects;

import doc.Page;
import doc_gui.attributes.IntegerAttribute;
import doc_gui.attributes.MathObjectAttribute;
import doc_gui.attributes.StringAttribute;

public class AnswerBoxObject extends MathObject {

	public AnswerBoxObject(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		this.setStudentSelectable(true);
	}
	
	public AnswerBoxObject(Page p){
		super(p);
		this.setStudentSelectable(true);
	}

	public AnswerBoxObject() {
		this.setStudentSelectable(true);
	}

	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub
		addAttribute(new StringAttribute("answer"));
		getAttributeWithName("answer").setValue("");
		getAttributeWithName("answer").setStudentEditable(true);
		addAttribute(new IntegerAttribute("fontSize", 1, 50));
		getAttributeWithName("fontSize").setValue(12);
		getAttributeWithName("fontSize").setStudentEditable(false);
	}

	public void setFontSize(int fontSize) throws AttributeException {
		setAttributeValue("fontSize", fontSize);
	}

	public int getFontSize() {
		return ((IntegerAttribute)getAttributeWithName("fontSize")).getValue();
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return ANSWER_BOX;
	}
	
	public String getText(){
		return ((StringAttribute)getAttributeWithName("answer")).getValue();
	}
	
	public void setText(String s) throws AttributeException{
		setAttributeValue("answer", s);
	}

	@Override
	public AnswerBoxObject clone() {
		// TODO Auto-generated method stub
		AnswerBoxObject o = new AnswerBoxObject(getParentPage());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		return o;
	}

}
