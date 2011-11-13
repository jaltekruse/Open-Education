/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import java.awt.Rectangle;
import java.util.Vector;

import doc.mathobjects.MathObject;

public class Page implements MathObjectContainer{
	
	/**
	 * The standard 8.5 inch page width. Given as an integer, 612, in the standard
	 * user space 72 dpi.
	 */
	public static final int DEFAULT_PAGE_WIDTH = 612;
	
	/**
	 * The standard 11 inch page height. Given as an integer, 792, in the standard
	 * user space 72 dpi.
	 */
	public static final int DEFAULT_PAGE_HEIGHT = 792;
	
	/**
	 * The standard half-inch page margin. Given as an integer, 36, in the standard
	 * user space 72 dpi.
	 */
	public static final int DEFAULT_MARGIN = 36;

	private int orientation;
	public static final int PORTRAIT = 1;
	public static final int LANDSCAPE = 2;
	
	private int xMargin, yMargin, pageWidth, pageHeight;
	
	private String heading;
	
	private Vector<MathObject> objects;
	
	private Document parentDoc;
	
	public Page(Document doc){
		setObjects(new Vector<MathObject>());
		setParentDoc(doc);
		xMargin = DEFAULT_MARGIN;
		yMargin = DEFAULT_MARGIN;
		pageWidth = DEFAULT_PAGE_WIDTH;
		pageHeight = DEFAULT_PAGE_HEIGHT;
	}

	public void setObjects(Vector<MathObject> objects) {
		this.objects = objects;
	}
	
	public void removeObject(MathObject mObj){
		objects.remove(mObj);
	}

	public Vector<MathObject> getObjects() {
		return objects;
	}
	
	/**
	 * Add a MathObject to this page.
	 * @param mObj - object to add
	 * @return true if add was successful, if object did not fit in printable area it is not added
	 */
	public boolean addObject(MathObject mObj){
		
		//check to make sure the object will fit on the page, inside of the margins
		
		Rectangle printablePage = new Rectangle(0, 0, getWidth(),
				getHeight());
		
//		Rectangle objRect = new Rectangle(mObj.getxPos(), mObj.getyPos(), mObj.getWidth(), mObj.getHeight());
//		if (printablePage.contains(objRect)){
//		objects.add(mObj);
//			return true;
//		}
		
		if ( ! objects.contains(mObj)){
			objects.add(mObj);
		}

		return false;
		//throw error? the object would not fit within the printable page with the current position and dimensions
	}
	
	public void bringObjectToFront(MathObject mObj){
		objects.remove(mObj);
		objects.add(mObj);
	}
	
	public void shiftObjInFrontOfOther(MathObject toMove, MathObject toBeBehind){
		objects.remove(toMove);
		objects.add(objects.indexOf(toBeBehind), toMove);
	}
	
	public boolean objInFrontOfOther(MathObject obj1, MathObject obj2){
		if (objects.indexOf(obj1) > objects.indexOf(obj2)){
			return true;
		}
		return false;
	}
	
	public void sendObjectForward(MathObject mObj){
		int index = objects.lastIndexOf(mObj);
		Rectangle objRect = new Rectangle(mObj.getxPos(), mObj.getyPos(), mObj.getWidth(), mObj.getHeight());
		MathObject mObj2;
		Rectangle objRect2;
		for (int i = index + 1; i < objects.size(); i++){
			mObj2 = objects.get(i);
			objRect2 = new Rectangle(mObj2.getxPos(), mObj2.getyPos(), mObj2.getWidth(), mObj2.getHeight());
			if (objRect.intersects(objRect2)){
				objects.remove(mObj);
				objects.add(i, mObj);
				break;
			}
		}
	}
	
	public void sendObjectBackward(MathObject mObj){
		int index = objects.lastIndexOf(mObj);
		Rectangle objRect = new Rectangle(mObj.getxPos(), mObj.getyPos(), mObj.getWidth(), mObj.getHeight());
		MathObject mObj2;
		Rectangle objRect2;
		for (int i = index - 1; i >= 0; i--){
			mObj2 = objects.get(i);
			objRect2 = new Rectangle(mObj2.getxPos(), mObj2.getyPos(), mObj2.getWidth(), mObj2.getHeight());
			if (objRect.intersects(objRect2)){
				objects.remove(mObj);
				objects.add(i, mObj);
				break;
			}
		}
	}
	
	public void bringObjectToBack(MathObject mObj){
		objects.remove(mObj);
		objects.add(0,mObj);
	}
	
	public String exportToXML(){
		//should store page width and height at document level
		//do not need to allow teachers to mix page orientations
		String output = "";
		output += "<Page>\n";
		for (MathObject mObj : objects){
			output += mObj.exportToXML();
		}
		output += "</Page>\n";
		return output;
	}

	public void setWidth(int pageWidth) {
		this.pageWidth = pageWidth;
	}

	public int getWidth() {
		return pageWidth;
	}

	public void setHeight(int pageHeight) {
		this.pageHeight = pageHeight;
	}

	public int getHeight() {
		return pageHeight;
	}

	public void setxMargin(int xMargin) {
		this.xMargin = xMargin;
	}

	public int getxMargin() {
		return xMargin;
	}

	public void setyMargin(int yMargin) {
		this.yMargin = yMargin;
	}

	public int getyMargin() {
		return yMargin;
	}

	public void setParentDoc(Document parentDoc) {
		this.parentDoc = parentDoc;
	}

	public Document getParentDoc() {
		return parentDoc;
	}

	@Override
	public boolean childObjectsFocusable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isResizable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAllObjects() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
