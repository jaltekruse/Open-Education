/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public class PolygonObjectGUI extends MathObjectGUI {
	
	private static final int dotRadius = 3;
	
	public void drawMathObject(PolygonObject object, Graphics g, Point pageOrigin, float zoomLevel){
		g.setColor(Color.BLACK);
		int xOrigin = (int) (pageOrigin.getX() + object.getxPos() * zoomLevel);
		int yOrigin = (int) (pageOrigin.getY() + object.getyPos() * zoomLevel);
		int width = (int) (object.getWidth() * zoomLevel);
		int height = (int) (object.getHeight() * zoomLevel);
		int thickness = (int) (object.getThickness() * zoomLevel);
		
		Graphics2D g2d = (Graphics2D)g; 
		g2d.setStroke(new BasicStroke(thickness));
		
		Polygon p = new Polygon();
		for (int i = 0; i < object.getVertices().size(); i++){
			p.addPoint((int) (object.getVertices().get(i).getx() * width) + xOrigin,
					(int) (object.getVertices().get(i).gety() * height) + yOrigin);
		}
		
		g2d.drawPolygon(p);
		
		g2d.setStroke(new BasicStroke(1));
	}
	
	public void drawInteractiveComponents(PolygonObject object, Graphics g, Point pageOrigin, float zoomLevel){
		int xOrigin = (int) (pageOrigin.getX() + object.getxPos() * zoomLevel);
		int yOrigin = (int) (pageOrigin.getY() + object.getyPos() * zoomLevel);
		int width = (int) (object.getWidth() * zoomLevel);
		int height = (int) (object.getHeight() * zoomLevel);
		int thickness = (int) (object.getThickness() * zoomLevel);
		
		for (int i = 0; i < object.getVertices().size(); i++){
			g.setColor(Color.YELLOW);
			g.fillOval((int) (object.getVertices().get(i).getx() * width) + xOrigin - dotRadius,
					(int) (object.getVertices().get(i).gety() * height) + yOrigin - dotRadius
					, 2 * dotRadius, 2 * dotRadius);
			g.setColor(Color.BLACK);
			g.drawOval((int) (object.getVertices().get(i).getx() * width) + xOrigin - dotRadius,
					(int) (object.getVertices().get(i).gety() * height) + yOrigin - dotRadius
					, 2 * dotRadius, 2 * dotRadius);
		}
	}
}
