/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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

			else if((graph.X_MAX-graph.X_MIN)/graph.X_STEP <= 10){
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

			else if((graph.Y_MAX-graph.Y_MIN)/graph.Y_STEP <= 10){
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

		while (tempY <= graph.Y_MAX) 
		{//there are still more dashes to draw
			if (graph.SHOW_GRID){
				setLineSize(1);
				drawLineSeg(graph.X_MIN, tempY, graph.X_MAX, tempY, Color.GRAY, g);
			}
			if(tempY%(2 * graph.Y_STEP) == 0 && tempY != 0){
				yNumbers.add(tempY);
			}
			if (graph.SHOW_AXIS){
				setLineSize(graph.LINE_SIZE_DEFAULT);
				g.setColor(Color.BLACK);
				if (graph.X_MIN <= 0 && graph.X_MAX >= 0){
				drawLineSeg(2 * graph.LINE_SIZE * graph.X_PIXEL, tempY, -2 * graph.LINE_SIZE
						* graph.X_PIXEL, tempY, Color.BLACK, g);
				}
				else if (graph.X_MIN >= 0){
					drawLineSeg(0, tempY, 2 * graph.LINE_SIZE * graph.X_PIXEL, tempY, Color.BLACK, g);
				}
				else{
					ptOn(graph.X_MAX - graph.LINE_SIZE_DEFAULT * graph.X_PIXEL, tempY, g);
				}
			}

			tempY += graph.Y_STEP;
		}

		// finds the fist factor of the graph.X_STEP on the screen
		// used to draw the first dash mark on the x-axis
		double tempX = (int) (graph.X_MIN / graph.X_STEP);
		tempX *= graph.X_STEP;
		Vector<Double> xNumbers = new Vector<Double>();

		int tempWidth;

		graph.NUM_FREQ = 1;
		int tempFreq;
		do{
			String ptText = doubleToString(tempX, graph.X_STEP);
			tempWidth = g.getFontMetrics().stringWidth(ptText) + 4;
			if(tempWidth > (int) ((graph.X_MAX-graph.X_MIN)/(graph.X_STEP * graph.NUM_FREQ) * graph.X_PIXEL)){
				tempFreq = (int) Math.round(((graph.X_MAX-graph.X_MIN)
						/(graph.X_STEP))/((graph.X_SIZE)/tempWidth));
				tempFreq++;
				if (tempFreq > graph.NUM_FREQ){
					graph.NUM_FREQ = tempFreq;
				}
			}
			tempX += graph.X_STEP;
		}while (tempX <= graph.X_MAX);
		
		tempX = (int) (graph.X_MIN / graph.X_STEP);
		tempX *= graph.X_STEP;
		while (tempX <= graph.X_MAX) {
			if (graph.SHOW_GRID){
				setLineSize(1);
				drawLineSeg(tempX, graph.Y_MIN, tempX, graph.Y_MAX, Color.GRAY, g);
			}
			if(tempX%(graph.NUM_FREQ * graph.X_STEP) == 0 && tempX != 0)
			{//if its reached a place where a number should be drawn
				xNumbers.add(tempX);
			}

			if (graph.SHOW_AXIS){
				setLineSize(graph.LINE_SIZE_DEFAULT);
				g.setColor(Color.BLACK);
				if (graph.Y_MIN <= 0 && graph.Y_MAX >= 0) {
					drawLineSeg(tempX, 2 * graph.LINE_SIZE * graph.Y_PIXEL, tempX, -2
							* graph.LINE_SIZE * graph.Y_PIXEL, Color.BLACK, g);
				}
				else if( graph.Y_MIN >= 0){
					ptOn(tempX, graph.Y_MIN, g);
				}
				else{
					ptOn(tempX, graph.Y_MAX - 1 * graph.LINE_SIZE * graph.Y_PIXEL, g);
				}
			}
			tempX += graph.X_STEP;
		}

		if (graph.SHOW_NUMBERS){
			//draw y numbers on top, so the lines don't draw over them
			for (Double d : yNumbers){
				String ptText = doubleToString(d, graph.X_STEP);
				width = g.getFontMetrics().stringWidth(ptText);
				g.setColor(Color.white);
				if ( ! (gridyToScreen(d) - (height/2) - numberInset < graph.Y_PIC_ORIGIN ||
						gridyToScreen(d) + (height/2) + numberInset > graph.Y_PIC_ORIGIN + graph.Y_SIZE))
				{// if the number does not bleed off of the graph
					if(graph.X_MAX <= 0)
					{// the axis is not showing, numbers must be drawn on the left side
						g.fillRect(gridxToScreen(graph.X_MAX) - width - 2 * numberInset - numberAndAxisSpace
								, gridyToScreen(d)- height/2 - numberInset,
								width + 2 * numberInset, height + 2 * numberInset);
						g.setColor(Color.black);
						g.drawString(ptText, gridxToScreen(graph.X_MAX) - width - numberAndAxisSpace - numberInset,
								gridyToScreen(d) + height/2 );
					}
					else if (graph.X_MIN >= 0)
					{// the axis is not showing, numbers must be drawn on the right side
						g.fillRect(gridxToScreen(graph.X_MIN) + numberInset + numberAndAxisSpace
								, gridyToScreen(d) - height/2 - numberInset,
								width + 2 * numberInset, height + 2 * numberInset);
						g.setColor(Color.black);
						g.drawString(ptText, gridxToScreen(graph.X_MIN) + numberAndAxisSpace + numberInset,
								gridyToScreen(d) + height/2 );
					}
	
					else if (graph.X_MIN <= 0 && graph.X_MAX >= 0 &&
							! (gridxToScreen(0) - width - numberInset
									- numberAndAxisSpace < graph.X_PIC_ORIGIN))
					{// the axis is showing, default to drawing numbers on the left side of the axis
						g.fillRect(gridxToScreen(0) - width - 2 * numberInset - numberAndAxisSpace
								, gridyToScreen(d)- height/2 - numberInset,
								width + 2 * numberInset, height + 2 * numberInset);
						g.setColor(Color.black);
						g.drawString(ptText, gridxToScreen(0) - width - numberAndAxisSpace - numberInset,
								gridyToScreen(d) + height/2 );
					}
					else
					{// draw numbers on right side of axis
						g.fillRect(gridxToScreen(0) + numberInset + numberAndAxisSpace
								, gridyToScreen(d) - height/2 - numberInset,
								width + 2 * numberInset, height + 2 * numberInset);
						g.setColor(Color.black);
						g.drawString(ptText, gridxToScreen(0) + numberAndAxisSpace + numberInset,
								gridyToScreen(d) + height/2 );
					}
				}
			}

			//draw x numbers on top, so the lines don't draw over them
			for (Double d : xNumbers){
				String ptText = doubleToString(d, graph.X_STEP);
				width = g.getFontMetrics().stringWidth(ptText);
				g.setColor(Color.white);
				if ( ! (gridxToScreen(d) - (width/2) - numberInset < graph.X_PIC_ORIGIN ||
						gridxToScreen(d) + (width/2) + numberInset > graph.X_PIC_ORIGIN + graph.X_SIZE))
				{// if the number does not bleed off of the graph
					if(graph.Y_MAX <= 0)
					{// the axis is not showing, numbers must be drawn on the left side
						g.fillRect(gridxToScreen(d) - (width/2) - numberInset,
								gridyToScreen(graph.Y_MAX) + numberAndAxisSpace
								, width + numberInset * 2, height + 2 * numberInset);
						g.setColor(Color.black);
						g.drawString(ptText, gridxToScreen(d) - (width/2)
								, gridyToScreen(graph.Y_MAX) + numberAndAxisSpace + numberInset + height);
					}
					else if (graph.Y_MIN >= 0)
					{// the axis is not showing, numbers must be drawn on the right side
						g.fillRect(gridxToScreen(d) - (width/2) - numberInset,
								gridyToScreen(graph.Y_MIN) - height - numberInset * 2 - numberAndAxisSpace
								, width + numberInset * 2, height + 2 * numberInset);
						g.setColor(Color.black);
						g.drawString(ptText, gridxToScreen(d) - (width/2)
								, gridyToScreen(graph.Y_MIN) - numberAndAxisSpace - numberInset);
					}
					else if (graph.Y_MIN <= 0 && graph.Y_MAX >= 0 &&
							! (gridyToScreen(0) - height - numberInset
									- numberAndAxisSpace < graph.Y_PIC_ORIGIN) )
					{// the axis is showing, default to drawing numbers on the top of the axis
						g.fillRect(gridxToScreen(d) - (width/2) - numberInset,
								gridyToScreen(0) - height - numberAndAxisSpace - 2 * numberInset,
								width + numberInset * 2, height + 2 * numberInset);
						g.setColor(Color.black);
						g.drawString(ptText, gridxToScreen(d) - (width/2) ,
								gridyToScreen(0) - numberAndAxisSpace - numberInset);
					}
					else{
						g.fillRect(gridxToScreen(d) - (width/2) - numberInset,
								gridyToScreen(0) + numberAndAxisSpace,
								width + numberInset * 2, height + 2 * numberInset);
						g.setColor(Color.black);
						g.drawString(ptText, gridxToScreen(d) - (width/2) ,
								gridyToScreen(0) + numberAndAxisSpace + numberInset + height);
					}
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
	
	private boolean numbersClose(double num1, double num2){
		if ( Math.abs( num1 - num2) < .00001){
			return true;
		}
		return false;
	}
	
	private String doubleToString(double d, double round){
		String ptText;
		if ( numbersClose(d % round, 0)){
			d = d / round * round;
		}
		else if ( numbersClose( d % round, round)){
			d = ((d / round) + 1 ) * round;
		}
		if ( ((Double)d).toString().length() > 5){
			ptText = String.format("%.4G", d);
		}
		else{
			if ( (int) d == d){
				ptText = (int) d + ""; 
			}
			else{
				ptText = d + "";
			}
		}
		return ptText;
	}
}

