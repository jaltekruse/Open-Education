/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui.mathobject_gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;


import doc.Page;
import doc.PointInDocument;
import doc.mathobjects.MathObject;
import doc_gui.DocMouseListener;

public abstract class MathObjectGUI<K extends MathObject>{
	
	private static int resizeDotDiameter = 10;
	
	private static int resizeDotRadius = 5;
	
	private int currResizeDot;
	
	private boolean resizeDotBeingDragged;
	
	/**
	 * Represents the value of the upper left resizing dot.
	 */
	public static final int NORTHWEST_DOT = 0;
	public static final int NORTH_DOT = 1;
	public static final int NORTHEAST_DOT = 2;
	public static final int EAST_DOT = 3;
	public static final int SOUTHEAST_DOT = 4;
	public static final int SOUTH_DOT = 5;
	public static final int SOUTHWEST_DOT = 6;
	public static final int WEST_DOT = 7;
	
	public abstract void drawMathObject(K object, Graphics g, Point pageOrigin, float zoomLevel);
	
	public void drawInteractiveComponents(K object, Graphics g, Point pageOrigin, float zoomLevel){}
	
	public static void drawResizingDots(MathObject object, Graphics g, Point pageOrigin, float zoomLevel){
		
		if ( object.isVerticallyResizable() && object.isHorizontallyResizable()){
			for (int i = NORTHWEST_DOT; i <= WEST_DOT; i++){
				drawResizeDot(object, g, pageOrigin, zoomLevel, i);
			}
			return;
		}
		if ( object.isVerticallyResizable()){
			drawResizeDot(object, g, pageOrigin, zoomLevel, NORTH_DOT);
			drawResizeDot(object, g, pageOrigin, zoomLevel, SOUTH_DOT);
			return;
		}
		if ( object.isHorizontallyResizable()){
			drawResizeDot(object, g, pageOrigin, zoomLevel, WEST_DOT);
			drawResizeDot(object, g, pageOrigin, zoomLevel, EAST_DOT);
			return;
		}

	}
	
	/**
	 * Draws a single white circle (dot) used to show a user that they can click and drag
	 * to resize an object.
	 * 
	 * @param object - the object able to be resized
	 * @param g - the graphics object to draw on
	 * @param pageOrigin - the point on the graphics object where the objects parent page
	 * 				
	 * @param zoomLevel
	 * @param dot
	 */
	private static void drawResizeDot(MathObject object, Graphics g, Point pageOrigin, float zoomLevel, int dot){
		Point p;
		p = getPosResizeDot(object, pageOrigin, zoomLevel, dot);
		g.setColor(Color.WHITE);
		g.fillOval( (int) p.getX(), (int) p.getY(), resizeDotDiameter, resizeDotDiameter);
		g.setColor(Color.BLACK);
		g.drawOval((int) p.getX(), (int) p.getY(), resizeDotDiameter, resizeDotDiameter);
	}
	
	public static Point getPosResizeDot(MathObject object, Point pageOrigin, float zoomLevel, int dotVal){
		int xOrigin = (int) (pageOrigin.getX() + object.getxPos() * zoomLevel);
		int yOrigin = (int) (pageOrigin.getY() + object.getyPos() * zoomLevel);
		int width = (int) (object.getWidth() * zoomLevel);
		int height = (int) (object.getHeight() * zoomLevel);
		
		if (dotVal == NORTHWEST_DOT){
			return new Point(xOrigin - resizeDotDiameter, yOrigin - resizeDotDiameter);
		}
		else if (dotVal == NORTH_DOT){
			return new Point(xOrigin + width/2 - resizeDotRadius, yOrigin - resizeDotDiameter);
		}
		else if (dotVal == NORTHEAST_DOT){
			return new Point(xOrigin + width, yOrigin - resizeDotDiameter);
		}
		else if (dotVal == EAST_DOT){
			return new Point(xOrigin + width, yOrigin  + height/2 - resizeDotRadius);
		}
		else if (dotVal == SOUTHEAST_DOT){
			return new Point(xOrigin + width, yOrigin + height);
		}
		else if (dotVal == SOUTH_DOT){
			return new Point(xOrigin + width/2 - resizeDotRadius, yOrigin  + height);
		}
		else if (dotVal == SOUTHWEST_DOT){
			return new Point(xOrigin - resizeDotDiameter, yOrigin + height );
		}
		else if (dotVal == WEST_DOT){
			return new Point(xOrigin - resizeDotDiameter, yOrigin + height/2 - resizeDotRadius);
		}
		else{
			System.out.println("Invalid resize dot value");
			return null;
		}
		
	}
	
