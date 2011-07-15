/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import doc.mathobjects.*;

public class PageGUI {
	
	private int printableXOrigin, printableYOrigin;
	
	private DocViewerPanel docViewerPanel;
	
	private RectangleObjectGUI rectangleGUI;
	
	private TextObjectGUI textGUI;
	
	private OvalObjectGUI ovalGUI;
	
	private GraphObjectGUI graphGUI;
	
	private TriangleObjectGUI triangleGUI;

	private HexagonObjectGUI hexagonGUI;
	
	private PolygonObjectGUI polygonGUI;

	private ExpressionObjectGUI expressionGUI;
	
	private NumberLineObjectGUI numLineGUI;
	/**
	 * Constructor used for painting to the screen.
	 * 
	 * @param docViewerPanel - the viewer panel to be drawn on
	 */
	public PageGUI(DocViewerPanel docViewerPanel){
		this.docViewerPanel = docViewerPanel;
		rectangleGUI = new RectangleObjectGUI();
		textGUI = new TextObjectGUI();
		ovalGUI = new OvalObjectGUI();
		graphGUI = new GraphObjectGUI();
		triangleGUI = new TriangleObjectGUI();
		hexagonGUI = new HexagonObjectGUI();
		polygonGUI = new PolygonObjectGUI();
		expressionGUI = new ExpressionObjectGUI();
		numLineGUI = new NumberLineObjectGUI();
	}
	
	
	/**
	 * Constructor used for printing to a physical document
	 */
	public PageGUI(){
		rectangleGUI = new RectangleObjectGUI();
		textGUI = new TextObjectGUI();
		ovalGUI = new OvalObjectGUI();
		graphGUI = new GraphObjectGUI();
		triangleGUI = new TriangleObjectGUI();
		hexagonGUI = new HexagonObjectGUI();
		polygonGUI = new PolygonObjectGUI();
		expressionGUI = new ExpressionObjectGUI();
		numLineGUI = new NumberLineObjectGUI();
	}
	
	public void drawPage(Graphics g, Page p, Point pageOrigin, Rectangle visiblePageSection,
			float zoomLevel){
		
		//draw MathObjects, only if they intersect with the available viewport
		
		//translate viewport into an available subsection of printable document
		//used to detect collisions with the rectangles that contain mathObjects
		
		for (MathObject mObj : p.getObjects()){
//			System.out.println(mObj);
			if (mObj instanceof RectangleObject){
				rectangleGUI.drawMathObject((RectangleObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
			else if (mObj instanceof TextObject){
				textGUI.drawMathObject((TextObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
			else if (mObj instanceof OvalObject){
				ovalGUI.drawMathObject((OvalObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
			else if (mObj instanceof GraphObject){
				graphGUI.drawMathObject((GraphObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
			else if (mObj instanceof PolygonObject){
				polygonGUI.drawMathObject((PolygonObject)mObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (mObj instanceof TriangleObject){
					//add call to custom drawer in TrinalgeObjectGUI if extra code is needed to be
					//added to the generic polygon drawing method
				}
				else if (mObj instanceof HexagonObject){
					//add call to custom drawer in HexagonObjectGUI if extra code is needed to be
					//added to the generic polygon drawing method
				}
			}
			else if(mObj instanceof ExpressionObject){
				expressionGUI.drawMathObject((ExpressionObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
			else if (mObj instanceof NumberLineObject){
				numLineGUI.drawMathObject((NumberLineObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
			else{
				System.out.println("unreconginzed object (printed in class PageGUI)");
			}
		}

	}
	
	public void drawPageWithDecorations(Graphics g, Page p, Point pageOrigin, Rectangle visiblePageSection,
			float zoomLevel){
		int adjustedWidth = (int) (Page.DEFAULT_PAGE_WIDTH * zoomLevel);
		int adjustedHeight = (int) (Page.DEFAULT_PAGE_HEIGHT * zoomLevel);
		
		int pageXOrigin = (int) pageOrigin.getX();
		int pageYOrigin = (int) pageOrigin.getY();
		
		//draw page
		g.setColor(Color.WHITE);
		g.fillRect(pageXOrigin, pageYOrigin, adjustedWidth, adjustedHeight);
		
		//draw gray box to show margins
		int adjustedMargin = (int) (Page.DEFAULT_MARGIN * zoomLevel);
		printableXOrigin = pageXOrigin + adjustedMargin;
		printableYOrigin = pageYOrigin + adjustedMargin;
		g.setColor(Color.GRAY.brighter());
		g.drawRect(printableXOrigin, printableYOrigin,
				adjustedWidth - 2 * adjustedMargin, adjustedHeight - adjustedMargin * 2);
		
		//draw shadow and outline of page
		g.setColor(Color.BLACK);
		g.fillRect(pageXOrigin + 3, pageYOrigin + adjustedHeight, adjustedWidth, 3);
		g.fillRect(pageXOrigin + adjustedWidth, pageYOrigin + 3, 3, adjustedHeight);
		g.drawRect(pageXOrigin, pageYOrigin, adjustedWidth, adjustedHeight);
		
		drawPage(g, p, pageOrigin, visiblePageSection, zoomLevel);
		
		if (docViewerPanel.getFocusedObject() != null){
			if (docViewerPanel.getFocusedObject().getParentPage() == p)
			{//the focused object is on this page, print the resize dots
				MathObjectGUI.drawResizingDots(docViewerPanel.getFocusedObject(), g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}	
	}
}
