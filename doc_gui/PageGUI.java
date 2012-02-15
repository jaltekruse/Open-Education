/*

 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import doc.Page;
import doc.mathobjects.AnswerBoxObject;
import doc.mathobjects.ConeObject;
import doc.mathobjects.CubeObject;
import doc.mathobjects.CylinderObject;
import doc.mathobjects.ExpressionObject;
import doc.mathobjects.GraphObject;
import doc.mathobjects.Grouping;
import doc.mathobjects.MathObject;
import doc.mathobjects.NumberLineObject;
import doc.mathobjects.OvalObject;
import doc.mathobjects.PolygonObject;
import doc.mathobjects.RectangleObject;
import doc.mathobjects.TextObject;
import doc.mathobjects.TriangleObject;
import doc_gui.mathobject_gui.AnswerBoxGUI;
import doc_gui.mathobject_gui.ConeObjectGUI;
import doc_gui.mathobject_gui.CubeObjectGUI;
import doc_gui.mathobject_gui.CylinderObjectGUI;
import doc_gui.mathobject_gui.ExpressionObjectGUI;
import doc_gui.mathobject_gui.GraphObjectGUI;
import doc_gui.mathobject_gui.MathObjectGUI;
import doc_gui.mathobject_gui.NumberLineObjectGUI;
import doc_gui.mathobject_gui.OvalObjectGUI;
import doc_gui.mathobject_gui.PolygonObjectGUI;
import doc_gui.mathobject_gui.TextObjectGUI;
import doc_gui.mathobject_gui.TriangleObjectGUI;

public class PageGUI {

	private int printableXOrigin, printableYOrigin;

	private DocViewerPanel docPanel;

	public TextObjectGUI textGUI;
	public OvalObjectGUI ovalGUI;
	public GraphObjectGUI graphGUI;
	public TriangleObjectGUI triangleGUI;
	public PolygonObjectGUI polygonGUI;
	public ExpressionObjectGUI expressionGUI;
	public NumberLineObjectGUI numLineGUI;
	public AnswerBoxGUI answerBoxGUI;
	public CubeObjectGUI cubeGUI;
	public CylinderObjectGUI cylinderGUI;
	public ConeObjectGUI coneGUI;

	public static final int MOUSE_LEFT_CLICK = 0;
	public static final int MOUSE_MIDDLE_CLICK = 1;
	public static final int MOUSE_RIGHT_CLICK = 2;
	public static final int MOUSE_LEFT_PRESS = 3;
	public static final int MOUSE_MIDDLE_PRESS = 4;
	public static final int MOUSE_RIGHT_PRESS = 5;
	public static final int MOUSE_RIGHT_RELEASE = 6;
	public static final int MOUSE_MIDDLE_RELEASE = 7;
	public static final int MOUSE_LEFT_RELEASE = 8;
	public static final int MOUSE_MOVED = 9;
	public static final int MOUSE_DRAGGED = 10;
	public static final int MOUSE_ENTERED = 11;
	public static final int MOUSE_EXITED = 12;

	/**
	 * Constructor used for painting to the screen.
	 * 
	 * @param docViewerPanel - the viewer panel to be drawn on
	 */
	public PageGUI(DocViewerPanel docViewerPanel){
		this.docPanel = docViewerPanel;
		textGUI = new TextObjectGUI();
		ovalGUI = new OvalObjectGUI();
		graphGUI = new GraphObjectGUI();
		triangleGUI = new TriangleObjectGUI();
		polygonGUI = new PolygonObjectGUI();
		expressionGUI = new ExpressionObjectGUI();
		numLineGUI = new NumberLineObjectGUI();
		answerBoxGUI = new AnswerBoxGUI();
		cubeGUI = new CubeObjectGUI();
		cylinderGUI = new CylinderObjectGUI();
		coneGUI = new ConeObjectGUI();
	}


	/**
	 * Constructor used for printing to a physical document
	 */
	public PageGUI(){
		textGUI = new TextObjectGUI();
		ovalGUI = new OvalObjectGUI();
		graphGUI = new GraphObjectGUI();
		triangleGUI = new TriangleObjectGUI();
		polygonGUI = new PolygonObjectGUI();
		expressionGUI = new ExpressionObjectGUI();
		numLineGUI = new NumberLineObjectGUI();
		answerBoxGUI = new AnswerBoxGUI();
		cubeGUI = new CubeObjectGUI();
		cylinderGUI = new CylinderObjectGUI();
		coneGUI = new ConeObjectGUI();
	}

	/**
	 * @param mObj
	 * @param x
	 * @param y
	 * @param mouseActionCode
	 */
	public void handleMouseAction(MathObject mObj, int x, int y, int mouseActionCode){
		if (mObj instanceof RectangleObject){

		}
		else if (mObj instanceof TextObject){

		}
		else if (mObj instanceof OvalObject){

		}
		else if (mObj instanceof GraphObject){

			//			graphGUI.mouseClicked((GraphObject)mObj, x, y, docPanel.getZoomLevel());
			//			docPanel.repaintDoc();
		}
		else if (mObj instanceof PolygonObject){
			if (mObj instanceof TriangleObject){

			}
			else{

			}
		}
		else if(mObj instanceof ExpressionObject){

		}
		else if (mObj instanceof NumberLineObject){

		}
		else if (mObj instanceof AnswerBoxObject){

		}
		else{
			//			System.out.println("unreconginzed object (PageGUI.handleMouseAction)");
		}
	}

	public void drawPage(Graphics g, Page p, Point pageOrigin, Rectangle visiblePageSection,
			float zoomLevel){



		//draw MathObjects, only if they intersect with the available viewport

		//translate viewport into an available subsection of printable document
		//used to detect collisions with the rectangles that contain mathObjects

		for (MathObject mObj : p.getObjects()){
			drawObject(mObj, g, p, pageOrigin, visiblePageSection, zoomLevel);
		}

	}

	public void drawObject(MathObject mObj, Graphics g, Page p, Point pageOrigin, Rectangle visiblePageSection,
			float zoomLevel){
		if (mObj instanceof TextObject){
			textGUI.drawMathObject((TextObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				textGUI.drawInteractiveComponents((TextObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof Grouping){
			Grouping group = ((Grouping)mObj);
			for (MathObject mathObj : group.getObjects()){
				drawObject(mathObj, g, p, pageOrigin, visiblePageSection, zoomLevel);
				if (docPanel != null && group == docPanel.getFocusedObject()){
					g.setColor(Color.BLUE);
					((Graphics2D)g).setStroke(new BasicStroke(2));
					g.drawRect((int) (pageOrigin.getX() + mathObj.getxPos() * zoomLevel), 
							(int) (pageOrigin.getY() + mathObj.getyPos() * zoomLevel ),
							(int) ( mathObj.getWidth() * zoomLevel ),
							(int) ( mathObj.getHeight() * zoomLevel ));
					((Graphics2D)g).setStroke(new BasicStroke(1));
				}
			}
		}
		else if (mObj instanceof OvalObject){
			ovalGUI.drawMathObject( (OvalObject) mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				ovalGUI.drawInteractiveComponents( (OvalObject) mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof GraphObject){
			graphGUI.drawMathObject((GraphObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				graphGUI.drawInteractiveComponents( (GraphObject) mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof CubeObject){
			cubeGUI.drawMathObject((CubeObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				cubeGUI.drawInteractiveComponents( (CubeObject) mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof ConeObject){
			coneGUI.drawMathObject((ConeObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
			}
		}
		else if (mObj instanceof PolygonObject){
			if (mObj instanceof TriangleObject){
				//add call to custom drawer in TrinalgeObjectGUI if extra code is needed to be
				//added to the generic polygon drawing method
				polygonGUI.drawMathObject((PolygonObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel != null && docPanel.getFocusedObject() == mObj){
					polygonGUI.drawInteractiveComponents( (PolygonObject)mObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
			else{
				polygonGUI.drawMathObject((PolygonObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel != null && docPanel.getFocusedObject() == mObj){
					polygonGUI.drawInteractiveComponents( (PolygonObject)mObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
		}
		else if(mObj instanceof ExpressionObject){
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				expressionGUI.drawInteractiveComponents((ExpressionObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
			else{
				expressionGUI.drawMathObject((ExpressionObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}

		}
		else if (mObj instanceof NumberLineObject){
			numLineGUI.drawMathObject((NumberLineObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				numLineGUI.drawInteractiveComponents( (NumberLineObject) mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof AnswerBoxObject){
			answerBoxGUI.drawMathObject((AnswerBoxObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				answerBoxGUI.drawInteractiveComponents((AnswerBoxObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof CylinderObject){
			cylinderGUI.drawMathObject((CylinderObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				cylinderGUI.drawInteractiveComponents((CylinderObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else{
			System.out.println("unreconginzed object (printed in PageGUI.drawMathObj)");
		}
	}

	public void drawPageWithDecorations(Graphics g, Page p, Point pageOrigin, Rectangle visiblePageSection,
			float zoomLevel){
		int adjustedWidth = (int) (p.getWidth() * zoomLevel);
		int adjustedHeight = (int) (p.getHeight() * zoomLevel);

		int pageXOrigin = (int) pageOrigin.getX();
		int pageYOrigin = (int) pageOrigin.getY();

		if (docPanel.getSelectedPage() == p){
			g.setColor(new Color(230,240, 255));
		}
		else{
			g.setColor(Color.WHITE);
		}
		g.fillRect(pageXOrigin, pageYOrigin, adjustedWidth, adjustedHeight);

		//draw gray box to show margins
		int adjustedMargin = (int) (p.getxMargin()* zoomLevel);
		printableXOrigin = pageXOrigin + adjustedMargin;
		printableYOrigin = pageYOrigin + adjustedMargin;
		g.setColor(Color.GRAY.brighter());
		g.drawRect(printableXOrigin, printableYOrigin,
				adjustedWidth - 2 * adjustedMargin, adjustedHeight - adjustedMargin * 2);

		//draw shadow and outline of page

		if (docPanel.getSelectedPage() == p){
			g.setColor(Color.BLUE);
		}
		else{
			g.setColor(Color.BLACK);
		}
		g.fillRect(pageXOrigin + 3, pageYOrigin + adjustedHeight, adjustedWidth, 3);
		g.fillRect(pageXOrigin + adjustedWidth, pageYOrigin + 3, 3, adjustedHeight);
		g.drawRect(pageXOrigin, pageYOrigin, adjustedWidth, adjustedHeight);

		drawPage(g, p, pageOrigin, visiblePageSection, zoomLevel);

		if (docPanel.getFocusedObject() != null){
			MathObject focusedObj = docPanel.getFocusedObject();
			if (p.objectContainedBelow(focusedObj))
			{//the focused object is on this page, print the resize dots
				//the temporary rectangle used for selecting a group of objects is
				//created using a RectangleObject, but it is not added to its parent page
				//in that case the dots are not drawn
				if( focusedObj instanceof ExpressionObject){
					expressionGUI.drawInteractiveComponents((ExpressionObject)focusedObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
				if ( (! focusedObj.isHorizontallyResizable() && ! focusedObj.isVerticallyResizable())
						|| docPanel.isInStudentMode())
				{// if the object cannot be resized, or if in student mode (where they cannot resize objects)
					g.setColor(Color.GRAY);
					((Graphics2D)g).setStroke(new BasicStroke(3));
					g.drawRect((int) (pageOrigin.getX() + focusedObj.getxPos() * zoomLevel) - 3, 
							(int) (pageOrigin.getY() + focusedObj.getyPos() * zoomLevel ) - 3,
							(int) ( focusedObj.getWidth() * zoomLevel ) + 6,
							(int) ( focusedObj.getHeight() * zoomLevel ) + 6);
					((Graphics2D)g).setStroke(new BasicStroke(1));
				}
				else{
					MathObjectGUI.drawResizingDots(focusedObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}	
			}
		}	
	}
}
