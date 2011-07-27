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
	
	private DocViewerPanel docPanel;
	
	private RectangleObjectGUI rectangleGUI;
	private TextObjectGUI textGUI;
	private OvalObjectGUI ovalGUI;
	private GraphObjectGUI graphGUI;
	private TriangleObjectGUI triangleGUI;
	private HexagonObjectGUI hexagonGUI;
	private PolygonObjectGUI polygonGUI;
	private ExpressionObjectGUI expressionGUI;
	private NumberLineObjectGUI numLineGUI;
	private AnswerBoxGUI answerBoxGUI;
	
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
	
	private Graphics lastGraphics;
	
	/**
	 * Constructor used for painting to the screen.
	 * 
	 * @param docViewerPanel - the viewer panel to be drawn on
	 */
	public PageGUI(DocViewerPanel docViewerPanel){
		this.docPanel = docViewerPanel;
		rectangleGUI = new RectangleObjectGUI();
		textGUI = new TextObjectGUI();
		ovalGUI = new OvalObjectGUI();
		graphGUI = new GraphObjectGUI();
		triangleGUI = new TriangleObjectGUI();
		hexagonGUI = new HexagonObjectGUI();
		polygonGUI = new PolygonObjectGUI();
		expressionGUI = new ExpressionObjectGUI();
		numLineGUI = new NumberLineObjectGUI();
		answerBoxGUI = new AnswerBoxGUI();
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
		answerBoxGUI = new AnswerBoxGUI();
	}
	
	public void handleMouseAction(MathObject mObj, int x, int y, int mouseActionCode){
		if (mObj instanceof RectangleObject){
			
		}
		else if (mObj instanceof TextObject){

		}
		else if (mObj instanceof OvalObject){
	
		}
		else if (mObj instanceof GraphObject){
			graphGUI.mouseClicked((GraphObject)mObj, x, y);
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
	
	
	public Graphics getLastGraphics(){
		return lastGraphics;
	}
	
	public void drawPage(Graphics g, Page p, Point pageOrigin, Rectangle visiblePageSection,
			float zoomLevel){
		
		
		if (lastGraphics != null){
			lastGraphics.dispose();
		}
		
		//used to make calculations involving the display of the page, without having to redraw
		//mostly just need the fonMetrics object
		lastGraphics = g;
		
		
		
		//draw MathObjects, only if they intersect with the available viewport
		
		//translate viewport into an available subsection of printable document
		//used to detect collisions with the rectangles that contain mathObjects
		
		for (MathObject mObj : p.getObjects()){
//			System.out.println(mObj);
			if (mObj instanceof RectangleObject){
				rectangleGUI.drawMathObject((RectangleObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel.getFocusedObject() == mObj){
					rectangleGUI.drawInteractiveComponents((RectangleObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
			else if (mObj instanceof TextObject){
				textGUI.drawMathObject((TextObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel.getFocusedObject() == mObj){
					textGUI.drawInteractiveComponents((TextObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
			else if (mObj instanceof OvalObject){
				ovalGUI.drawMathObject((OvalObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel.getFocusedObject() == mObj){
					ovalGUI.drawInteractiveComponents((OvalObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
			else if (mObj instanceof GraphObject){
				graphGUI.drawMathObject((GraphObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel.getFocusedObject() == mObj){
					graphGUI.drawInteractiveComponents((GraphObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
			else if (mObj instanceof PolygonObject){
				if (mObj instanceof TriangleObject){
					//add call to custom drawer in TrinalgeObjectGUI if extra code is needed to be
					//added to the generic polygon drawing method
					polygonGUI.drawMathObject((PolygonObject)mObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
					if (docPanel.getFocusedObject() == mObj){
						polygonGUI.drawInteractiveComponents((PolygonObject)mObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
					}
				}
				else if (mObj instanceof HexagonObject){
					//add call to custom drawer in HexagonObjectGUI if extra code is needed to be
					//added to the generic polygon drawing method
					polygonGUI.drawMathObject((PolygonObject)mObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
					if (docPanel.getFocusedObject() == mObj){
						polygonGUI.drawInteractiveComponents((PolygonObject)mObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
					}
				}
				else{
					polygonGUI.drawMathObject((PolygonObject)mObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
					if (docPanel.getFocusedObject() == mObj){
						polygonGUI.drawInteractiveComponents((PolygonObject)mObj, g,
							new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
					}
				}
			}
			else if(mObj instanceof ExpressionObject){
				expressionGUI.drawMathObject((ExpressionObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel.getFocusedObject() == mObj){
					expressionGUI.drawInteractiveComponents((ExpressionObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
			else if (mObj instanceof NumberLineObject){
				numLineGUI.drawMathObject((NumberLineObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel.getFocusedObject() == mObj){
					numLineGUI.drawInteractiveComponents((NumberLineObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
			}
			else if (mObj instanceof AnswerBoxObject){
				answerBoxGUI.drawMathObject((AnswerBoxObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				if (docPanel.getFocusedObject() == mObj){
					numLineGUI.drawInteractiveComponents((AnswerBoxObject)mObj, g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
				}
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
			if (docPanel.getFocusedObject().getParentPage() == p)
			{//the focused object is on this page, print the resize dots
				MathObjectGUI.drawResizingDots(docPanel.getFocusedObject(), g,
						new Point((int) pageOrigin.getX(), (int) pageOrigin.getY()), zoomLevel);
			}
		}	
	}
}
