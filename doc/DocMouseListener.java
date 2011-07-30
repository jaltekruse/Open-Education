/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.event.MouseInputListener;

import doc.mathobjects.MathObject;
import doc.mathobjects.MathObjectGUI;
import doc.mathobjects.ObjectGroup;
import doc.mathobjects.RectangleObject;

public class DocMouseListener implements MouseInputListener{
	
	private DocViewerPanel docPanel;
	
	private boolean draggingDot, draggingObject, placingObject, objPlacementRequiresMouseDrag,
					selectionRectRequiresMouseDrag, selectionRectBeingResized;
	
	private MathObject objToPlace;
	
	private int currentDragDot, xBoxOffset, yBoxOffset;
	
	public DocMouseListener(DocViewerPanel docPanel){
		this.docPanel = docPanel;
		draggingDot = false;
	}
	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("xClick: " + e.getX() + " yClick: " + e.getY());
		boolean requiresRedraw = false;
		boolean clickHandled = false;
		
		if (selectionRectRequiresMouseDrag || selectionRectBeingResized){
			System.out.println("remove select rect when clicked");
			selectionRectRequiresMouseDrag = false;
			selectionRectBeingResized = false;
			docPanel.setSelectionRect(null);
		}
		PointInDocument clickedPt = docPanel.panelPt2DocPt(e.getX(), e.getY());
		
