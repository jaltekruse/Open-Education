package doc.mathobjects;

import java.awt.Rectangle;
import java.util.Vector;

import doc.Document;
import doc.MathObjectContainer;
import doc.Page;
import doc_gui.attributes.MathObjectAttribute;
import doc_gui.attributes.StringAttribute;

public class Grouping extends MathObject implements MathObjectContainer{
	
	private Vector<MathObject> objects;
	public static final String STORE_IN_DATABASE = "Store in Database";
	public static final String BRING_TO_LEFT = "Bring all to Left";
	public static final String BRING_TO_TOP = "Bring all to Top";

	public Grouping(){
		objects = new Vector<MathObject>();
		addAction(MathObject.MAKE_INTO_PROBLEM);
		addAction(STORE_IN_DATABASE);
		addAction(BRING_TO_LEFT);
		addAction(BRING_TO_TOP);
//		setDate("");
//		setAuthor("");
		setTags("");
		setName("");
	}
	
	public Grouping(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		objects = new Vector<MathObject>();
		addAction(MathObject.MAKE_INTO_PROBLEM);
		addAction(STORE_IN_DATABASE);
		addAction(BRING_TO_LEFT);
		addAction(BRING_TO_TOP);
//		setDate("");
//		setAuthor("");
		setTags("");
		setName("");
	}
	
	public Grouping(Page p){
		super(p);
		objects = new Vector<MathObject>();
		addAction(MathObject.MAKE_INTO_PROBLEM);
		addAction(STORE_IN_DATABASE);
		addAction(BRING_TO_LEFT);
		addAction(BRING_TO_TOP);
//		setDate("");
//		setAuthor("");
		setTags("");
		setName("");
	}

	@Override
	public void addDefaultAttributes() {
		addAttribute(new StringAttribute("group name"));
//		addAttribute(new StringAttribute("author"));
//		addAttribute(new StringAttribute("date(dd/mm/yy)"));
		addAttribute(new StringAttribute("tags(separate by commas)"));
		
	}
	
	public void performSpecialObjectAction(String s) {
		if (s.equals(MathObject.MAKE_INTO_PROBLEM)){
			ProblemObject newProblem = new ProblemObject(getParentPage(), getxPos(),getyPos(),
					getWidth(), getHeight());
			
			//this group might be the temporary one in the docViewerPanel, so just reset it
			this.getParentPage().getParentDoc().docPanel.setTempGroup(new Grouping(new Page(new Document(""))));
			this.getParentPage().getParentDoc().docPanel.setFocusedObject(newProblem);

			newProblem.setObjects(getObjects());
			getParentPage().addObject(newProblem);
			getParentPage().removeObject(this);
		}
		else if (s.equals(STORE_IN_DATABASE)){
			this.getParentPage().getParentDoc().docPanel.getNotebook().getDatabase().addGrouping(this);
		}
		else if (s.equals(BRING_TO_LEFT)){
			for (MathObject mObj  : getObjects()){
				mObj.setxPos(0);
			}
			adjustSizeToFixChildren();
		}
		else if (s.equals(BRING_TO_TOP)){
			for (MathObject mObj  : getObjects()){
				mObj.setyPos(0);
			}
			adjustSizeToFixChildren();
		}
	}
	

	@Override
	public String getType() {
		return GROUPING;
	}
	
	public void setName(String s){
		getAttributeWithName("group name").setValue(s);
	}
	
	public String getName(){
		return ((StringAttribute) getAttributeWithName("group name")).getValue();
	}
	
	public void setAuthor(String s){
		getAttributeWithName("author").setValue(s);
	}
	
	public String getAuthor(){
		return ((StringAttribute) getAttributeWithName("author")).getValue();
	}
	
	public void setDate(String s){
		getAttributeWithName("date(dd/mm/yy)").setValue(s);
	}
	
	public String getDate(){
		return ((StringAttribute) getAttributeWithName("date(dd/mm/yy)")).getValue();
	}
	
	public void setTags(String s){
		getAttributeWithName("tags(separate by commas)").setValue(s);
	}
	
	public String getTags(){
		return ((StringAttribute) getAttributeWithName("tags(separate by commas)")).getValue();
	}

	public void setObjects(Vector<MathObject> objects) {
		this.objects = objects;
	}

	public Vector<MathObject> getObjects() {
		return objects;
	}
	
	public boolean addObject(MathObject mObj){
		objects.add(mObj);
		return true;
	}
	
