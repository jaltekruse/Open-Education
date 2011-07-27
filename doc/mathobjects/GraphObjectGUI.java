/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import gui.graph.Graph;
import gui.graph.GraphedCartFunction;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import tree.ExpressionParser;

public class GraphObjectGUI extends MathObjectGUI {

	Graph graph;
	
	public GraphObjectGUI(){
		graph = new Graph();
	}
	
	public void drawMathObject(GraphObject object, Graphics g,
			Point pageOrigin, float zoomLevel) {
		// TODO Auto-generated method stub
		
		int xOrigin = (int) (pageOrigin.getX() + object.getxPos() * zoomLevel);
		int yOrigin = (int) (pageOrigin.getY() + object.getyPos() * zoomLevel);
		int width = (int) (object.getWidth() * zoomLevel);
		int height = (int) (object.getHeight() * zoomLevel);
		
		graph.removeAllSingleGraphs();
		if ( ! object.getExpression().equals("")){
			graph.AddGraph(new GraphedCartFunction("y=" + object.getExpression(),
					new ExpressionParser(), graph, Color.BLUE));
		}
		
		graph.repaint(g, width , height, zoomLevel, xOrigin, yOrigin, object);
		
	}
	
	public void mouseClicked(GraphObject gObj, int x , int y){
		//add a point to the graph
		graph.syncWithGraphObject(gObj);
		graph.addPointAtScreenPt(x, y);
		
	}
}
