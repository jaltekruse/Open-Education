package doc.mathobjects;

import doc.Page;
import doc.attributes.AttributeException;
import doc.attributes.IntegerAttribute;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;
import doc.attributes.StringAttribute;

public class AnswerBoxObject extends MathObject {

	public static final String FONT_SIZE = "fontSize";
	public static final String ANSWER = "answer";
	public AnswerBoxObject(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		this.setStudentSelectable(true);
	}
	
	public AnswerBoxObject(MathObjectContainer p){
		super(p);
		this.setStudentSelectable(true);
	}

	public AnswerBoxObject() {
		this.setStudentSelectable(true);
	}

	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub
		addAttribute(new StringAttribute(ANSWER));
		getAttributeWithName(ANSWER).setValue("");
		getAttributeWithName("answer").setStudentEditable(true);
		addAttribute(new IntegerAttribute(FONT_SIZE, 1, 50));
		getAttributeWithName(FONT_SIZE).setValue(12);
		getAttributeWithName(FONT_SIZE).setStudentEditable(false);
	}

	public void setFontSize(int fontSize) throws AttributeException {
		setAttributeValue("fontSize", fontSize);
	}

	public int getFontSize() {
		return (Integer) getAttributeValue(FONT_SIZE);
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return ANSWER_BOX_OBJ;
	}
	
	public String getText(){
		return (String) getAttributeValue(ANSWER);
	}
	
	public void setText(String s) throws AttributeException{
		setAttributeValue("answer", s);
	}

	@Override
	public AnswerBoxObject clone() {
		// TODO Auto-generated method stub
		AnswerBoxObject o = new AnswerBoxObject(getParentContainer());
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
