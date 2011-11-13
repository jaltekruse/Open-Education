/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui.mathobject_gui;

import gui.graph.Graph;
import gui.graph.GraphedCartFunction;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import doc.mathobjects.GraphObject;

import tree.EvalException;
import tree.ExpressionParser;
import tree.ParseException;

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
		
		try {
			graph.repaint(g, width , height, zoomLevel, xOrigin, yOrigin, object);
		} catch (EvalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void mouseClicked(GraphObject gObj, int x , int y, float zoomLevel){
		//add a point to the graph
		graph.pullVarsFromGraphObject(gObj, (int) (gObj.getWidth() * zoomLevel),
				(int) (gObj.getHeight() * zoomLevel) );
		graph.addPointAtScreenPt(x, y);
	}
}