	public void convertPositionsToPage(){
		//converts objects back to their on page positions
		for (MathObject mathObj : objects){
			mathObj.setxPos(getxPos() + (int) Math.round(mathObj.getxPos()/1000.0 * getWidth()) );
			mathObj.setyPos(getyPos() + (int) Math.round(mathObj.getyPos()/1000.0 * getHeight()) );
			mathObj.setWidth((int) Math.round(mathObj.getWidth()/1000.0 * getWidth()) );
			mathObj.setHeight((int) Math.round(mathObj.getHeight()/1000.0 * getHeight()) );
		}
	}
	
	public void convertPositionsGroupRelative(){
		for (MathObject mathObj : objects){
			mathObj.setWidth( (int) ((double)mathObj.getWidth()/getWidth() * 1000) );
			mathObj.setHeight( (int) ((double)mathObj.getHeight()/getHeight() * 1000) );
			mathObj.setxPos( (int) ( ((double) mathObj.getxPos() - getxPos())/getWidth() * 1000) );
			mathObj.setyPos( (int) ( ((double) mathObj.getyPos() - getyPos())/getHeight() * 1000) );
		}
	}

	public void adjustSizeToFixChildren(){
		Vector<MathObject> temp = new Vector<MathObject>();
		convertPositionsToPage();
		for (MathObject mObj : getObjects()){
			temp.add(mObj.clone());
		}
		getObjects().removeAllElements();
		for (MathObject mObj : temp){
			addObjectFromPage(mObj);
		}
		
	}
	
	public boolean addObjectFromPage(MathObject mObj){
		if (this.getParentPage() != mObj.getParentPage()){
			return false;
		}
		if (objects.size() == 0){
			setWidth(mObj.getWidth());
			setHeight(mObj.getHeight());
			setxPos(mObj.getxPos());
			setyPos(mObj.getyPos());
			
			//positions of objects within Groups are relative to the group origin
			//and saved in a fraction of the total width/height, instead of number of pixels
			mObj.setxPos(0);
			mObj.setyPos(0);
			mObj.setWidth(1000);
			mObj.setHeight(1000);
			objects.add(mObj);
			return true;
		}
		Rectangle objRect = new Rectangle(mObj.getxPos(), mObj.getyPos(), mObj.getWidth(), mObj.getHeight());
		Rectangle groupRect = new Rectangle(getxPos(), getyPos(), getWidth(), getHeight());
		if (groupRect.contains(objRect))
		{//the object fits into the current group rectangle, it must be added and have its position/size adjusted
			//so it is relative to the size of the group
			mObj.setWidth( (int) ((double)mObj.getWidth()/getWidth() * 1000) );
			mObj.setHeight( (int) ((double)mObj.getHeight()/getHeight() * 1000) );
			mObj.setxPos( (int) ( ((double) mObj.getxPos() - getxPos())/getWidth() * 1000) );
			mObj.setyPos( (int) ( ((double) mObj.getyPos() - getyPos())/getHeight() * 1000) );
			objects.add(mObj);
			return true;
		}
		else{
			int oldx = getxPos();
			int oldy = getyPos();
			int oldWidth = getWidth();
			int oldHeight = getHeight();
			
			//converts objects back to their on page positions
			convertPositionsToPage();
			
			if ( mObj.getxPos() < oldx){
				setxPos(mObj.getxPos());
				setWidth( getWidth() + oldx - mObj.getxPos());
			}
			if (mObj.getxPos() + mObj.getWidth() > getxPos() + getWidth()){
				setWidth( mObj.getxPos() + mObj.getWidth() - getxPos());
			}
			if ( mObj.getyPos() < oldy){
				setyPos(mObj.getyPos());
				setHeight( getHeight() + oldy - mObj.getyPos());
			}
			if (mObj.getyPos() + mObj.getHeight() > getyPos() + getHeight()){
				setHeight( mObj.getyPos() + mObj.getHeight() - getyPos());
			}
			
			objects.add(mObj);
			
			convertPositionsGroupRelative();
			return true;
		}
	}
	
	public void unGroup(){
		//converts objects back to their on page positions, add them to the page
		convertPositionsToPage();
		
		for (MathObject mathObj : objects){
			getParentPage().addObject(mathObj);
			mathObj.setParentPage(getParentPage());
		}
		
		
		//note, this method does not remove the group from the page, it must be done externally
	}

	@Override
	public boolean childObjectsFocusable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String exportToXML(){
		String output = "";
		output += "<" + getType() + ">\n";
		for (MathObjectAttribute mAtt : getAttributes()){
			output += mAtt.export();
		}
		for (MathObject mObj : getObjects()){
			output += mObj.exportToXML();
		}
		output += "</" + getType() + ">\n";
		return output;
	}
	

	@Override
	public boolean isResizable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean removeAllObjects() {
		// TODO Auto-generated method stub
		objects = new Vector<MathObject>();
		return true;
	}
}
