package doc.mathobjects;

import java.awt.Rectangle;
import java.util.Vector;

import doc.MathObjectContainer;
import doc.Page;
import doc_gui.attributes.Date;
import doc_gui.attributes.DateAttribute;
import doc_gui.attributes.IntegerAttribute;
import doc_gui.attributes.MathObjectAttribute;
import doc_gui.attributes.StringAttribute;

public class Grouping extends MathObject implements MathObjectContainer{
	
	private Vector<MathObject> objects;
	private Vector<DecimalRectangle> objectBounds;
	
	public static final String DATE = "date";
	public static final String TAGS = "tags(separate by commas)";
	public static final String AUTHOR = "author";
	public static final String NAME = "group name"; 
	
	public static final String STORE_IN_DATABASE = "Store in Database";
	public static final String BRING_TO_LEFT = "Bring all to Left";
	public static final String BRING_TO_RIGHT = "Bring all to Right";
	public static final String BRING_TO_TOP = "Bring all to Top";
	public static final String BRING_TO_BOTTOM = "Bring all to Bottom";

	public Grouping(){
		objects = new Vector<MathObject>();
		objectBounds = new Vector<DecimalRectangle>();
		addMoreAttributes();
		addAction(MathObject.MAKE_INTO_PROBLEM);
		addAction(STORE_IN_DATABASE);
		addAction(BRING_TO_LEFT);
		addAction(BRING_TO_RIGHT);
		addAction(BRING_TO_TOP);
		addAction(BRING_TO_BOTTOM);
		setDate(new Date());
		setAuthor("");
		setTags("");
		setName("");
	}
	
	public Grouping(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		objects = new Vector<MathObject>();
		objectBounds = new Vector<DecimalRectangle>();
		addMoreAttributes();
		addAction(MathObject.MAKE_INTO_PROBLEM);
		addAction(STORE_IN_DATABASE);
		addAction(BRING_TO_LEFT);
		addAction(BRING_TO_RIGHT);
		addAction(BRING_TO_TOP);
		addAction(BRING_TO_BOTTOM);
		setDate(new Date());
		setAuthor("");
		setTags("");
		setName("");
	}
	
	public Grouping(Page p){
		super(p);
		objects = new Vector<MathObject>();
		objectBounds = new Vector<DecimalRectangle>();
		addMoreAttributes();
		addAction(MathObject.MAKE_INTO_PROBLEM);
		addAction(STORE_IN_DATABASE);
		addAction(BRING_TO_LEFT);
		addAction(BRING_TO_RIGHT);
		addAction(BRING_TO_TOP);
		addAction(BRING_TO_BOTTOM);
		setDate(new Date());
		setAuthor("");
		setTags("");
		setName("");
	}

	@Override
	public void addDefaultAttributes() {

	}
	
	public void addMoreAttributes(){
		MathObjectAttribute name = new StringAttribute(NAME);
		name.setUserEditable(false);
		addAttribute(name);
		MathObjectAttribute tags = new StringAttribute(TAGS);
		tags.setUserEditable(false);
		addAttribute(tags);
		MathObjectAttribute author = new StringAttribute(AUTHOR);
		author.setUserEditable(false);
		addAttribute(author);
		MathObjectAttribute date = new DateAttribute(DATE);
		date.setUserEditable(false);
		addAttribute(date);
	}
	