	public static Color brightenColor(Color c){
		if ( c.equals(Color.WHITE)){
			return c;
		}
		else{
			int newRed = (int ) (c.getRed() * 1.4);
			if (newRed > 255){
				newRed = 255;
			}
			int newGreen = (int ) (c.getGreen() * 1.4);
			if (newGreen > 255){
				newGreen = 255;
			}
			int newBlue = (int ) (c.getBlue() * 1.4);
			if (newBlue > 255){
				newBlue = 255;
			}
			return new Color(newRed, newGreen, newBlue);
		}
	}
	
	public static int detectResizeDotCollision(Point clickPos, MathObject object,
			Point pageOrigin, float zoomLevel){
		int extraBuffer = 1;
		Point p;
		
		if ( object.isVerticallyResizable() && object.isHorizontallyResizable()){
			for (int i = NORTHWEST_DOT; i <= WEST_DOT; i++){
				p = getPosResizeDot(object, pageOrigin, zoomLevel, i);
				if ( Math.sqrt( Math.pow( (p.getX() + resizeDotRadius - clickPos.getX() ), 2) +
						Math.pow( (p.getY() + resizeDotRadius - clickPos.getY() ), 2))
						<= resizeDotRadius + extraBuffer)
				{//calculate the distance from the click to one of the dots
					return i;
				}
			}
		}
		if ( object.isVerticallyResizable()){
			p = getPosResizeDot(object, pageOrigin, zoomLevel, NORTH_DOT);
			if ( Math.sqrt( Math.pow( (p.getX() + resizeDotRadius - clickPos.getX() ), 2) +
					Math.pow( (p.getY() + resizeDotRadius - clickPos.getY() ), 2))
					<= resizeDotRadius + extraBuffer)
			{//calculate the distance from the click to one of the dots
				return NORTH_DOT;
			}
			p = getPosResizeDot(object, pageOrigin, zoomLevel, SOUTH_DOT);
			if ( Math.sqrt( Math.pow( (p.getX() + resizeDotRadius - clickPos.getX() ), 2) +
					Math.pow( (p.getY() + resizeDotRadius - clickPos.getY() ), 2))
					<= resizeDotRadius + extraBuffer)
			{//calculate the distance from the click to one of the dots
				return SOUTH_DOT;
			}
		}
		if ( object.isHorizontallyResizable()){
			p = getPosResizeDot(object, pageOrigin, zoomLevel, WEST_DOT);
			if ( Math.sqrt( Math.pow( (p.getX() + resizeDotRadius - clickPos.getX() ), 2) +
					Math.pow( (p.getY() + resizeDotRadius - clickPos.getY() ), 2))
					<= resizeDotRadius + extraBuffer)
			{//calculate the distance from the click to one of the dots
				return WEST_DOT;
			}
			p = getPosResizeDot(object, pageOrigin, zoomLevel, EAST_DOT);
			if ( Math.sqrt( Math.pow( (p.getX() + resizeDotRadius - clickPos.getX() ), 2) +
					Math.pow( (p.getY() + resizeDotRadius - clickPos.getY() ), 2))
					<= resizeDotRadius + extraBuffer)
			{//calculate the distance from the click to one of the dots
				return EAST_DOT;
			}
		}
		

		return Integer.MAX_VALUE;
	}
	
