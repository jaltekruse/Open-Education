/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import tree.Decimal;
import tree.EvalException;
import tree.ExpressionParser;
import tree.Number;
import tree.ParseException;
import tree.Expression;

public class GraphedCartFunction extends GraphWithExpression {


	/**
	 * The default constructor, set the equation equal to an empty string,
	 * makes it not currently graphing, integral and tracing values are
	 * false.
	 */
	
	public GraphedCartFunction(String s, ExpressionParser ep, Graph g, Color c) {
		super(s, ep, g, c);
	}
	
	/**
	 * Constructor that takes all attributes of a function.
	 * 
	 * @param exParser- associated 
	 * @param eqtn - string of equation
	 * @param ind - string of independent var
	 * @param dep - string of dependent var
	 * @param connected - boolean for points to be connected when graphed
	 * @param c - a color to display the function with
	 */
	public GraphedCartFunction(ExpressionParser exParser, Graph g, String eqtn, String ind, String dep, 
			boolean connected, Color c) {
		super(exParser, g, eqtn, ind, dep, connected, c);

	}
	
	public GraphedCartFunction(ExpressionParser parser, Graph graph, Color color) {
		// TODO Auto-generated constructor stub
		super(parser, graph, color);
	}

	@Override
	public void draw(Graphics g) throws EvalException, ParseException {
		// TODO Auto-generated method stub
		
		//used to temporarily store the value stored in the independent and dependent vars,
		//this will allow it to be restored after graphing, so that if in the terminal a
		//value was assigned to x, it will not be overriden by the action of graphing
		Number xVal = getIndependentVar().getValue();
		Number yVal = getDependentVar().getValue();
		
		super.clearPts();
		Graphics2D g2d = ((Graphics2D)g);
		
		g.setColor(getColor());
		if (hasFocus()){
			graph.LINE_SIZE = 3;
		}
		else{
			graph.LINE_SIZE = 2;
		}
		
		int tempLineSize = graph.LINE_SIZE;
		
		double lastX, lastY, currX, currY;
		
			Expression expression = getParser().ParseExpression(getFuncEqtn());
			getIndependentVar().setValue(new Decimal(graph.X_MIN));
			expression.eval();
			lastX = getIndependentVar().getValue().toDec().getValue();
			lastY = getDependentVar().getValue().toDec().getValue();
			
			if (gridxToScreen(lastX) <= graph.X_SIZE &&  gridxToScreen(lastX) >= 0
					&& gridyToScreen(lastY) <= graph.Y_SIZE && gridyToScreen(lastY) >= 0)
			{//if the current point is on the screen, add it to the list of points
				addPt(gridxToScreen(lastX), gridyToScreen(lastY));
			}
			for (int i = 1; i < graph.X_SIZE; i += 2) {
				getIndependentVar().updateValue(2 * graph.X_PIXEL);
				expression.eval();
				currX = getIndependentVar().getValue().toDec().getValue();
				currY = getDependentVar().getValue().toDec().getValue();
				
//				System.out.println("x: " + currX + " y: " + currY);
				if (gridxToScreen(currX) <= graph.X_SIZE + graph.X_PIC_ORIGIN
						&&  gridxToScreen(currX) >= graph.X_PIC_ORIGIN
						&& gridyToScreen(currY) <= graph.Y_SIZE + graph.Y_PIC_ORIGIN
						&& gridyToScreen(currY) >= graph.Y_PIC_ORIGIN)
				{//if the current point is on the screen, add it to the list of points
					addPt(gridxToScreen(currX), gridyToScreen(currY));
//					System.out.println("currPt on screen");
					if (lastY <= graph.Y_MIN){
						addPt(gridxToScreen(lastX), graph.Y_SIZE + graph.Y_PIC_ORIGIN);
					}
					if (lastY >= graph.Y_MAX){
						addPt(gridxToScreen(lastX), 0 + graph.Y_PIC_ORIGIN);
					}
				}
				else if (gridxToScreen(lastX) <= graph.X_SIZE + graph.X_PIC_ORIGIN
						&&  gridxToScreen(lastX) >= graph.X_PIC_ORIGIN
						&& gridyToScreen(lastY) <= graph.Y_SIZE + graph.Y_PIC_ORIGIN
						&& gridyToScreen(lastY) >= graph.Y_PIC_ORIGIN)
				{//if the current point is on the screen, add it to the list of points
					addPt(gridxToScreen(lastX), gridyToScreen(lastY));
//					System.out.println("currPt on screen");
					if (currY <= graph.Y_MIN){
						addPt(gridxToScreen(lastX), graph.Y_SIZE + graph.Y_PIC_ORIGIN);
					}
					if (currY >= graph.Y_MAX){
						addPt(gridxToScreen(lastX), 0 + graph.Y_PIC_ORIGIN);
					}
				}
				else if (lastY >= graph.Y_MAX && currY <= graph.Y_MIN)
				{//if the last point was off the the top of the screen, and this one is off
					//the bottom, add the two to the list of points
					addPt(gridxToScreen(lastX), graph.Y_SIZE + graph.Y_PIC_ORIGIN);
					addPt(gridxToScreen(currX), 0 + graph.Y_PIC_ORIGIN);
				}
				else if (currY >= graph.Y_MAX && lastY <= graph.Y_MIN)
				{//if the last point was off the the bottom of the screen, and this one is off
					//the top, add the two to the list of points
					addPt(gridxToScreen(lastX), 0 + graph.Y_PIC_ORIGIN);
					addPt(gridxToScreen(currX), graph.Y_SIZE + graph.Y_PIC_ORIGIN);
//					System.out.println("curr off bottom");
				}
				
				if (isConnected()){
					drawLineSeg(lastX, lastY, currX, currY, getColor(), g);
				}
				
				lastX = currX;
				lastY = currY;
			}
//			if (isConnected()){
//				int lastxPt, lastyPt,currxPt, curryPt;
//				g.setColor(getColor());
//				g2d.setStroke(new BasicStroke(graph.LINE_SIZE * graph.DOC_ZOOM_LEVEL));
//				for ( int i = 0; i < super.getxVals().size() - 1; i++){
//					lastxPt = super.getxVals().get(i);
//					lastyPt = super.getyVals().get(i);
//					currxPt = super.getxVals().get(i + 1);
//					curryPt = super.getyVals().get(i + 1);
//					
//						g2d.drawLine(lastxPt, lastyPt, currxPt, curryPt);
//					}
//			}
		graph.LINE_SIZE = 2;
		g2d.setStroke(new BasicStroke(1));
	}

}