	@Override
	public void performSpecialObjectAction(String s) {
		if (s.equals(MathObject.MAKE_INTO_PROBLEM)){
			ProblemObject newProblem = new ProblemObject(getParentPage(), getxPos(),getyPos(),
					getWidth(), getHeight());
			
			//this group might be the temporary one in the docViewerPanel, so just reset it
			this.getParentPage().getParentDoc().getDocViewerPanel().resetTempGroup();
			this.getParentPage().getParentDoc().getDocViewerPanel().setFocusedObject(newProblem);

			newProblem.setObjects(getObjects());
			newProblem.setObjectBounds(getObjectBounds());
			getParentPage().addObject(newProblem);
			getParentPage().removeObject(this);
		}
		else if (s.equals(STORE_IN_DATABASE)){
			this.getParentPage().getParentDoc().getDocViewerPanel().getNotebook().getDatabase().addGrouping(this);
		}
		else if (s.equals(BRING_TO_LEFT)){
			for (MathObject mObj  : getObjects()){
				mObj.setxPos(getxPos());
			}
			adjustSizeToFixChildren();
		}
		else if (s.equals(BRING_TO_TOP)){
			for (MathObject mObj  : getObjects()){
				mObj.setyPos(getyPos());
			}
			adjustSizeToFixChildren();
		}
		else if (s.equals(BRING_TO_RIGHT)){
			for (MathObject mObj  : getObjects()){
				mObj.setxPos(getxPos() + getWidth() - mObj.getWidth());
			}
			adjustSizeToFixChildren();
		}
		else if (s.equals(BRING_TO_BOTTOM)){
			for (MathObject mObj  : getObjects()){
				mObj.setyPos(getyPos() + getHeight() - mObj.getHeight());
			}
			adjustSizeToFixChildren();
		}
	}
	
	@Override
	public Grouping clone() {
		Grouping o = new Grouping(getParentPage());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		for ( MathObject mObj : getObjects()){
			o.addObjectFromPage(mObj.clone());
		}
		return o;
	}

	@Override
	public String getType() {
		return GROUPING;
	}
	
	public void setName(String s){
		getAttributeWithName(NAME).setValue(s);
	}
	
	public String getName(){
		return ((StringAttribute) getAttributeWithName(NAME)).getValue();
	}
	
	public void setAuthor(String s){
		getAttributeWithName(AUTHOR).setValue(s);
	}
	
	public String getAuthor(){
		return ((StringAttribute) getAttributeWithName(AUTHOR)).getValue();
	}
	
	public void setDate(Date d){
		getAttributeWithName(DATE).setValue(d);
	}
	
	public Date getDate(){
		return ((DateAttribute) getAttributeWithName(DATE)).getValue();
	}
	
	public void setTags(String s){
		getAttributeWithName(TAGS).setValue(s);
	}
	
	public String getTags(){
		return ((StringAttribute) getAttributeWithName(TAGS)).getValue();
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
		Vector<MathObject> temp = getObjects();
		objects = new Vector<MathObject>();
		objectBounds = new Vector<DecimalRectangle>();
		objectBounds.removeAllElements();
		for (MathObject mObj : temp){
			addObjectFromPage(mObj);
		}
		
	}
	
	public boolean addObjectFromPage(MathObject mObj){
		if (this.getParentPage() != mObj.getParentPage())
		{// cannot add object from another page
			return false;
		}
		if (objects.size() == 0){
			setWidth(mObj.getWidth());
			setHeight(mObj.getHeight());
			setxPos(mObj.getxPos());
			setyPos(mObj.getyPos());
			
			//positions of objects within Groups are relative to the group origin
			//and saved in a fraction of the total width/height, instead of number of pixels
			objectBounds.add(new DecimalRectangle(0,0,1,1));
//			getParentPage().shiftObjInFrontOfOther(this, mObj);
			objects.add(mObj);
			getParentPage().removeObject(mObj);
			return true;
		}
		Rectangle objRect = new Rectangle(mObj.getxPos(), mObj.getyPos(), mObj.getWidth(), mObj.getHeight());
		Rectangle groupRect = new Rectangle(getxPos(), getyPos(), getWidth(), getHeight());
		if (groupRect.contains(objRect))
		{//the object fits into the current group rectangle, it must be added and have its position/size adjusted
			//so it is relative to the size of the group
			objectBounds.add(new DecimalRectangle(
					((double) mObj.getxPos() - getxPos())/getWidth(),
					((double) mObj.getyPos() - getyPos())/getHeight(),
					(double)mObj.getWidth()/getWidth(),
					(double)mObj.getHeight()/getHeight() ) );
			objects.add(mObj);
			return true;
		}
		else
		{// the new object is outside of the groups bounds
			
			//need to temporarily store the currently grouped objects
			//to prevent them from being resized with the calls to setWidth
			// which adjusts the size of the child objects as a side effect
			
			//they will be added back after the group is resized to accommodate
			//the new object that was outside of the groups bounds
			Vector<MathObject> oldObjects = objects;
			objects = new Vector<MathObject>();
			objectBounds = new Vector<DecimalRectangle>();
			
			//add additional height or width to the group if the object is outside

			if ( mObj.getxPos() < getxPos()){

				setWidth( getWidth() + getxPos() - mObj.getxPos());
				setxPos(mObj.getxPos());
				
			}
			if (mObj.getxPos() + mObj.getWidth() > getxPos() + getWidth()){
				setWidth( mObj.getxPos() + mObj.getWidth() - getxPos());
			}

			if ( mObj.getyPos() <  getyPos()){
				setHeight( getHeight() +  getyPos() - mObj.getyPos());
				setyPos(mObj.getyPos());
			}
			if (mObj.getyPos() + mObj.getHeight() > getyPos() + getHeight()){
				setHeight( mObj.getyPos() + mObj.getHeight() - getyPos());
			}
			
			for (MathObject mathObj : oldObjects){
				objects.add(mathObj);
				DecimalRectangle temp = new DecimalRectangle(
						((double) mathObj.getxPos() - getxPos())/getWidth(),
						((double) mathObj.getyPos() - getyPos())/getHeight(),
						(double)mathObj.getWidth()/getWidth(),
						(double)mathObj.getHeight()/getHeight() );

				objectBounds.add(temp);
			}
			

			objectBounds.add(new DecimalRectangle(
					((double) mObj.getxPos() - getxPos())/getWidth(),
					((double) mObj.getyPos() - getyPos())/getHeight(),
					(double)mObj.getWidth()/getWidth(),
					(double)mObj.getHeight()/getHeight() ) );
			objects.add(mObj);

			return true;
		}
	}
	