	public static boolean detectObjectCollision(Point clickPos, MathObject object,
			Point pageOrigin, float zoomLevel){
		//(x0,y0)----(x1,y0)
		//  |           |
		//  |           |
		//(x0,y1)----(x1,y1)
		int x0 = (int) (object.getxPos() * zoomLevel + pageOrigin.getX());
		int x1 = (int) ((object.getxPos() + object.getWidth()) * zoomLevel + pageOrigin.getX());
		int y0 = (int) (object.getyPos() * zoomLevel + pageOrigin.getY());
		int y1 = (int) ((object.getyPos() + object.getHeight()) * zoomLevel + pageOrigin.getY());
		int x =  (int) clickPos.getX();
		int y = (int) clickPos.getY();
		
		Rectangle bigRect = new Rectangle(x0 - resizeDotDiameter, y0 - resizeDotDiameter,
				x1 - x0 + 2 * resizeDotDiameter, y1 - y0 + 2 * resizeDotDiameter);
		
		if (bigRect.contains(x,y)){
			return true;
		}
		return false;
	}
	
	public static boolean detectBorderCollision(Point clickPos, MathObject object,
			Point pageOrigin, float zoomLevel){
		//(x0,y0)----(x1,y0)
		//  |           |
		//  |           |
		//(x0,y1)----(x1,y1)
		int x0 = (int) (object.getxPos() * zoomLevel + pageOrigin.getX());
		int x1 = (int) ((object.getxPos() + object.getWidth()) * zoomLevel + pageOrigin.getX());
		int y0 = (int) (object.getyPos() * zoomLevel + pageOrigin.getY());
		int y1 = (int) ((object.getyPos() + object.getHeight()) * zoomLevel + pageOrigin.getY());
		int x =  (int) clickPos.getX();
		int y = (int) clickPos.getY();
		
		Rectangle smallRect = new Rectangle(x0 , y0 ,
				x1 - x0 , y1 - y0 - 2 );
		
		Rectangle bigRect = new Rectangle(x0 - resizeDotDiameter, y0 - resizeDotDiameter,
				x1 - x0 + 2 * resizeDotDiameter, y1 - y0 + 2 * resizeDotDiameter);
		
		if (bigRect.contains(x,y) && ! smallRect.contains(x,y)){
			return true;
		}
		return false;
	}
	
	public static void moveBoxToPoint(Point clickPos, MathObject object,
			Point pageOrigin, float zoomLevel, int xBoxOffset, int yBoxOffset){
		object.setxPos((int) ( (clickPos.getX() - pageOrigin.getX() - xBoxOffset * zoomLevel) / zoomLevel ));
		object.setyPos((int) ( (clickPos.getY() - pageOrigin.getY() - yBoxOffset * zoomLevel) / zoomLevel ));
		if (object.getxPos() + object.getWidth() > Page.DEFAULT_PAGE_WIDTH){
			object.setxPos(Page.DEFAULT_PAGE_WIDTH - object.getWidth());
		}
		else if (object.getxPos() < 0){
			object.setxPos( 0 );
		}
		if (object.getyPos() < 0){
			object.setyPos( 0 );
		}
		else if (object.getyPos() + object.getHeight() > Page.DEFAULT_PAGE_HEIGHT){
			object.setyPos(Page.DEFAULT_PAGE_HEIGHT - object.getHeight());
		}
	}
	
