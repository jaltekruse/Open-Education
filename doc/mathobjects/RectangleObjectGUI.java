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
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;

public class RectangleObjectGUI extends MathObjectGUI{

	public void drawMathObject(RectangleObject object, Graphics g, Point pageOrigin,
			float zoomLevel) {
		// TODO Auto-generated method stub
		
//		System.out.println("draw rect");
		g.setColor(Color.BLACK);
		int xOrigin = (int) (pageOrigin.getX() + object.getxPos() * zoomLevel);
		int yOrigin = (int) (pageOrigin.getY() + object.getyPos() * zoomLevel);
		int width = (int) (object.getWidth() * zoomLevel);
		int height = (int) (object.getHeight() * zoomLevel);
		int thickness = (int) (object.getThickness() * zoomLevel);
		
		Graphics2D g2d = (Graphics2D)g; 
		g2d.setStroke(new BasicStroke(thickness));
		g2d.draw(new Rectangle(xOrigin, yOrigin, width, height));
		g2d.setStroke(new BasicStroke());
		
//		g.drawRect(xOrigin, yOrigin, width, height);
	}

}
