/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui.mathobject_gui;


import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import doc.attributes.StringAttribute;
import doc.mathobjects.GraphObject;
import doc_gui.graph.Graph;
import doc_gui.graph.GraphedCartFunction;

import tree.EvalException;
import tree.ExpressionParser;
import tree.ParseException;

public class GraphObjectGUI extends MathObjectGUI<GraphObject> {

	Graph graph;
	
	public static final Color[] graphColors = {Color.BLUE, Color.GREEN.darker(),
		Color.RED.darker()};
	
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
		int colorIndex = 0;
		ExpressionParser parser = new ExpressionParser();
		boolean hadError = false;
		for ( StringAttribute ex : object.getExpressions()){
			if ( ! ex.getValue().equals("")){
				try {
					graph.AddGraph(new GraphedCartFunction("y=" + ex.getValue(),
							parser, graph, graphColors[colorIndex]));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					hadError = true;
				}
			}
			colorIndex++;
		}
		
		graph.repaint(g, width , height, zoomLevel, xOrigin, yOrigin, object);
		
		if (hadError){
			FontMetrics fm = g.getFontMetrics();
			int errorWidth = fm.stringWidth("error");
			g.setColor(Color.WHITE);
			g.fillRect((xOrigin + width/2) - errorWidth/2
					, (yOrigin + height/2) - fm.getHeight()/2,
					errorWidth + 4, fm.getHeight() + 4);
			g.setColor(Color.BLACK);
			g.drawRect((xOrigin + width/2) - errorWidth/2
					, (yOrigin + height/2) - fm.getHeight()/2,
					errorWidth + 4, fm.getHeight() + 4);
			g.setColor(Color.RED);
			g.drawString("error", (xOrigin + width/2) - errorWidth/2 + 2
					, (yOrigin + height/2) + fm.getHeight()/2);
		}
		
	}
	
	public void mouseClicked(GraphObject gObj, int x , int y, float zoomLevel){
		//add a point to the graph
		graph.pullVarsFromGraphObject(gObj, (int) (gObj.getWidth() * zoomLevel),
				(int) (gObj.getHeight() * zoomLevel) );
		graph.addPointAtScreenPt(x, y);
	}
}