	/**
	 * @param dotVal - the code for one of the 8 dots
	 * @param docPt - a point on the page in the user space
	 */
	public static void moveResizeDot(MathObject object, int dotVal,
			PointInDocument docPt, DocMouseListener docMouseListener){
		int verticalMovement, horizontalMovement;
		
		if (dotVal == SOUTH_DOT){
			verticalMovement = ( docPt.getyPos() - object.getyPos() - object.getHeight());
			object.setHeight(object.getHeight() + verticalMovement);
		}
		else if (dotVal == NORTH_DOT){
			verticalMovement = ( docPt.getyPos() - object.getyPos());
			object.setHeight(object.getHeight() - verticalMovement);
			object.setyPos(object.getyPos() + verticalMovement);
		}
		else if (dotVal == WEST_DOT){
			horizontalMovement = ( docPt.getxPos() - object.getxPos());
			object.setWidth(object.getWidth() - horizontalMovement);
			object.setxPos(object.getxPos() + horizontalMovement);
		}
		else if (dotVal == EAST_DOT){
			horizontalMovement = ( docPt.getxPos() - object.getxPos() - object.getWidth());
			object.setWidth(object.getWidth() + horizontalMovement);
		}
		else if (dotVal == NORTHEAST_DOT){
			verticalMovement = ( docPt.getyPos() - object.getyPos());
			object.setHeight(object.getHeight() - verticalMovement);
			object.setyPos(object.getyPos() + verticalMovement);
			
			horizontalMovement = ( docPt.getxPos() - object.getxPos() - object.getWidth());
			object.setWidth(object.getWidth() + horizontalMovement);	
		}
		else if (dotVal == SOUTHEAST_DOT){
			verticalMovement = ( docPt.getyPos() - object.getyPos() - object.getHeight());
			object.setHeight(object.getHeight() + verticalMovement);
			
			horizontalMovement = ( docPt.getxPos() - object.getxPos() - object.getWidth());
			object.setWidth(object.getWidth() + horizontalMovement);
		}
		else if (dotVal == SOUTHWEST_DOT){
			horizontalMovement = ( docPt.getxPos() - object.getxPos());
			object.setWidth(object.getWidth() - horizontalMovement);
			object.setxPos(object.getxPos() + horizontalMovement);
			
			verticalMovement = ( docPt.getyPos() - object.getyPos() - object.getHeight());
			object.setHeight(object.getHeight() + verticalMovement);
		}
		else if (dotVal == NORTHWEST_DOT){
			horizontalMovement = ( docPt.getxPos() - object.getxPos());
			object.setWidth(object.getWidth() - horizontalMovement);
			object.setxPos(object.getxPos() + horizontalMovement);
			
			verticalMovement = ( docPt.getyPos() - object.getyPos());
			object.setHeight(object.getHeight() - verticalMovement);
			object.setyPos(object.getyPos() + verticalMovement);
		}
		
		if (object.getHeight() <= 0){
			if (object.getHeight() == 0){
				object.setHeight(1);
			}
			object.setHeight(Math.abs(object.getHeight()));
			object.setyPos(object.getyPos() - object.getHeight());
			if (dotVal == NORTH_DOT){
				docMouseListener.setCurrentDragDot(SOUTH_DOT);
			}
			else if (dotVal == SOUTH_DOT){
				docMouseListener.setCurrentDragDot(NORTH_DOT);
			}
			else if (dotVal == SOUTHEAST_DOT){
				docMouseListener.setCurrentDragDot(NORTHEAST_DOT);
			}
			else if (dotVal == SOUTHWEST_DOT){
				docMouseListener.setCurrentDragDot(NORTHWEST_DOT);
			}
			else if (dotVal == NORTHWEST_DOT){
				docMouseListener.setCurrentDragDot(SOUTHWEST_DOT);
			}
			else if (dotVal == NORTHEAST_DOT){
				docMouseListener.setCurrentDragDot(SOUTHEAST_DOT);
				
			}
		}
		
		if (object.getWidth() <= 0){
			if (object.getWidth() == 0){
				object.setWidth(1);
			}
			object.setWidth(Math.abs(object.getWidth()));
			object.setxPos(object.getxPos() - object.getWidth());
			if (dotVal == EAST_DOT){
				docMouseListener.setCurrentDragDot(WEST_DOT);
			}
			else if (dotVal == WEST_DOT){
				docMouseListener.setCurrentDragDot(EAST_DOT);
			}
			else if (dotVal == SOUTHEAST_DOT){
				docMouseListener.setCurrentDragDot(SOUTHWEST_DOT);
			}
			else if (dotVal == SOUTHWEST_DOT){
				docMouseListener.setCurrentDragDot(SOUTHEAST_DOT);
			}
			else if (dotVal == NORTHWEST_DOT){
				docMouseListener.setCurrentDragDot(NORTHEAST_DOT);
			}
			else if (dotVal == NORTHEAST_DOT){
				docMouseListener.setCurrentDragDot(NORTHWEST_DOT);
			}
		}
	}

	public void setResizeDotBeingDragged(boolean resizeDotBeingDragged) {
		this.resizeDotBeingDragged = resizeDotBeingDragged;
	}

	public boolean resizeDotBeingDragged() {
		return resizeDotBeingDragged;
	}

	public void setCurrResizeDot(int currResizeDot) {
		this.currResizeDot = currResizeDot;
	}

	public int getCurrResizeDot() {
		return currResizeDot;
	}
}
