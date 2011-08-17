/*

 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;


import doc.mathobjects.*;
import doc_gui.DocViewerPanel;
import doc_gui.mathobject_gui.AnswerBoxGUI;
import doc_gui.mathobject_gui.CubeObjectGUI;
import doc_gui.mathobject_gui.ExpressionObjectGUI;
import doc_gui.mathobject_gui.GraphObjectGUI;
import doc_gui.mathobject_gui.HexagonObjectGUI;
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
	public HexagonObjectGUI hexagonGUI;
	public PolygonObjectGUI polygonGUI;
	public ExpressionObjectGUI expressionGUI;
	public NumberLineObjectGUI numLineGUI;
	public AnswerBoxGUI answerBoxGUI;
	public CubeObjectGUI cubeGUI;
	
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
		hexagonGUI = new HexagonObjectGUI();
		polygonGUI = new PolygonObjectGUI();
		expressionGUI = new ExpressionObjectGUI();
		numLineGUI = new NumberLineObjectGUI();
		answerBoxGUI = new AnswerBoxGUI();
		cubeGUI = new CubeObjectGUI();
	}
	
	
	/**
	 * Constructor used for printing to a physical document
	 */
	public PageGUI(){
		textGUI = new TextObjectGUI();
		ovalGUI = new OvalObjectGUI();
		graphGUI = new GraphObjectGUI();
		triangleGUI = new TriangleObjectGUI();
		hexagonGUI = new HexagonObjectGUI();
		polygonGUI = new PolygonObjectGUI();
		expressionGUI = new ExpressionObjectGUI();
		numLineGUI = new NumberLineObjectGUI();
		answerBoxGUI = new AnswerBoxGUI();
		cubeGUI = new CubeObjectGUI();
	}
	
	public void handleMouseAction(MathObject mObj, int x, int y, int mouseActionCode){
		if (mObj instanceof RectangleObject){
			
		}
		else if (mObj instanceof TextObject){

		}
		else if (mObj instanceof OvalObject){
	
		}
		else if (mObj instanceof GraphObject){
			graphGUI.mouseClicked((GraphObject)mObj, x, y, docPanel.getZoomLevel());
			docPanel.repaintDoc();
		}
		else if (mObj instanceof PolygonObject){
			if (mObj instanceof TriangleObject){
		
			}
			else if (mObj instanceof HexagonObject){
			
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
			System.out.println("unreconginzed object (printed in class PageGUI)");
		}
	}
	
	public void drawPage(Graphics g, Page p, Point pageOrigin, Rectangle visiblePageSection,
			float zoomLevel){
		
		
		
		//draw MathObjects, only if they intersect with the available viewport
		
		//translate viewport into an available subsection of printable document
		//used to detect collisions with the rectangles that contain mathObjects
		
		for (MathObject mObj : p.getObjects()){
			System.out.println("draw " + mObj);
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
				int w = mathObj.getWidth();
				int h = mathObj.getHeight();
				int x = mathObj.getxPos();
				int y = mathObj.getyPos();
				mathObj.setxPos(group.getxPos() + (int) Math.round(mathObj.getxPos()/1000.0 * group.getWidth()) );
				mathObj.setyPos(group.getyPos() + (int) Math.round(mathObj.getyPos()/1000.0 * group.getHeight()) );
				mathObj.setWidth((int) Math.round(mathObj.getWidth()/1000.0 * group.getWidth()) );
				mathObj.setHeight((int) Math.round(mathObj.getHeight()/1000.0 * group.getHeight()) );
				drawObject(mathObj, g, p, pageOrigin, visiblePageSection, zoomLevel);
				if (group == docPanel.getFocusedObject()){
					g.setColor(Color.BLUE);
					((Graphics2D)g).setStroke(new BasicStroke(2));
					g.drawRect((int) (pageOrigin.getX() + mathObj.getxPos() * zoomLevel) - 3, 
							(int) (pageOrigin.getY() + mathObj.getyPos() * zoomLevel ) - 3,
							(int) ( mathObj.getWidth() * zoomLevel ) + 6,
							(int) ( mathObj.getHeight() * zoomLevel ) + 6);
					((Graphics2D)g).setStroke(new BasicStroke(1));
				}
				mathObj.setxPos(x);
				mathObj.setyPos(y);
				mathObj.setWidth(w);
				mathObj.setHeight(h);
			}
		}
		else if (mObj instanceof OvalObject){
			ovalGUI.drawMathObject((OvalObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				ovalGUI.drawInteractiveComponents((OvalObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof GraphObject){
			graphGUI.drawMathObject((GraphObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				graphGUI.drawInteractiveComponents((GraphObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof CubeObject){
			cubeGUI.drawMathObject((CubeObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				cubeGUI.drawInteractiveComponents((CubeObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof PolygonObject){
			if (mObj instanceof TriangleObject){
				//add call to custom drawer in TrinalgeObjectGUI if extra code is needed to be
				//added to the generic polygon drawing method
				polygonGUI.drawMathObject((PolygonObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel != null && docPanel.getFocusedObject() == mObj){
					polygonGUI.drawInteractiveComponents((PolygonObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
			else if (mObj instanceof HexagonObject){
				//add call to custom drawer in HexagonObjectGUI if extra code is needed to be
				//added to the generic polygon drawing method
				polygonGUI.drawMathObject((PolygonObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel != null && docPanel.getFocusedObject() == mObj){
					polygonGUI.drawInteractiveComponents((PolygonObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
			else{
				polygonGUI.drawMathObject((PolygonObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel != null && docPanel.getFocusedObject() == mObj){
					polygonGUI.drawInteractiveComponents((PolygonObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
		}
		else if(mObj instanceof ExpressionObject){
			expressionGUI.drawMathObject((ExpressionObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				expressionGUI.drawInteractiveComponents((ExpressionObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof NumberLineObject){
			numLineGUI.drawMathObject((NumberLineObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				numLineGUI.drawInteractiveComponents((NumberLineObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else if (mObj instanceof AnswerBoxObject){
			answerBoxGUI.drawMathObject((AnswerBoxObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			if (docPanel != null && docPanel.getFocusedObject() == mObj){
				numLineGUI.drawInteractiveComponents((AnswerBoxObject)mObj, g,
					new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}
		else{
			System.out.println("unreconginzed object (printed in class PageGUI)");
		}
	}
	
	public void drawPageWithDecorations(Graphics g, Page p, Point pageOrigin, Rectangle visiblePageSection,
			float zoomLevel){
		int adjustedWidth = (int) (Page.DEFAULT_PAGE_WIDTH * zoomLevel);
		int adjustedHeight = (int) (Page.DEFAULT_PAGE_HEIGHT * zoomLevel);
		
		int pageXOrigin = (int) pageOrigin.getX();
		int pageYOrigin = (int) pageOrigin.getY();
		
		System.out.println("xOrig:" + pageXOrigin + " yOrign:" + pageYOrigin);
		//draw page
		if (docPanel.getSelectedPage() == p){
			g.setColor(new Color(230,240, 255));
		}
		else{
			g.setColor(Color.WHITE);
		}
		g.fillRect(pageXOrigin, pageYOrigin, adjustedWidth, adjustedHeight);
		
		//draw gray box to show margins
		int adjustedMargin = (int) (Page.DEFAULT_MARGIN * zoomLevel);
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
			if (docPanel.getFocusedObject().getParentPage() == p
					&& p.getObjects().contains(docPanel.getFocusedObject()) &&
					! (docPanel.getFocusedObject() instanceof ExpressionObject ) )
			{//the focused object is on this page, print the resize dots
				//the temporary rectangle used for selecting a group of objects is
				//created using a RectangleObject, but it is not added to its parent page
				//in that case the dots are not drawn
				MathObjectGUI.drawResizingDots(docPanel.getFocusedObject(), g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}	
	}
}