	public void removeObject(MathObject mObj){
		if ( ! objects.contains(mObj)){
			return;
		}
		objectBounds.remove(objects.indexOf(mObj));
		objects.remove(mObj);
		adjustSizeToFixChildren();
	}
	
	public void unGroup(){
		
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
			System.out.println("attribute name: " + mAtt.getName());
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
		objectBounds = new Vector<DecimalRectangle>();
		return true;
	}
	
	/**
	 * This method is sued to move or resize all of the child object in a group after a corresponding
	 * change to the group is made. This is not a method for adjusting the group size after child
	 * objects have been modified and possibly been moved outside of the group.
	 */
	private void adjustChildrenToGroupChange(){
		int index = 0;
		DecimalRectangle tempBounds;
		for (MathObject mathObj : objects){
			tempBounds = objectBounds.get(index);
			mathObj.setxPos(getxPos() + (int) Math.round(tempBounds.getX() * getWidth()) );
			mathObj.setyPos(getyPos() + (int) Math.round(tempBounds.getY() * getHeight()) );
			mathObj.setWidth((int) Math.round(tempBounds.getWidth() * getWidth()) );
			mathObj.setHeight((int) Math.round(tempBounds.getHeight() * getHeight()) );
			index++;
		}
	}
	
	@Override
	public void setWidth(int width) {
		getAttributeWithName(WIDTH).setValue(width);
		adjustChildrenToGroupChange();
	}

	@Override
	public int getWidth() {
		return ((IntegerAttribute)getAttributeWithName(WIDTH)).getValue();
	}

	@Override
	public void setHeight(int height) {
		getAttributeWithName(HEIGHT).setValue(height);
		adjustChildrenToGroupChange();
	}

	@Override
	public int getHeight() {
		return ((IntegerAttribute)getAttributeWithName(HEIGHT)).getValue();
	}

	@Override
	public void setxPos(int xPos) {
		getAttributeWithName(X_POS).setValue(xPos);
		adjustChildrenToGroupChange();
	}

	@Override
	public int getxPos() {
		return ((IntegerAttribute)getAttributeWithName(X_POS)).getValue();
	}

	@Override
	public void setyPos(int yPos) {
		getAttributeWithName(Y_POS).setValue(yPos);
		adjustChildrenToGroupChange();
	}

	@Override
	public int getyPos() {
		return ((IntegerAttribute)getAttributeWithName(Y_POS)).getValue();
	}

	public void setObjectBounds(Vector<DecimalRectangle> objectBounds) {
		this.objectBounds = objectBounds;
	}

	public Vector<DecimalRectangle> getObjectBounds() {
		return objectBounds;
	}
}
