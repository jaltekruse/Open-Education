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
import java.util.Vector;

import doc.GridPoint;
import doc.mathobjects.GraphObject;
import doc_gui.attributes.BooleanAttribute;
import doc_gui.attributes.DoubleAttribute;
import doc_gui.attributes.IntegerAttribute;

import tree.EvalException;
import tree.ExpressionParser;
import tree.ParseException;
import tree.ValueNotStoredException;
import tree.VarStorage;

public class Graph {
	
	public double X_MIN, X_MAX, Y_MIN, Y_MAX, X_STEP, Y_STEP, X_PIXEL, Y_PIXEL,
	THETA_STEP, THETA_MIN,THETA_MAX, POL_STEP, POL_AX_STEP;
	public float DOC_ZOOM_LEVEL;
	public boolean SHOW_GRID, SHOW_AXIS, SHOW_NUMBERS;
	public int X_SIZE, Y_SIZE, LINE_SIZE, LINE_SIZE_DEFAULT = 2, NUM_FREQ, X_PIC_ORIGIN, Y_PIC_ORIGIN, FONT_SIZE;
	public VarStorage varList;
//	private BufferedImage graphPic;
	private Vector<SingleGraph> singleGraphs;
	private Vector<PointOnGrid> freePoints;
	private CartAxis cartAxis;
	private SelectionGraphic selectionGraphic;
	private DragDisk dragDisk;
	private ExpressionParser parser;
	private GraphCalculationGraphics graphCalcGraphics;
	
	public Graph(){
		freePoints = new Vector<PointOnGrid>();
		varList = new VarStorage(new ExpressionParser());
		cartAxis = new CartAxis(this);
		graphCalcGraphics = new GraphCalculationGraphics(this);
		singleGraphs = new Vector<SingleGraph>();
		parser = new ExpressionParser();
	}
	
