/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.event.MouseInputListener;

import doc.Page;
import doc.PointInDocument;
import doc.mathobjects.MathObject;
import doc.mathobjects.Grouping;
import doc.mathobjects.RectangleObject;
import doc_gui.mathobject_gui.MathObjectGUI;

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
		docPanel.requestFocus();

		boolean clickHandled = false, requiresRedraw = false;
		if (selectionRectRequiresMouseDrag || selectionRectBeingResized){
			selectionRectRequiresMouseDrag = false;
			selectionRectBeingResized = false;
			docPanel.setSelectionRect(null);
		}
		PointInDocument clickedPt = docPanel.panelPt2DocPt(e.getX(), e.getY());

		if ( ! clickedPt.isOutSidePage()){
			Rectangle objRect;

			//gives extra space around object to allow selection,
			//most useful for when objects are very thin
			int clickBuffer = 3;

			Vector<MathObject> currPageObjects =
					docPanel.getDoc().getPage(clickedPt.getPage()).getObjects();
			MathObject mObj, oldFocused;
			oldFocused = docPanel.getFocusedObject();

			for ( int i = (currPageObjects.size() - 1); i >= 0; i--)
			{//cycle through all of the objects on the page that was clicked

				mObj = docPanel.getDoc().getPage(clickedPt.getPage()).getObjects().get(i);
				objRect = new Rectangle(mObj.getxPos() - clickBuffer, mObj.getyPos() - clickBuffer,
						mObj.getWidth() + 2 * clickBuffer, mObj.getHeight() + 2 * clickBuffer);
				if (objRect.contains(new Point(clickedPt.getxPos(), clickedPt.getyPos())) &&
						mObj != docPanel.getFocusedObject())
				{// the click occurred within an object, that was not already selected
					if (mObj instanceof Grouping && docPanel.isInStudentMode()){
						// TODO change this to fit new group structure
						for (MathObject subObj : ((Grouping)mObj).getObjects()){
							objRect = new Rectangle(subObj.getxPos(), subObj.getyPos(),
									subObj.getWidth(),subObj.getHeight());
							if (objRect.contains(new Point(clickedPt.getxPos(), clickedPt.getyPos())) 
									&& mObj != docPanel.getFocusedObject()){
								docPanel.setFocusedObject(subObj);
								docPanel.repaint();
								return;
							}
						}
					}
					else {
						docPanel.setFocusedObject(mObj);
						docPanel.repaintDoc();
						docPanel.updateObjectToolFrame();
						return;
					}
				}
			}

			if (docPanel.getFocusedObject() == oldFocused){
				if (oldFocused != null){
					objRect = new Rectangle(oldFocused.getxPos() - clickBuffer,
							oldFocused.getyPos() - clickBuffer,
							oldFocused.getWidth() + 2 * clickBuffer,
							oldFocused.getHeight() + 2 * clickBuffer);
					if ( ! objRect.contains(new Point(clickedPt.getxPos(), clickedPt.getyPos()))){
						//send event to object
						docPanel.setFocusedObject(null);
						docPanel.repaintDoc();
						return;
					}
				}
			}
		}
		else
		{//click was outside of page
			docPanel.setSelectedPage(null);
			docPanel.setFocusedObject(null);
			docPanel.repaintDoc();
			return;
		}

		//objects can be off of the page, so this check must happen out here
		if (docPanel.getFocusedObject() != null && ! clickHandled){
			Point objPos = null;
			objPos = docPanel.getObjectPos(docPanel.getFocusedObject());
			Rectangle focusedRect = new Rectangle( objPos.x, objPos.y,
					(int) (docPanel.getFocusedObject().getWidth() * docPanel.getZoomLevel()),
					(int) (docPanel.getFocusedObject().getHeight() * docPanel.getZoomLevel()));
			if (focusedRect.contains(new Point(e.getX(), e.getY())))
			{// user clicked on the object that already had focus
				//send an event to the object so it can handle it (for drag and drop, moving graphs, etc.)
				docPanel.getPageGUI().handleMouseAction(docPanel.getFocusedObject(),
						(int) (e.getX() - objPos.getX()), (int) (e.getY() - objPos.getY()),
						PageGUI.MOUSE_LEFT_CLICK);
				clickHandled = true;
				requiresRedraw = true;
			}
			//throw click down to focused object
		}

		if ( ! clickHandled && ! clickedPt.isOutSidePage()){
			//the click hit a page, but missed all of its objects, select the page
			docPanel.setSelectedPage(clickedPt.getPage());
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		docPanel.requestFocus();
		draggingDot = false;


		PointInDocument clickedPt = docPanel.panelPt2DocPt(e.getX(), e.getY());

		if (detectResizeOrMove(e))
		{// the mouse press occurred over a resize dot or the selected object
			return;
		}
		else if (clickedPt.isOutSidePage())
		{//click was outside of page
			if (placingObject){
				setPlacingObject(false);
				docPanel.ungroupTempGroup();
			}
		}
		else
		{// the user clicked on a page, but none of the objcts were contacted
			if (placingObject){
				objToPlace.setxPos(clickedPt.getxPos());
				objToPlace.setyPos(clickedPt.getyPos());
				objToPlace.setWidth(1);
				objToPlace.setHeight(1);
				objToPlace.setParentContainer(docPanel.getDoc().getPage(clickedPt.getPage()));
				objToPlace.getParentContainer().addObject(objToPlace);
				docPanel.repaintDoc();
				docPanel.updateObjectToolFrame();
				objPlacementRequiresMouseDrag = true;
				setPlacingObject(false);
				docPanel.setFocusedObject(objToPlace);
				return;
			}
			else
			{//create a box to select multiple objects
				if ( ! docPanel.isInStudentMode() ){
					docPanel.ungroupTempGroup();
					RectangleObject rect = new RectangleObject(docPanel.getDoc().getPage(clickedPt.getPage()));
					rect.setxPos(clickedPt.getxPos());
					rect.setyPos(clickedPt.getyPos());
					rect.setWidth(1);
					rect.setHeight(1);
					docPanel.setSelectionRect(rect);
					selectionRectRequiresMouseDrag = true;
				}
			}
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
		if (selectionRectBeingResized || selectionRectRequiresMouseDrag){
			selectionRectBeingResized = false;
			selectionRectRequiresMouseDrag = false;
			docPanel.setSelectionRect(null);
			docPanel.repaintDoc();
		}
		if ( draggingDot ){
			// allows for the text field to gain focus after the resize of an object
			docPanel.setFocusedObject(docPanel.getFocusedObject());
		}
		selectionRectRequiresMouseDrag = false;
		selectionRectBeingResized = false;
		//		docPanel.updateObjectToolFrame();
		draggingDot = false;
		draggingObject = false;
		placingObject = false;
		objPlacementRequiresMouseDrag = false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		docPanel.requestFocus();
		PointInDocument docPt = docPanel.panelPt2DocPt(e.getX(), e.getY());

		if (dragMouseToPlaceObj(e)){
			return;
		}
		else if (draggingObject)
		{// A mathobject on the document is being shifted to a new location

			Page contactedPage = docPanel.getDoc().getPage(
					docPanel.panelPt2DocPt(e.getX(), e.getY()).getPage() ); 
			if ( contactedPage != docPanel.getFocusedObject().getParentContainer() )
			{ // the mouse moved off of the page the object was on previously
				docPanel.getFocusedObject().getParentContainer().
				removeObject(docPanel.getFocusedObject());
				contactedPage.addObject(docPanel.getFocusedObject());
				docPanel.getFocusedObject().setParentContainer(contactedPage);
			}
			MathObjectGUI.moveBoxToPoint(new Point (e.getX(), e.getY()),
					docPanel.getFocusedObject(),
					docPanel.getPageOrigin(docPanel.getFocusedObject().getParentPage()),
					docPanel.getZoomLevel(),
					xBoxOffset, yBoxOffset);
			docPanel.repaintDoc();
			return;

		}
		else if (dragMouseToResizeObj(e)){
			return;
		}
	}

	/**
	 * Check a mouse event to see what type it is, and assign it a code
	 * from the list of mouse event codes in PagieGUI.
	 * 
	 * @return - the code to best describe the MouseEvent Passed
	 */
	public int getMouseCode(MouseEvent e){
		// TODO implement this
		return 0;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private boolean detectResizeOrMove(MouseEvent e){
		if (docPanel.getFocusedObject() == null)
		{// the document mouse is not in a state in which this method is needed
			return false;
		}

		int dot = MathObjectGUI.detectResizeDotCollision(new Point(e.getX(), e.getY()),
				docPanel.getFocusedObject(), docPanel.getPageOrigin(
						docPanel.getFocusedObject().getParentPage()), docPanel.getZoomLevel());
		//				System.out.println("dot: " + dot);
		if (dot < Integer.MAX_VALUE  && ! docPanel.isInStudentMode())
		{// the user clicked on one of the dots for resizing an object
			draggingDot = true;
			draggingObject = false;
			currentDragDot = dot;
			return true;
		}
		else
		{// a resize dot was note contacted
			boolean clickedSelectedObj;
			// these three lines change the functionality to require contact with the object's boarder
			// instead of anywhere on it, will likely work better when I start forwarding mouse events
			// for individual objects to handle. Or will have to flip between the two depending
			// on the state of the selected object
			//				clickedSelectedObj = MathObjectGUI.detectBorderCollision(new Point(e.getX(), e.getY()),
			//						docPanel.getFocusedObject(), docPanel.getPageOrigin(
			//						docPanel.getFocusedObject().getParentPage()), docPanel.getZoomLevel());
			clickedSelectedObj = MathObjectGUI.detectObjectCollision(new Point(e.getX(), e.getY()),
					docPanel.getFocusedObject(), docPanel.getPageOrigin(
							docPanel.getFocusedObject().getParentPage()), docPanel.getZoomLevel());

			if (clickedSelectedObj && ! docPanel.isInStudentMode()){
				draggingObject = true;
				xBoxOffset = (int) ( (e.getX() - docPanel.getPageOrigin(
						docPanel.getFocusedObject().getParentPage()).getX()
						- docPanel.getFocusedObject().getxPos() * docPanel.getZoomLevel()) 
						/docPanel.getZoomLevel() );
				yBoxOffset = (int) ( (e.getY() - docPanel.getPageOrigin(
						docPanel.getFocusedObject().getParentPage()).getY()
						- docPanel.getFocusedObject().getyPos() * docPanel.getZoomLevel()) 
						/docPanel.getZoomLevel() );
				return true;
			}
		}
		return false;
	}

	private boolean dragMouseToPlaceObj(MouseEvent e){
		if ( ! (objPlacementRequiresMouseDrag || selectionRectRequiresMouseDrag) )
		{// the document mouse is not in a state in which this method is needed
			return false;
		}
		PointInDocument docPt = docPanel.panelPt2DocPt(e.getX(), e.getY());
		if (selectionRectRequiresMouseDrag)
		{//swap in the selection rectangle, allows reuse of code for the addition of a
			//rectangle to select objects
			objToPlace = docPanel.getSelectionRect();
			selectionRectBeingResized = true;
			selectionRectRequiresMouseDrag = false;
		}
		if (docPt.isOutSidePage()){
			objToPlace.getParentContainer().removeObject(objToPlace);
			docPanel.setFocusedObject(null);
		}
		else{
			boolean isWest = false;
			if ( ! selectionRectBeingResized )
			{ // if the object being re-sized is not the selection rectangle
				draggingDot = true;
			}
			if (docPt.getxPos() < objToPlace.getxPos()){
				objToPlace.setWidth(objToPlace.getxPos() - docPt.getxPos());
				objToPlace.setxPos(docPt.getxPos());
				isWest = true;
			}
			else{
				objToPlace.setWidth(docPt.getxPos() - objToPlace.getxPos());
			}
			if ( objToPlace.isHorizontallyResizable() && 
					! objToPlace.isVerticallyResizable()){
				objToPlace.setHeight(15);
				if ( isWest ){
					currentDragDot = MathObjectGUI.WEST_DOT;
				}
				else{
					currentDragDot = MathObjectGUI.EAST_DOT;
				}
			}
			else if ( ! objToPlace.isHorizontallyResizable() && 
					objToPlace.isVerticallyResizable()){
				if (docPt.getyPos() < objToPlace.getyPos()){
					objToPlace.setyPos(docPt.getyPos());
					currentDragDot = MathObjectGUI.NORTH_DOT;
				}
				else{
					currentDragDot = MathObjectGUI.SOUTH_DOT;
				}
			}
			else if (docPt.getyPos() < objToPlace.getyPos()){
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
				selectionRectBeingResized = true;
			}
			setObjPlacementRequiresMouseDrag(false);
			docPanel.repaintDoc();
		}
		return true;
	}

	private boolean dragMouseToResizeObj(MouseEvent e){
		if ( ! (draggingDot || selectionRectBeingResized) )
		{// the document mouse is not in a state in which this method is needed
			return false;
		}
		// one of the dots of an object is being moved to make it larger or smaller
		// or the temporary selection rectangle is being resized
		PointInDocument docPt = docPanel.panelPt2DocPt(e.getX(), e.getY());
		MathObject objToResize = null;
		if (draggingDot)
		{// an object is focused and one of its resizing dots has been contacted
			//resize the focues object
			objToResize = docPanel.getFocusedObject();
		}
		else if ( selectionRectBeingResized)
		{// the user drew a selection rectangle on the screen, resize the selection rectangle
			objToResize = docPanel.getSelectionRect();
		}

		Page pageOfResizeObject = objToResize.getParentPage();
		Page contactedPage = null;
		contactedPage = docPanel.getDoc().getPage(docPt.getPage());

		if ( ! docPt.isOutSidePage() && pageOfResizeObject == contactedPage){

			if (selectionRectBeingResized)
			{//a special object that does note exist in the document was resized, it is used
				//to select multiple objects at once and create groups

				MathObjectGUI.moveResizeDot(docPanel.getSelectionRect(), currentDragDot,
						docPt, this);
				adjustTempGroupSelection();
			}
			else{// a regular object was resized
				MathObjectGUI.moveResizeDot(objToResize, currentDragDot,
						docPt, this);
			}
			docPanel.repaintDoc();
			return true;
		}
		else
		{// the mouse moved outside of the page where the object to resize resides
			Point pageOrigin = null;
			pageOrigin = docPanel.getPageOrigin(objToResize.getParentPage());
			//flags to indicate that a position has not been assigned to the click
			int xMouseRequest = Integer.MAX_VALUE;
			int yMouseRequest = Integer.MAX_VALUE;

			if (e.getX() <= pageOrigin.getX())
			{//event was to the left of the document, send an resize request with the edge of page
				//and the events given y position
				xMouseRequest = 0;
			}
			else if ( e.getX() >= pageOrigin.getX() + 
					(int) (pageOfResizeObject.getWidth() * docPanel.getZoomLevel())){
				xMouseRequest = pageOfResizeObject.getWidth();
			}

			if (e.getY() <= pageOrigin.getY()){
				yMouseRequest = 0;
			}

			else if (e.getY() >= pageOrigin.getY() +
					(int) (pageOfResizeObject.getHeight() * docPanel.getZoomLevel())){
				yMouseRequest = pageOfResizeObject.getHeight();
			}

			if (yMouseRequest == Integer.MAX_VALUE){
				yMouseRequest = docPt.getyPos();
			}
			if (xMouseRequest == Integer.MAX_VALUE){
				xMouseRequest = docPt.getxPos();
			}
			MathObjectGUI.moveResizeDot(objToResize, currentDragDot,
					new PointInDocument (1, xMouseRequest, yMouseRequest), this);

			if (selectionRectBeingResized)
			{//a special object that does note exist in the document was resized, it is used
				//to select multiple objects at once and create groups

				adjustTempGroupSelection();

			}
			docPanel.repaintDoc();
		}
		return true;
	}

	private void adjustTempGroupSelection(){
		//need to modify this code to take into account the current stacking of the objects
		//large objects automatically cover up smaller ones using this system
		Rectangle selectRect = docPanel.getSelectionRect().getBounds();
		Grouping tempGroup = docPanel.getTempGroup();
		Vector<MathObject> pageObjects = docPanel.getSelectionRect().getParentPage().getObjects();

		//temporary storage of objects that collide with the selection rectangle
		Vector<MathObject> collisionObjects = new Vector<MathObject>();

		for (MathObject mObj : pageObjects)
		{// find all of the bojects that were contated by the selection rectangle
			if (selectRect.intersects(mObj.getBounds()))
			{
				collisionObjects.add(mObj);
			}
		}

		if (collisionObjects.size() == 0)
		{// no objects were contacted
			docPanel.ungroupTempGroup();
			docPanel.setFocusedObject(null);
			return;
		}

		if (collisionObjects.size() == 1)
		{// only one object was contacted
			MathObject contactedObj = collisionObjects.get(0);
			if (contactedObj.equals(docPanel.getFocusedObject()))
			{//collision occurred with a single object already selected
				;//do nothing
				if (contactedObj != tempGroup){
					collisionObjects.remove(contactedObj);
					docPanel.repaint();
					return;
				}
			}
			else{
				docPanel.setFocusedObject(contactedObj);
				collisionObjects.remove(contactedObj);
				return;
			}
		}

		if (collisionObjects.contains(tempGroup)){
			MathObject mObj = null;
			//need to make sure all of the objects in the group were contacted, otherwise remove them
			for ( int i = 0; i < tempGroup.getObjects().size(); i++ ){
				mObj = tempGroup.getObjects().get(i);
				if ( ! selectRect.intersects(mObj.getBounds()) ){
					tempGroup.removeObject(mObj);
					tempGroup.getParentContainer().addObject(mObj);
					i--;
				}
			}

			// remove the temporary group from the contacted list, it now contains only elements that were
			// in it before and were contacted by the current selection rectangle
			collisionObjects.remove(tempGroup);
		}

		//objects were selected, that have not been added to the temp group yet

		if (collisionObjects.size() > 0){

			tempGroup.setParentContainer(collisionObjects.get(0).getParentContainer());
			collisionObjects.get(0).getParentContainer().addObject(tempGroup);
			for ( MathObject mObj : collisionObjects){
				tempGroup.addObjectFromPage(mObj);
				mObj.getParentContainer().removeObject(mObj);
			}



			docPanel.setFocusedObject(tempGroup);
		}

		if (tempGroup.getParentContainer() != null){
			if (tempGroup.getObjects().size() == 1)
			{// there is one one object left in the temporary group
				docPanel.setFocusedObject(tempGroup.getObjects().get(0));
				docPanel.ungroupTempGroup();
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