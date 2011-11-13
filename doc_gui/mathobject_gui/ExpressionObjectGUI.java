/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui.mathobject_gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import doc.mathobjects.ExpressionObject;
import expression.Node;

import math_rendering.RootNodeGraphic;

import tree.ExpressionParser;
import tree.ParseException;

public class ExpressionObjectGUI extends MathObjectGUI {

	private ExpressionParser parser;

	public ExpressionObjectGUI(){
		parser = new ExpressionParser();
	}

	public void drawInteractiveComponents(ExpressionObject object, Graphics g, Point pageOrigin, float zoomLevel){
		//		System.out.println("draw rect");
		g.setColor(Color.BLACK);
		int xOrigin = (int) (pageOrigin.getX() + object.getxPos() * zoomLevel);
		int yOrigin = (int) (pageOrigin.getY() + object.getyPos() * zoomLevel);
		int width = (int) (object.getWidth() * zoomLevel);
		int height = (int) (object.getHeight() * zoomLevel);
		int fontSize = (int) (object.getFontSize() * zoomLevel);
		int outerBufferSpace = (int) (5 * zoomLevel);
		int stepBufferSpace = (int) (10 * zoomLevel);
		Graphics2D g2d = (Graphics2D) g;
		int shadowSize = (int) (5 * zoomLevel);

		RootNodeGraphic ceg;
		try {
			if ( ! object.getExpression().equals("")){
				Node n = Node.parseNode(object.getExpression());
				Vector<RootNodeGraphic> expressions = new Vector<RootNodeGraphic>();
				ceg = new RootNodeGraphic(n);
				ceg.generateExpressionGraphic(g, outerBufferSpace + xOrigin,
						outerBufferSpace + yOrigin, fontSize, zoomLevel);
				expressions.add(ceg);
				String stepsString = object.getAttributeWithName("steps").getValue().toString();
				Vector<String> steps = new Vector<String>();
				int totalHeight = ceg.getHeight();
				int greatestWidth = ceg.getWidth();
				int lastEnd = 0;
				for (int i = 0; i < stepsString.length(); i++){
					if (stepsString.charAt(i) == ';'){
						steps.add(stepsString.substring(lastEnd, i));
						lastEnd = i + 1;
					}
					else if ( i == stepsString.length() - 1){
						steps.add(stepsString.substring(lastEnd, i + 1));
					}
				}
				for (String s : steps){
					totalHeight += stepBufferSpace;
					n = Node.parseNode(s);
					RootNodeGraphic root = new RootNodeGraphic(n);
					root.generateExpressionGraphic(g, xOrigin + outerBufferSpace,
							outerBufferSpace + yOrigin + totalHeight, fontSize, zoomLevel);
					expressions.add(root);
					if (root.getWidth() > greatestWidth){
						greatestWidth = root.getWidth();
					}
					totalHeight += root.getHeight();
				}
				
				g.setColor(Color.WHITE);
				g.fillRect(xOrigin, yOrigin, greatestWidth + 2 * outerBufferSpace,
						totalHeight + 2 * outerBufferSpace);
				g.setColor(Color.BLACK);
				for (RootNodeGraphic r : expressions){
					r.draw();
				}
				if (greatestWidth > 0 && totalHeight > 0){
					object.setWidth( (int) ((greatestWidth + 2 * outerBufferSpace) / zoomLevel ));
					object.setHeight( (int) ((totalHeight + 2 * outerBufferSpace ) / zoomLevel ));
				}
				
				// draw the gray outline to show it is selected
				g.setColor(Color.GRAY);
				g.fillRect(xOrigin - shadowSize, yOrigin - shadowSize, shadowSize,
						(int) (object.getHeight() * zoomLevel) + 2 * shadowSize);
				g.fillRect(xOrigin + (int) (object.getWidth() * zoomLevel), yOrigin - shadowSize, shadowSize,
						(int) (object.getHeight() * zoomLevel) + 2 * shadowSize);
				g.fillRect(xOrigin - shadowSize, yOrigin - shadowSize,
						(int) (object.getWidth() * zoomLevel) + 2 * shadowSize,
						shadowSize);
				g.fillRect(xOrigin - shadowSize, yOrigin + (int) (object.getHeight() * zoomLevel),
						(int) (object.getWidth() * zoomLevel) + 2 * shadowSize,
						shadowSize);
				
				// draw the black box around all of the expressions
				g.setColor(Color.BLACK);
				g.drawRect(xOrigin, yOrigin , (int) (object.getWidth() * zoomLevel)
						, (int) (object.getHeight() * zoomLevel));
				


			}
			else{
				g2d.setStroke(new BasicStroke());
				g2d.setPaint(Color.BLACK);
				g.drawRect(xOrigin, yOrigin , width, height);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

	public void drawMathObject(ExpressionObject object, Graphics g, Point pageOrigin,
			float zoomLevel) {
		// TODO Auto-generated method stub

		g.setColor(Color.BLACK);
		int xOrigin = (int) (pageOrigin.getX() + object.getxPos() * zoomLevel);
		int yOrigin = (int) (pageOrigin.getY() + object.getyPos() * zoomLevel);
		int width = (int) (object.getWidth() * zoomLevel);
		int height = (int) (object.getHeight() * zoomLevel);
		int fontSize = (int) (object.getFontSize() * zoomLevel);
		int bufferSpace = (int) (5 * zoomLevel);

		RootNodeGraphic ceg;
		try {
			if ( ! object.getExpression().equals("")){
				Node n = Node.parseNode(object.getExpression());
				ceg = new RootNodeGraphic(n);
				ceg.generateExpressionGraphic(g, bufferSpace + xOrigin,
						bufferSpace + yOrigin, fontSize, zoomLevel);
				ceg.draw();
				object.setWidth( (int) (ceg.getWidth() / zoomLevel) + 10);
				object.setHeight( (int) (ceg.getHeight() / zoomLevel) + 10);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (object.getExpression().equals("")){
			g.drawRect(xOrigin, yOrigin, width, height);
		}
	}
}