	public void repaint(Graphics g, int xSize, int ySize){
		try {
			X_MIN = varList.getVarVal("xMin").toDec().getValue();
			X_MAX = varList.getVarVal("xMax").toDec().getValue();
			Y_MIN = varList.getVarVal("yMin").toDec().getValue();
			Y_MAX = varList.getVarVal("yMax").toDec().getValue();
			X_STEP = varList.getVarVal("xStep").toDec().getValue();
			Y_STEP = varList.getVarVal("yStep").toDec().getValue();
			THETA_MIN = varList.getVarVal("thetaMin").toDec().getValue();
			THETA_MAX = varList.getVarVal("thetaMax").toDec().getValue();
			THETA_STEP = varList.getVarVal("thetaStep").toDec().getValue();
			SHOW_GRID = true;
			SHOW_AXIS = true;
		} catch (EvalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint(g, xSize, ySize, 1, 0, 0, null);
	}
	
	public void syncWithGraphObject(GraphObject gObj, int xSize, int ySize){
		X_SIZE = xSize;
		if (X_SIZE == 0){
			X_SIZE = 1;
		}
		Y_SIZE = ySize;
		if (Y_SIZE == 0){
			Y_SIZE = 1;
		}
		X_MIN = ((DoubleAttribute)gObj.getAttributeWithName("xMin")).getValue();
		X_MAX = ((DoubleAttribute)gObj.getAttributeWithName("xMax")).getValue();
		Y_MIN = ((DoubleAttribute)gObj.getAttributeWithName("yMin")).getValue();
		Y_MAX = ((DoubleAttribute)gObj.getAttributeWithName("yMax")).getValue();
		X_STEP = ((DoubleAttribute)gObj.getAttributeWithName("xStep")).getValue();
		Y_STEP = ((DoubleAttribute)gObj.getAttributeWithName("yStep")).getValue();
		FONT_SIZE = ((IntegerAttribute)gObj.getAttributeWithName("fontSize")).getValue();
		SHOW_AXIS = ((BooleanAttribute)gObj.getAttributeWithName("showAxis")).getValue();
		SHOW_GRID = ((BooleanAttribute)gObj.getAttributeWithName("showGrid")).getValue();
		SHOW_NUMBERS = ((BooleanAttribute)gObj.getAttributeWithName("showNumbers")).getValue();
		X_PIXEL = (X_MAX - X_MIN) / X_SIZE;
		Y_PIXEL = (Y_MAX - Y_MIN) / Y_SIZE;
		NUM_FREQ = 2;
	}
	
	public void repaint(Graphics g, int xSize, int ySize, float docZoomLevel,
			int xPicOrigin, int yPicOrigin, GraphObject gObj){
		
		try
		{	
			DOC_ZOOM_LEVEL = docZoomLevel;
			X_PIC_ORIGIN = xPicOrigin;
			Y_PIC_ORIGIN = yPicOrigin;
			X_SIZE = xSize;
			if (X_SIZE == 0){
				X_SIZE = 1;
			}
			Y_SIZE = ySize;
			if (Y_SIZE == 0){
				Y_SIZE = 1;
			}
			if (gObj != null)
			{//there is an associated GraphObject, so the graph is being used in OpenNotebook
				X_MIN = ((DoubleAttribute)gObj.getAttributeWithName("xMin")).getValue();
				X_MAX = ((DoubleAttribute)gObj.getAttributeWithName("xMax")).getValue();
				Y_MIN = ((DoubleAttribute)gObj.getAttributeWithName("yMin")).getValue();
				Y_MAX = ((DoubleAttribute)gObj.getAttributeWithName("yMax")).getValue();
				X_STEP = ((DoubleAttribute)gObj.getAttributeWithName("xStep")).getValue();
				Y_STEP = ((DoubleAttribute)gObj.getAttributeWithName("yStep")).getValue();
				FONT_SIZE = ((IntegerAttribute)gObj.getAttributeWithName("fontSize")).getValue();
				SHOW_AXIS = ((BooleanAttribute)gObj.getAttributeWithName("showAxis")).getValue();
				SHOW_GRID = ((BooleanAttribute)gObj.getAttributeWithName("showGrid")).getValue();
				SHOW_NUMBERS = ((BooleanAttribute)gObj.getAttributeWithName("showNumbers")).getValue();
			}
			X_PIXEL = (X_MAX - X_MIN) / X_SIZE;
			Y_PIXEL = (Y_MAX - Y_MIN) / Y_SIZE;
			NUM_FREQ = 2;

			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		g.setColor(Color.white);
		g.fillRect(X_PIC_ORIGIN, Y_PIC_ORIGIN, X_SIZE, Y_SIZE);
		
		cartAxis.draw(g);
		
		for (SingleGraph sg : singleGraphs){
			if ( ! sg.hasFocus() ){
				try {
					sg.draw(g);
				} catch (EvalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace(); 
				}
			}
		}
		
		graphCalcGraphics.drawIntegrals(g);
		//this loop is to draw the graphs that currently have focus, so they appear over
		//the integrals that are drawn with the line above
		for (SingleGraph sg : singleGraphs){
			if ( sg.hasFocus() ){
				try {
					sg.draw(g);
				} catch (EvalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		graphCalcGraphics.draw(g);
		
		if (selectionGraphic != null){
			selectionGraphic.draw(g);
		}
		if (dragDisk != null){
			dragDisk.draw(g);
		}
		for (PointOnGrid p : freePoints){
			try {
				p.draw(g);
			} catch (EvalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		graphCalcGraphics.drawInfoBoxes(g);
		
		g.setColor(Color.BLACK);
		((Graphics2D)g).setStroke(new BasicStroke(2 * DOC_ZOOM_LEVEL));
		g.drawRect(X_PIC_ORIGIN, Y_PIC_ORIGIN, xSize, ySize);
		
		((Graphics2D)g).setStroke(new BasicStroke(1));
		
//		g.dispose();
		//graphCompPic.flush();
	}
	
	public void addPoint(double x, double y){
		freePoints.add(new PointOnGrid(this, x, y));
	}
	
	public void addPointAtScreenPt(int x, int y){
		addPoint(x * X_PIXEL + X_MIN, -y * Y_PIXEL + Y_MAX);
	}
	
	public ExpressionParser getParser(){
		return parser;
	}
	
	public void shiftGraph(double xPix, double yPix){
		try{
			varList.updateVarVal("xMin", (xPix)*X_PIXEL);
			varList.updateVarVal("xMax", (xPix)*X_PIXEL);
			varList.updateVarVal("yMin", (yPix)*Y_PIXEL);
			varList.updateVarVal("yMax", (yPix)*Y_PIXEL);
		} catch (Exception ex){
			;
		}
	}
	
	public void removeSingleGraph(SingleGraph s){
		for (SingleGraph sg : singleGraphs){
			if (s.equals(sg)){
				graphCalcGraphics.removeAllWithGraph(s);
			}
		}
		singleGraphs.remove(s);
	}
	
	public void removeAllSingleGraphs(){
		singleGraphs.removeAllElements();
	}
	
	public void zoom(double rate) throws EvalException{
		X_MIN = varList.getVarVal("xMin").toDec().getValue();
		X_MAX = varList.getVarVal("xMax").toDec().getValue();
		Y_MIN = varList.getVarVal("yMin").toDec().getValue();
		Y_MAX = varList.getVarVal("yMax").toDec().getValue();
		
		//hacked solution to prevent drawing the grid, the auto-rescaling of the 
		//grid stops working after the numbers get too big
		if (X_MIN < -7E8 || X_MAX > 7E8 || Y_MIN < -7E8 || Y_MAX > 7E8){
			if (rate < 100)
			{//if the user is trying to zoom out farther, do nothing
				return;
			}
		}
		
		varList.updateVarVal("xMin", -1 * (X_MAX-X_MIN)*(100-rate)/100);
		varList.updateVarVal("xMax", (X_MAX-X_MIN)*(100-rate)/100);
		varList.updateVarVal("yMin", -1 * (Y_MAX-Y_MIN)*(100-rate)/100);
		varList.updateVarVal("yMax", (Y_MAX-Y_MIN)*(100-rate)/100);
	}
	
	public void zoomMouseRelative(double rate, int mouseX, int mouseY) throws EvalException{
		X_MIN = varList.getVarVal("xMin").toDec().getValue();
		X_MAX = varList.getVarVal("xMax").toDec().getValue();
		Y_MIN = varList.getVarVal("yMin").toDec().getValue();
		Y_MAX = varList.getVarVal("yMax").toDec().getValue();
		double xRatio = mouseX/X_SIZE;
		double yRatio = mouseY/Y_SIZE;
		
		//hacked solution to prevent drawing the grid, the auto-rescaling of the 
		//grid stops working after the numbers get too big
		if (X_MIN < -7E8 || X_MAX > 7E8 || Y_MIN < -7E8 || Y_MAX > 7E8){
			if (rate < 100)
			{//if the user is trying to zoom out farther, do nothing
				return;
			}
		}
		
		varList.updateVarVal("xMin", -1 * (X_MAX-X_MIN)*(100-rate)/100);
		varList.updateVarVal("xMax", (X_MAX-X_MIN)*(100-rate)/100);
		varList.updateVarVal("yMin", -1 * (Y_MAX-Y_MIN)*(100-rate)/100);
		varList.updateVarVal("yMax", (Y_MAX-Y_MIN)*(100-rate)/100);
	}
	
	public void setLineSize(int sizeInPixels) {
		LINE_SIZE = sizeInPixels;
	}
	
	public void AddGraph(SingleGraph graph){
		singleGraphs.add(graph);
	}

	public void setSelection(SelectionGraphic selectionGraphic) {
		this.selectionGraphic = selectionGraphic;
	}

	public SelectionGraphic getSelection() {
		return selectionGraphic;
	}
	
	public DragDisk getDragDisk(){
		return dragDisk;
	}
	
	public Vector<SingleGraph> getGraphs(){
		return singleGraphs;
	}

	public void setGraphCalcGrpahics(GraphCalculationGraphics graphCalcGraphics) {
		this.graphCalcGraphics = graphCalcGraphics;
	}

	public GraphCalculationGraphics getGraphCalcGraphics() {
		return graphCalcGraphics;
	}
}
