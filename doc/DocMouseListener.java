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

public class DocMouseListener implements MouseInputListener{
	
	private DocViewerPanel docPanel;
	
	private boolean draggingDot, draggingObject, placingObject, objPlacementRequiresMouseDrag;
	
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
				{//check for focusing an object that was clicked
					
				}
			}
			else
			{//click was outside of page
				if (placingObject){
					setPlacingObject(false);
				}
			}
			
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
						}
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
		
		if (objPlacementRequiresMouseDrag)
		{// the mouse was pressed, but not dragged to set an objects size
			//set a default size
			objToPlace.setWidth(50);
			objToPlace.setHeight(50);
			objPlacementRequiresMouseDrag = false;
		}
		docPanel.updateObjectToolFrame();
		draggingDot = false;
		draggingObject = false;
		placingObject = false;
		objPlacementRequiresMouseDrag = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		PointInDocument docPt = docPanel.panelPt2DocPt(e.getX(), e.getY());
		
		if (objPlacementRequiresMouseDrag()){
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
				setObjPlacementRequiresMouseDrag(false);
				docPanel.repaintDoc();
				return;
			}
		}
		
		if (draggingObject){
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
		if (draggingDot){
			try {
				if ( ! docPt.isOutSidePage() && docPanel.getFocusedObject().getParentPage()
						== docPanel.getDoc().getPage(docPt.getPage())){
					MathObjectGUI.moveResizeDot(docPanel.getFocusedObject(), currentDragDot,
							docPt, this);
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
							(int) (Page.DEFAULT_PAGE_WIDTH * docPanel.getZoomLevel())){
						xMouseRequest = Page.DEFAULT_PAGE_WIDTH;
					}
					
					if (e.getY() <= pageOrigin.getY()){
						yMouseRequest = 0;
					}
					
					else if (e.getY() >= pageOrigin.getY() +
							(int) (Page.DEFAULT_PAGE_HEIGHT * docPanel.getZoomLevel())){
						yMouseRequest = Page.DEFAULT_PAGE_HEIGHT;
					}
					
					if (yMouseRequest == Integer.MAX_VALUE){
						yMouseRequest = docPt.getyPos();
					}
					if (xMouseRequest == Integer.MAX_VALUE){
						xMouseRequest = docPt.getxPos();
					}
					MathObjectGUI.moveResizeDot(docPanel.getFocusedObject(), currentDragDot,
							new PointInDocument (1, xMouseRequest, yMouseRequest), this);
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
