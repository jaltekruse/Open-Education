/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import tree.Decimal;

public class CartAxis extends GraphComponent{

	public CartAxis(Graph g) {
		super(g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
		Font oldFont = g.getFont();
		
		g.setFont(g.getFont().deriveFont(graph.FONT_SIZE * graph.DOC_ZOOM_LEVEL));
		
		try {
			//these four statements are for resizing the grid after zooming
			if((graph.X_MAX-graph.X_MIN)/graph.X_STEP >= 16)
			{
				if ((graph.X_MAX-graph.X_MIN)/14 > 1)
				{
					graph.varList.setVarVal("xStep", new Decimal((int)(graph.X_MAX-graph.X_MIN)/14));
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
			
			else if((graph.X_MAX-graph.X_MIN)/graph.X_STEP <= 5){
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
			
			if((graph.Y_MAX-graph.Y_MIN)/graph.Y_STEP >= 16)
			{
				if ((graph.Y_MAX-graph.Y_MIN)/14 > 1)
				{
					graph.varList.setVarVal("yStep", new Decimal((int)(graph.Y_MAX-graph.Y_MIN)/14));
					graph.Y_STEP = graph.varList.getVarVal("yStep").toDec().getValue();
				}
				else
				{
					for (int i = 0; i < 25; i ++){
						if ((graph.Y_MAX-graph.Y_MIN)/20/Math.pow(.5, i) < .7){
							graph.varList.setVarVal("yStep", new Decimal(Math.pow(.5, i)));
							graph.Y_STEP = graph.varList.getVarVal("xStep").toDec().getValue();
						}
					}
				}
			}
			
			else if((graph.Y_MAX-graph.Y_MIN)/graph.Y_STEP <= 5){
				if ((graph.Y_MAX-graph.Y_MIN)/10 > 1)
				{
					graph.varList.setVarVal("yStep", new Decimal((int)(graph.Y_MAX-graph.Y_MIN)/10));
					graph.Y_STEP = graph.varList.getVarVal("yStep").toDec().getValue();
				}
				else
				{
					for (int i = 0; i < 25; i ++){
						if ((graph.Y_MAX-graph.Y_MIN)/20 < Math.pow(.5, i)){
							graph.varList.setVarVal("yStep", new Decimal(Math.pow(.5, i)));
							graph.Y_STEP = graph.varList.getVarVal("xStep").toDec().getValue();
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// finds the fist factor of the graph.Y_STEP on the screen
		// used to draw the first dash mark on the y-axis
		double tempY = (int) (graph.Y_MIN / graph.Y_STEP);
		tempY *= graph.Y_STEP;
		int width;
		int height = (int) ( g.getFontMetrics().getHeight() * (.6));
		int numberAndAxisSpace = (int) ( 5 * graph.DOC_ZOOM_LEVEL );
		int numberInset = (int) (1 * graph.DOC_ZOOM_LEVEL );
		Vector<Double> yNumbers = new Vector<Double>();

		if (graph.X_MIN <= 0 && graph.X_MAX >= 0)
		{//the y axis is showing
			while (tempY <= graph.Y_MAX) 
			{//there are still more dashes to draw
				if (graph.SHOW_GRID){
					setLineSize(1);
					drawLineSeg(graph.X_MIN, tempY, graph.X_MAX, tempY, Color.GRAY, g);
				}
				if (graph.SHOW_AXIS){
					setLineSize(graph.LINE_SIZE_DEFAULT);
					drawLineSeg(2 * graph.LINE_SIZE * graph.X_PIXEL, tempY, -2 * graph.LINE_SIZE
							* graph.X_PIXEL, tempY, Color.BLACK, g);
				}
				if(tempY%(2 * graph.Y_STEP) == 0 && tempY != 0){
					yNumbers.add(tempY);
				} 
				tempY += graph.Y_STEP;
			}
		}
		else
		{//the y axis is not showing
			if(graph.X_MIN >= 0)
			{
				while (tempY <= graph.Y_MAX)
				{
					if (graph.SHOW_GRID){
						setLineSize(1);
						drawLineSeg(graph.X_MIN, tempY, graph.X_MAX, tempY, Color.GRAY, g);
					}
					if (graph.SHOW_AXIS){
						setLineSize(graph.LINE_SIZE_DEFAULT);
						g.setColor(Color.BLACK);
						drawLineSeg(0, tempY, 2 * graph.LINE_SIZE * graph.X_PIXEL, tempY, Color.BLACK, g);
					}
					if(tempY%(2 * graph.Y_STEP) == 0 && tempY != 0)
					{
						yNumbers.add(tempY);
					} 
					tempY += graph.Y_STEP;
				}
			}
			else if(graph.X_MAX <= 0){
				while (tempY <= graph.Y_MAX) {
					if (graph.SHOW_GRID){
						setLineSize(1);
						drawLineSeg(graph.X_MIN, tempY, graph.X_MAX, tempY, Color.GRAY, g);
					}
					if (graph.SHOW_AXIS){
						setLineSize(graph.LINE_SIZE_DEFAULT);
						g.setColor(Color.BLACK);
						ptOn(graph.X_MAX - graph.LINE_SIZE_DEFAULT * graph.X_PIXEL, tempY, g);
					}
					if(tempY%(2 * graph.Y_STEP) == 0 && tempY != 0){
						yNumbers.add(tempY);
					}
					tempY += graph.Y_STEP;
					}
			}
		}
		
		// finds the fist factor of the graph.X_STEP on the screen
		// used to draw the first dash mark on the x-axis
		double tempX = (int) (graph.X_MIN / graph.X_STEP);
		tempX *= graph.X_STEP;

		int tempWidth = g.getFontMetrics().stringWidth(Double.toString(tempX));
		if(tempWidth > (int) ((graph.X_MAX-graph.X_MIN)/(graph.X_STEP * graph.NUM_FREQ))){
			graph.NUM_FREQ = (int) (((graph.X_MAX-graph.X_MIN)/(graph.X_STEP))/((graph.X_SIZE)/tempWidth)) + 1;
		}
		if (graph.Y_MIN <= 0 && graph.Y_MAX >= 0) {
			while (tempX <= graph.X_MAX) {
				if (graph.SHOW_GRID){
					setLineSize(1);
					drawLineSeg(tempX, graph.Y_MIN, tempX, graph.Y_MAX, Color.GRAY, g);
				}
				if (graph.SHOW_AXIS){
					setLineSize(graph.LINE_SIZE_DEFAULT);
					drawLineSeg(tempX, 2 * graph.LINE_SIZE * graph.Y_PIXEL, tempX, -2
							* graph.LINE_SIZE * graph.Y_PIXEL, Color.BLACK, g);
				}
				
				if(tempX%(graph.NUM_FREQ * graph.X_STEP) == 0 && tempX != 0)
				{//if its reached a place where a number should be drawn
					if (graph.SHOW_NUMBERS){
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
				} 
				tempX += graph.X_STEP;
			}
		}
		else {
			if( graph.Y_MIN >= 0){
				while (tempX <= graph.X_MAX) {
					if (graph.SHOW_GRID){
						setLineSize(1);
						drawLineSeg(tempX, graph.Y_MIN, tempX, graph.Y_MAX, Color.GRAY, g);
					}
					if (graph.SHOW_AXIS){
						setLineSize(graph.LINE_SIZE_DEFAULT);
						g.setColor(Color.BLACK);
						ptOn(tempX, graph.Y_MIN, g);
					}
					if(tempX%(graph.NUM_FREQ * graph.X_STEP) == 0 && tempX != 0){
						if (graph.SHOW_NUMBERS){
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
									gridyToScreen(graph.Y_MIN) - height - numberInset * 2 - numberAndAxisSpace
									, width + numberInset * 2, height + 2 * numberInset);
							g.setColor(Color.black);
							g.drawString(ptText, gridxToScreen(tempX) - (width/2)
									, gridyToScreen(graph.Y_MIN) - numberAndAxisSpace - numberInset);
						}
					} 
					tempX += graph.X_STEP;
				}
			}
			if( graph.Y_MAX <= 0){
				while (tempX <= graph.X_MAX) {
					if (graph.SHOW_GRID){
						setLineSize(1);
						drawLineSeg(tempX, graph.Y_MIN, tempX, graph.Y_MAX, Color.GRAY, g);
					}
					if (graph.SHOW_AXIS){
						setLineSize(graph.LINE_SIZE_DEFAULT);
						g.setColor(Color.BLACK);
						ptOn(tempX, graph.Y_MAX - 1 * graph.LINE_SIZE * graph.Y_PIXEL, g);
					}
					if(tempX%(graph.NUM_FREQ * graph.X_STEP) == 0 && tempX != 0){
						if (graph.SHOW_NUMBERS){
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
									gridyToScreen(graph.Y_MAX) + numberAndAxisSpace
									, width + numberInset * 2, height + 2 * numberInset);
							g.setColor(Color.black);
							g.drawString(ptText, gridxToScreen(tempX) - (width/2)
									, gridyToScreen(graph.Y_MAX) + numberAndAxisSpace + numberInset + height);
						}
					} 
					tempX += graph.X_STEP;
				}
			}
		}
		
		if (graph.SHOW_NUMBERS){
			//draw y numbers on top, so the x lines don't draw over them
			for (Double d : yNumbers){
				if(graph.X_MAX <= 0){
					String ptText = null;
					if (d % 1 == 0 && d < Integer.MAX_VALUE &&
							d > Integer.MIN_VALUE){//number is int
						ptText = Integer.toString(d.intValue()); 
					}
					else{
						ptText = Double.toString(d);
					}
					width = g.getFontMetrics().stringWidth(ptText);
					g.setColor(Color.white);
					g.fillRect(gridxToScreen(graph.X_MAX) - width - 2 * numberInset - numberAndAxisSpace
							, gridyToScreen(d)- height/2 - numberInset,
							width + 2 * numberInset, height + 2 * numberInset);
					g.setColor(Color.black);
					g.drawString(ptText, gridxToScreen(graph.X_MAX) - width - numberAndAxisSpace - numberInset,
							gridyToScreen(d) + height/2 );
				}
				else if (graph.X_MIN >= 0){
					String ptText = null;
					if (d % 1 == 0 && d < Integer.MAX_VALUE &&
							d > Integer.MIN_VALUE){//number is int
						ptText = Integer.toString(d.intValue()); 
					}
					else{
						ptText = Double.toString(d);
					}
					width = g.getFontMetrics().stringWidth(ptText);
					g.setColor(Color.white);
					g.fillRect(gridxToScreen(graph.X_MIN) + numberInset + numberAndAxisSpace
							, gridyToScreen(d) - height/2 - numberInset,
							width + 2 * numberInset, height + 2 * numberInset);
					g.setColor(Color.black);
					g.drawString(ptText, gridxToScreen(graph.X_MIN) + numberAndAxisSpace + numberInset,
							gridyToScreen(d) + height/2 );
				}
				
				else if (graph.X_MIN <= 0 && graph.X_MAX >= 0) {
					String ptText = null;
					if (d % 1 == 0 && d < Integer.MAX_VALUE &&
							d > Integer.MIN_VALUE){//number is int
						ptText = Integer.toString(d.intValue()); 
					}
					else{
						ptText = Double.toString(d);
					}
					width = g.getFontMetrics().stringWidth(ptText);
					g.setColor(Color.white);
					g.fillRect(gridxToScreen(0) - width - 2 * numberInset - numberAndAxisSpace
							, gridyToScreen(d)- height/2 - numberInset,
							width + 2 * numberInset, height + 2 * numberInset);
					g.setColor(Color.black);
					g.drawString(ptText, gridxToScreen(0) - width - numberAndAxisSpace - numberInset,
							gridyToScreen(d) + height/2 );
				}
					
			}
		}

		if (graph.SHOW_AXIS){
			if (graph.X_MIN <= 0 && graph.X_MAX >= 0)
				drawLineSeg(0, graph.Y_MIN, 0, graph.Y_MAX, Color.BLACK, g);
	
			if (graph.Y_MIN <= 0 && graph.Y_MAX >= 0)
				drawLineSeg(graph.X_MIN, 0, graph.X_MAX, 0, Color.BLACK, g);
		}
		
		g.setFont(oldFont);
	}
}