		if ( ! clickedPt.isOutSidePage()){
			try {
				System.out.println("in page");
				
				Rectangle objRect;
				
				if(!clickHandled){
					//gives extra space around object to allow selection,
					//most useful for when objects are very thin
					int clickBuffer = 3;
					Vector<MathObject> currPageObjects =
						docPanel.getDoc().getPage(clickedPt.getPage()).getObjects();
					MathObject mObj;
					for ( int i = (currPageObjects.size() - 1); i >= 0; i--)
					{//cycle through all of the objects on the page that was clicked
						mObj = docPanel.getDoc().getPage(clickedPt.getPage()).getObjects().get(i);
						objRect = new Rectangle(mObj.getxPos() - clickBuffer, mObj.getyPos() - clickBuffer,
								mObj.getWidth() + 2 * clickBuffer, mObj.getHeight() + 2 * clickBuffer);
						if (objRect.contains(new Point(clickedPt.getxPos(), clickedPt.getyPos())) &&
								mObj != docPanel.getFocusedObject())
						{// the click occurred within an object, that was not already selected
							docPanel.setFocusedObject(mObj);
							requiresRedraw = true;
							clickHandled = true;
							break;
						}
					}
				}
				
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		{//click was outside of page
			System.out.println("outside page");
			docPanel.setSelectedPage(null);
			docPanel.repaintDoc();
			return;
		}
		
		//objects can be off of the page, so this check must happen out here
		if (docPanel.getFocusedObject() != null && ! clickHandled){
			System.out.println("check for sending into obj");
			Point objPos = null;
			try {
				objPos = docPanel.getObjectPos(docPanel.getFocusedObject());
			} catch (DocumentException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			Rectangle focusedRect = new Rectangle( objPos.x, objPos.y,
					(int) (docPanel.getFocusedObject().getWidth() * docPanel.getZoomLevel()),
					(int) (docPanel.getFocusedObject().getHeight() * docPanel.getZoomLevel()));
			if (focusedRect.contains(new Point(e.getX(), e.getY()))){
				docPanel.getPageGUI().handleMouseAction(docPanel.getFocusedObject(),
						(int) (e.getX() - objPos.getX()), (int) (e.getY() - objPos.getY()), 0);
				clickHandled = true;
				requiresRedraw = true;
			}
			//throw click down to focused object
		}
		
		if ( ! clickHandled && ! clickedPt.isOutSidePage()){
			//the click hit a page, but missed all of its objects, select the page
			docPanel.setSelectedPage(clickedPt.getPage());
			System.out.println("set selected page");
			clickHandled = true;
			requiresRedraw = true;
		}
		
		if (!clickHandled)
		{// click occurred on a non-active part of screen, unfocus current object
			docPanel.setFocusedObject(null);
			requiresRedraw = true;
		}
		
		if (requiresRedraw){
			docPanel.repaintDoc();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		draggingDot = false;
		System.out.println("pressed");
		try {
		
			if (docPanel.getFocusedObject() != null){
				
				int dot = MathObjectGUI.detectResizeDotCollision(new Point(e.getX(), e.getY()),
						docPanel.getFocusedObject(), docPanel.getPageOrigin(
								docPanel.getFocusedObject().getParentPage()), docPanel.getZoomLevel());
//				System.out.println("dot: " + dot);
				if (dot < Integer.MAX_VALUE){
					draggingDot = true;
					draggingObject = false;
					currentDragDot = dot;
					return;
				}
				else{
					boolean b = MathObjectGUI.detectBorderCollision(new Point(e.getX(), e.getY()),
							docPanel.getFocusedObject(), docPanel.getPageOrigin(
									docPanel.getFocusedObject().getParentPage()), docPanel.getZoomLevel());
//					boolean b = MathObjectGUI.detectObjectCollision(new Point(e.getX(), e.getY()),
//					docPanel.getFocusedObject(), docPanel.getPageOrigin(
//							docPanel.getFocusedObject().getParentPage()), docPanel.getZoomLevel());
					
					if (b){
						draggingObject = true;
						xBoxOffset = (int) ( (e.getX() - docPanel.getPageOrigin(
								docPanel.getFocusedObject().getParentPage()).getX()
								- docPanel.getFocusedObject().getxPos() * docPanel.getZoomLevel()) 
								 /docPanel.getZoomLevel() );
						yBoxOffset = (int) ( (e.getY() - docPanel.getPageOrigin(
								docPanel.getFocusedObject().getParentPage()).getY()
								- docPanel.getFocusedObject().getyPos() * docPanel.getZoomLevel()) 
								 /docPanel.getZoomLevel() );
						return;
					}
				}
			}
			
			PointInDocument clickedPt = docPanel.panelPt2DocPt(e.getX(), e.getY());
			
			if ( ! clickedPt.isOutSidePage()){
				
				if (placingObject){
//					System.out.println("placing obj");
					objToPlace.setxPos(clickedPt.getxPos());
					objToPlace.setyPos(clickedPt.getyPos());
					objToPlace.setWidth(1);
					objToPlace.setHeight(1);
					objToPlace.setParentPage(docPanel.getDoc().getPage(clickedPt.getPage()));
					objToPlace.getParentPage().addMathObject(objToPlace);
					docPanel.repaintDoc();
					objPlacementRequiresMouseDrag = true;
					setPlacingObject(false);
					docPanel.setFocusedObject(objToPlace);
					return;
				}
				else
				{//create a box to select multiple objects
					RectangleObject rect = new RectangleObject(docPanel.getDoc().getPage(clickedPt.getPage()));
					rect.setxPos(clickedPt.getxPos());
					rect.setyPos(clickedPt.getyPos());
					rect.setWidth(1);
					rect.setHeight(1);
					docPanel.setSelectionRect(rect);
					selectionRectRequiresMouseDrag = true;
				}
			}
			else
			{//click was outside of page
				if (placingObject){
					setPlacingObject(false);
				}
			}
			
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
		System.out.println("released");
		
		if (objPlacementRequiresMouseDrag)
		{// the mouse was pressed, but not dragged to set an objects size
			//set a default size
			objToPlace.setWidth(50);
			objToPlace.setHeight(50);
			objPlacementRequiresMouseDrag = false;
		}
		if (selectionRectBeingResized || selectionRectRequiresMouseDrag){
			selectionRectBeingResized = false;
			selectionRectRequiresMouseDrag = false;
			docPanel.setFocusedObject(null);
			docPanel.setSelectionRect(null);
			System.out.println("before draw");
			docPanel.repaintDoc();
			System.out.println("after draw");
		}
		selectionRectRequiresMouseDrag = false;
		selectionRectBeingResized = false;
		System.out.println("before frame update");
		docPanel.updateObjectToolFrame();
		System.out.println("after frame update");
		draggingDot = false;
		draggingObject = false;
		placingObject = false;
		objPlacementRequiresMouseDrag = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouse dragged");
		PointInDocument docPt = docPanel.panelPt2DocPt(e.getX(), e.getY());
		
		if (objPlacementRequiresMouseDrag() || selectionRectRequiresMouseDrag){
			if (selectionRectRequiresMouseDrag)
			{//swap in the selection rectangle, allows reuse of code for the addition of a
				//rectangle to select objects
				objToPlace = docPanel.getSelectionRect();
				docPanel.setFocusedObject(objToPlace);
				selectionRectBeingResized = true;
			}
			if (docPt.isOutSidePage()){
				objToPlace.getParentPage().removeObject(objToPlace);
				docPanel.setFocusedObject(null);
			}
			else{
				boolean isWest = false;
				draggingDot = true;
				if (docPt.getxPos() < objToPlace.getxPos()){
					objToPlace.setWidth(objToPlace.getxPos() - docPt.getxPos());
					objToPlace.setxPos(docPt.getxPos());
					isWest = true;
				}
				else{
					objToPlace.setWidth(docPt.getxPos() - objToPlace.getxPos());
				}
				
				if (docPt.getyPos() < objToPlace.getyPos()){
					objToPlace.setHeight(objToPlace.getyPos() - docPt.getyPos());
					objToPlace.setyPos(docPt.getyPos());
					if (isWest){
						currentDragDot = MathObjectGUI.NORTHWEST_DOT;
					}
					else{
						currentDragDot = MathObjectGUI.NORTHEAST_DOT;
					}
				}
				else{
					objToPlace.setHeight(docPt.getyPos() - objToPlace.getyPos());
					if (isWest){
						currentDragDot = MathObjectGUI.SOUTHWEST_DOT;
					}
					else{
						currentDragDot = MathObjectGUI.SOUTHEAST_DOT;
					}
				}
				if (selectionRectRequiresMouseDrag){
					selectionRectRequiresMouseDrag = false;
					docPanel.setFocusedObject(null);
				}
				setObjPlacementRequiresMouseDrag(false);
				docPanel.repaintDoc();
				return;
			}
		}
		
		if (draggingObject)
		{// A mathobject on the document is being shifted to a new location
//			System.out.println("xOff: " + xBoxOffset + " yOff:  " + yBoxOffset);
			try {
				MathObjectGUI.moveBoxToPoint(new Point (e.getX(), e.getY()),
						docPanel.getFocusedObject(),
						docPanel.getPageOrigin(docPanel.getFocusedObject().getParentPage()),
						docPanel.getZoomLevel(),
						xBoxOffset, yBoxOffset);
				docPanel.repaintDoc();
				return;
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (draggingDot || selectionRectBeingResized)
		{// one of the dots of an object is being moved to make it larger or smaller
			try {
				if (selectionRectBeingResized){
					System.out.println("being resized");
					docPanel.setFocusedObject(docPanel.getSelectionRect());
				}
				
				Page p = docPanel.getFocusedObject().getParentPage();
				if ( ! docPt.isOutSidePage() && p == docPanel.getDoc().getPage(docPt.getPage())){
					MathObjectGUI.moveResizeDot(docPanel.getFocusedObject(), currentDragDot,
							docPt, this);
					if (selectionRectBeingResized)
					{//a special object that does note exist in the document was resized, it is used
						//to select multiple objects at once and create groups
						System.out.println("being resized");
						docPanel.setSelectionRect((RectangleObject)docPanel.getFocusedObject());
						docPanel.setFocusedObject(null);
						Rectangle selectRect = docPanel.getSelectionRect().getBounds();
						ObjectGroup tempGroup = docPanel.getTempGroup();
						tempGroup.setParentPage(docPanel.getSelectionRect().getParentPage());
						Vector<MathObject> pageObjects = docPanel.getSelectionRect().getParentPage().getObjects();
						System.out.println("num objects " + pageObjects.size());
						for (MathObject mObj : pageObjects){
							System.out.println("check obj");
							if (selectRect.intersects(mObj.getBounds()) &&
									! (mObj instanceof ObjectGroup) &&
									! tempGroup.getObjects().contains(mObj)){
								System.out.println("intesects with selection");
								tempGroup.addObject(mObj);
							}
						}
						if (tempGroup.getObjects().size() > 0)
						{//an object was added
							docPanel.getSelectionRect().getParentPage().addMathObject(tempGroup);
						}
						for (MathObject mObj : tempGroup.getObjects()){
							mObj.getParentPage().removeObject(mObj);
						}
						
					}
					docPanel.repaintDoc();
					
					return;
				}
				else{
					Point pageOrigin = docPanel.getPageOrigin(docPanel.getFocusedObject().getParentPage());
					int xMouseRequest = Integer.MAX_VALUE;
					int yMouseRequest = Integer.MAX_VALUE;
					
					if (e.getX() <= pageOrigin.getX())
					{//event was to the left of the document, send an resize request with the edge of page
						//and the events given y position
						xMouseRequest = 0;
					}
					else if ( e.getX() >= pageOrigin.getX() + 
							(int) (p.getPageWidth() * docPanel.getZoomLevel())){
						xMouseRequest = p.getPageWidth();
					}
					
					if (e.getY() <= pageOrigin.getY()){
						yMouseRequest = 0;
					}
					
					else if (e.getY() >= pageOrigin.getY() +
							(int) (p.getPageHeight() * docPanel.getZoomLevel())){
						yMouseRequest = p.getPageHeight();
					}
					
					if (yMouseRequest == Integer.MAX_VALUE){
						yMouseRequest = docPt.getyPos();
					}
					if (xMouseRequest == Integer.MAX_VALUE){
						xMouseRequest = docPt.getxPos();
					}
					MathObjectGUI.moveResizeDot(docPanel.getFocusedObject(), currentDragDot,
							new PointInDocument (1, xMouseRequest, yMouseRequest), this);
					
					if (selectionRectBeingResized)
					{//a special object that does note exist in the document was resized, it is used
						//to select multiple objects at once and create groups
						System.out.println("being resized");
						docPanel.setSelectionRect((RectangleObject)docPanel.getFocusedObject());
						docPanel.setFocusedObject(null);
						Rectangle selectRect = docPanel.getSelectionRect().getBounds();
						ObjectGroup tempGroup = docPanel.getTempGroup();
						tempGroup.setParentPage(docPanel.getSelectionRect().getParentPage());
						Vector<MathObject> pageObjects = docPanel.getSelectionRect().getParentPage().getObjects();
						for (MathObject mObj : pageObjects){
							System.out.println("check obj");
							if (selectRect.intersects(mObj.getBounds()) &&
									! (mObj instanceof ObjectGroup) ){
								System.out.println("intesects with selection");
								tempGroup.addObject(mObj);
							}
						}
						if (tempGroup.getObjects().size() > 0)
						{//an object was added
							docPanel.getSelectionRect().getParentPage().addMathObject(tempGroup);
						}
						for (MathObject mObj : tempGroup.getObjects()){
							mObj.getParentPage().removeObject(mObj);
						}
						
					}
					docPanel.repaintDoc();
				}
						
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public int getCurrentDragDot(){
		return currentDragDot;
	}
	
	public void setCurrentDragDot(int dot){
		currentDragDot = dot;
	}
	public void setPlacingObject(boolean placingObject) {
		this.placingObject = placingObject;
	}
	public boolean isPlacingObject() {
		return placingObject;
	}
	public void setObjToPlace(MathObject objToPlace) {
		this.objToPlace = objToPlace;
	}
	public MathObject getObjToPlace() {
		return objToPlace;
	}
	public void setObjPlacementRequiresMouseDrag(
			boolean objPlacementRequiresMouseMovement) {
		this.objPlacementRequiresMouseDrag = objPlacementRequiresMouseMovement;
	}
	public boolean objPlacementRequiresMouseDrag() {
		return objPlacementRequiresMouseDrag;
	}
}
