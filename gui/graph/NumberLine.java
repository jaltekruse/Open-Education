package gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import tree.Decimal;
import tree.EvalException;
import tree.ParseException;

import doc.mathobjects.DoubleAttribute;
import doc.mathobjects.IntegerAttribute;
import doc.mathobjects.NumberLineObject;

public class NumberLine {
	
	private NumberLineComponent numLine;
	
	private Graph graph;
	
	private int OVERHANG;
	
	public NumberLine(){
		graph = new Graph();
		numLine = new NumberLineComponent(graph);
	}
	
	public void repaint(Graphics g, int xSize, int ySize, float docZoomLevel,
			int xPicOrigin, int yPicOrigin, NumberLineObject gObj){
		
		graph.DOC_ZOOM_LEVEL = docZoomLevel;
		OVERHANG = (int) (15 * graph.DOC_ZOOM_LEVEL);
		graph.X_PIC_ORIGIN = xPicOrigin + OVERHANG;
		graph.Y_PIC_ORIGIN = yPicOrigin;
		graph.X_SIZE = xSize - 2 * OVERHANG;
		if (graph.X_SIZE == 0){
			graph.X_SIZE = 1;
		}
		graph.Y_SIZE = ySize;
		if (graph.Y_SIZE == 0){
			graph.Y_SIZE = 1;
		}
		graph.FONT_SIZE = 8;
		
		graph.X_MIN = ((DoubleAttribute)gObj.getAttributeWithName("min")).getValue();
		graph.X_MAX = ((DoubleAttribute)gObj.getAttributeWithName("max")).getValue();
		graph.X_STEP = ((DoubleAttribute)gObj.getAttributeWithName("step")).getValue();
		graph.FONT_SIZE = ((IntegerAttribute)gObj.getAttributeWithName("fontSize")).getValue();
		graph.Y_MIN = -3;
		graph.Y_MAX = 3;
		graph.X_PIXEL = (graph.X_MAX - graph.X_MIN) / graph.X_SIZE;
		graph.Y_PIXEL = (graph.Y_MAX - graph.Y_MIN) / graph.Y_SIZE;
		graph.NUM_FREQ = 2;
		try {
			numLine.draw(g);
		} catch (EvalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class NumberLineComponent extends GraphComponent{
		
		public NumberLineComponent(Graph g) {
			super(g);
			// TODO Auto-generated constructor stub
		}

		public void draw(Graphics g) throws EvalException, ParseException {
			
			Font oldFont = g.getFont();
			
			g.setFont(g.getFont().deriveFont(graph.FONT_SIZE * graph.DOC_ZOOM_LEVEL));
			
			try {
				//these four statements are for resizing the grid after zooming
				if((graph.X_MAX-graph.X_MIN)/graph.X_STEP >= 24)
				{
					if ((graph.X_MAX-graph.X_MIN)/20 > 1)
					{
						graph.varList.setVarVal("xStep", new Decimal((int)(graph.X_MAX-graph.X_MIN)/20));
						graph.X_STEP = graph.varList.getVarVal("xStep").toDec().getValue();
					}
					else
					{
						for (int i = 0; i < 25; i ++){
							if ((graph.X_MAX-graph.X_MIN)/20/Math.pow(.5, i) < .7){
								graph.varList.setVarVal("xStep", new Decimal(Math.pow(.5, i)));
								graph.X_STEP = graph.varList.getVarVal("xStep").toDec().getValue();
							}
						}
					}
				}
				
				else if((graph.X_MAX-graph.X_MIN)/graph.X_STEP < 3){
					if ((graph.X_MAX-graph.X_MIN)/10 > 1)
					{
						graph.varList.setVarVal("xStep", new Decimal((int)(graph.X_MAX-graph.X_MIN)/10));
						graph.X_STEP = graph.varList.getVarVal("xStep").toDec().getValue();
					}
					else
					{
						for (int i = 0; i < 25; i ++){
							if ((graph.X_MAX-graph.X_MIN)/20 < Math.pow(.5, i)){
								graph.varList.setVarVal("xStep", new Decimal(Math.pow(.5, i)));
								graph.X_STEP = graph.varList.getVarVal("xStep").toDec().getValue();
							}
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			int width;
			int height = (int) ( g.getFontMetrics().getHeight() * (.6));
			int numberAndAxisSpace = (int) ( 5 * graph.DOC_ZOOM_LEVEL );
			int numberInset = (int) (1 * graph.DOC_ZOOM_LEVEL );
			int tempFreq;
			
			// finds the fist factor of the graph.X_STEP on the screen
			// used to draw the first dash mark on the x-axis
			double tempX = (int) (graph.X_MIN / graph.X_STEP);
			tempX *= graph.X_STEP;

			int tempWidth = g.getFontMetrics().stringWidth(Double.toString(tempX));
			if(tempWidth > (int) ((graph.X_MAX-graph.X_MIN)/(graph.X_STEP * graph.NUM_FREQ))){
				graph.NUM_FREQ = (int) (((graph.X_MAX-graph.X_MIN)/(graph.X_STEP))/((graph.X_SIZE)/tempWidth)) + 1;
			}
			
			while (tempX <= graph.X_MAX) {
				
				String ptText = null;
				if (tempX % 1 == 0 && tempX < Integer.MAX_VALUE &&
						tempX > Integer.MIN_VALUE){//number is int
					ptText = Integer.toString((int) tempX); 
				}
				else{
					ptText = Double.toString(tempX);
				}
				tempWidth = g.getFontMetrics().stringWidth(ptText);
				if(tempWidth > (int) ((graph.X_MAX-graph.X_MIN)/(graph.X_STEP * graph.NUM_FREQ))){
					tempFreq = (int) (((graph.X_MAX-graph.X_MIN)/(graph.X_STEP))/((graph.X_SIZE)/tempWidth)) + 1;
					if (tempFreq > graph.NUM_FREQ){
						graph.NUM_FREQ = tempFreq;
					}
				}
				
				setLineSize(graph.LINE_SIZE_DEFAULT);
				drawLineSeg(tempX, 2 * graph.LINE_SIZE * graph.Y_PIXEL, tempX, -2
						* graph.LINE_SIZE * graph.Y_PIXEL, Color.BLACK, g);
				tempX += graph.X_STEP;
			}
			
			tempX = (int) (graph.X_MIN / graph.X_STEP);
			tempX *= graph.X_STEP;
			while (tempX <= graph.X_MAX) {
				if(tempX%(graph.NUM_FREQ * graph.X_STEP) == 0)
				{//if its reached a place where a number should be drawn
					String ptText = null;
					if (tempX % 1 == 0 && tempX < Integer.MAX_VALUE &&
							tempX > Integer.MIN_VALUE){//number is int
						ptText = Integer.toString((int) tempX); 
					}
					else{
						ptText = Double.toString(tempX);
					}
					width = g.getFontMetrics().stringWidth(ptText);
					g.setColor(Color.white);
					g.fillRect(gridxToScreen(tempX) - (width/2) - numberInset,
							gridyToScreen(0) - height - numberAndAxisSpace - 2 * numberInset,
							width + numberInset * 2, height + 2 * numberInset);
					g.setColor(Color.black);
					g.drawString(ptText, gridxToScreen(tempX) - (width/2) ,
							gridyToScreen(0) - numberAndAxisSpace - numberInset);
				}
				tempX += graph.X_STEP;
			}
			
			int thickness = (int) (graph.LINE_SIZE * graph.DOC_ZOOM_LEVEL);
			int yGridOrigin = gridyToScreen(0);
			Graphics2D g2d = (Graphics2D)g; 
			g2d.setStroke(new BasicStroke(thickness));
			
			g.setColor(Color.BLACK);	
			
			g.drawLine(graph.X_PIC_ORIGIN - OVERHANG, yGridOrigin,
					graph.X_PIC_ORIGIN + graph.X_SIZE + OVERHANG, yGridOrigin);
			g2d.setStroke(new BasicStroke());
			
			int heightAboveAxis = (int)(5 * (graph.DOC_ZOOM_LEVEL));
			int distBeyondAxis = (int)(5 * (graph.DOC_ZOOM_LEVEL));
			Polygon p = new Polygon();
			p.addPoint(gridxToScreen(graph.X_MIN) - distBeyondAxis - OVERHANG, yGridOrigin);
			p.addPoint(gridxToScreen(graph.X_MIN) + heightAboveAxis - OVERHANG, yGridOrigin + heightAboveAxis);
			p.addPoint(gridxToScreen(graph.X_MIN) + heightAboveAxis - OVERHANG, yGridOrigin - heightAboveAxis);
			g.fillPolygon(p);
			
			p.reset();
			p.addPoint(gridxToScreen(graph.X_MAX) + distBeyondAxis + OVERHANG, yGridOrigin);
			p.addPoint(gridxToScreen(graph.X_MAX) - heightAboveAxis + OVERHANG, yGridOrigin + heightAboveAxis);
			p.addPoint(gridxToScreen(graph.X_MAX) - heightAboveAxis + OVERHANG, yGridOrigin - heightAboveAxis);
			g.fillPolygon(p);
			
			g.setFont(oldFont);
		}
		
	}

}
