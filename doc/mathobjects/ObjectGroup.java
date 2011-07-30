package doc.mathobjects;

import java.awt.Rectangle;
import java.util.Vector;

import doc.Page;

public class ObjectGroup extends MathObject {
	
	private Vector<MathObject> objects;

	public ObjectGroup(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		objects = new Vector<MathObject>();
		// TODO Auto-generated constructor stub
	}
	
	public ObjectGroup(Page p){
		super(p);
		objects = new Vector<MathObject>();
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

	public void setObjects(Vector<MathObject> objects) {
		this.objects = objects;
	}

	public Vector<MathObject> getObjects() {
		return objects;
	}

	public void addObject(MathObject mObj){
		if (this.getParentPage() != mObj.getParentPage()){
			return;
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
			mObj.setWidth(100);
			mObj.setHeight(100);
			objects.add(mObj);
			return;
		}
		Rectangle objRect = new Rectangle(mObj.getxPos(), mObj.getyPos(), mObj.getWidth(), mObj.getHeight());
		Rectangle groupRect = new Rectangle(getxPos(), getyPos(), getWidth(), getHeight());
		if (groupRect.contains(objRect))
		{//the object fits into the current group rectangle, it must be added and have its position/size adjusted
			//so it is relative to the size of the group
			mObj.setWidth( (int) ((double)mObj.getWidth()/getWidth() * 100) );
			System.out.println(mObj.getWidth() + " width obj");
			mObj.setHeight( (int) ((double)mObj.getHeight()/getHeight() * 100) );
			mObj.setxPos( (int) ( ((double) mObj.getxPos() - getxPos())/getWidth() * 100) );
			System.out.println(mObj.getxPos() + " x pos obj");
			mObj.setyPos( (int) ( ((double) mObj.getyPos() - getyPos())/getHeight() * 100) );
			objects.add(mObj);
			return;
		}
	}
}
