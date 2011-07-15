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
import java.awt.Rectangle;

import math_rendering.CompleteExpressionGraphic;

import tree.Expression;
import tree.ExpressionParser;
import tree.ParseException;

public class ExpressionObjectGUI extends MathObjectGUI {
	
	private ExpressionParser parser;

	public ExpressionObjectGUI(){
		parser = new ExpressionParser();
	}
	
	public void drawMathObject(ExpressionObject object, Graphics g, Point pageOrigin,
			float zoomLevel) {
		// TODO Auto-generated method stub
		
//		System.out.println("draw rect");
		g.setColor(Color.BLACK);
		int xOrigin = (int) (pageOrigin.getX() + object.getxPos() * zoomLevel);
		int yOrigin = (int) (pageOrigin.getY() + object.getyPos() * zoomLevel);
		int width = (int) (object.getWidth() * zoomLevel);
		int height = (int) (object.getHeight() * zoomLevel);
		int fontSize = (int) (object.getFontSize() * zoomLevel);
		int bufferSpace = (int) (7 * zoomLevel);
		
		CompleteExpressionGraphic ceg;
		try {
			if ( ! object.getExpression().equals("")){
				Expression e = parser.ParseExpression(object.getExpression());
				ceg = new CompleteExpressionGraphic(e);
				ceg.generateExpressionGraphic(g, bufferSpace + xOrigin,
						bufferSpace + yOrigin, fontSize, zoomLevel);
				ceg.draw();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		g.drawRect(xOrigin, yOrigin, width, height);
	}
}
