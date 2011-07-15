/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import tree.*;
import tree.Number;

public class GraphOld extends SubPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double X_MAX, X_MIN, Y_MAX, Y_MIN, X_STEP, Y_STEP, X_PIXEL, Y_PIXEL,
		THETA_STEP, THETA_MIN,THETA_MAX, POL_STEP, POL_AX_STEP;
	public int X_SIZE, Y_SIZE, LINE_SIZE, LINE_SIZE_DEFAULT, NUM_FREQ, mouseX,
		mouseY, mouseRefX, mouseRefY, NUM_GRAPHS;
	private boolean refPoint;
	private MainApplet mainApp;
	private ExpressionParser parser;
	private VarStorage varList;
	private JPanel graph;
	// stuff to put in separate graph class
	private Function[] functions;

	public GraphOld(MainApplet currmainApp, TopLevelContainerOld topLevelComp, int xSize, int ySize) {
		super(topLevelComp);
		mainApp = currmainApp;
		parser = mainApp.getParser();
		varList = parser.getVarList();
		X_SIZE = xSize;
		Y_SIZE = ySize;
		NUM_GRAPHS = 6;
		NUM_FREQ = 2;
		LINE_SIZE = 2;
		LINE_SIZE_DEFAULT = 2;
		X_MAX = 10;
		X_MIN = -10;
		Y_MAX = 10;
		Y_MIN = -10;
		X_STEP = 1;
		Y_STEP = 1;
		POL_AX_STEP = 1;
		X_PIXEL = (X_MAX - X_MIN) / X_SIZE;
		Y_PIXEL = (Y_MAX - Y_MIN) / Y_SIZE;
		POL_STEP = Math.PI/360;

		functions = new Function[NUM_GRAPHS];
		
		for( int i = 0; i < NUM_GRAPHS; i++){
			functions[i] = new Function(parser);
		}
		
		graph = new SubPanel(getTopLevelContainer()){
			
			public void paint(Graphics g) {
				//System.out.println("repaint graph");
				g.setColor(Color.white);
				X_SIZE = graph.getSize().width;
				Y_SIZE = graph.getSize().height;
				g.fillRect(0, 0, X_SIZE, Y_SIZE);
				g.setColor(Color.BLACK);
				try{
					X_MIN = varList.getVarVal("xMin").toDec().getValue();
					X_MAX = varList.getVarVal("xMax").toDec().getValue();
					Y_MIN = varList.getVarVal("yMin").toDec().getValue();
					Y_MAX = varList.getVarVal("yMax").toDec().getValue();
					THETA_MIN = varList.getVarVal("thetaMin").toDec().getValue();
					THETA_MAX = varList.getVarVal("thetaMax").toDec().getValue();
					THETA_STEP = varList.getVarVal("thetaStep").toDec().getValue();
					
				}
				catch (EvalException e)
				{
					// TODO Auto-generated catch block
					//do something to show there was an error
				}
				
				X_PIXEL = (X_MAX - X_MIN) / X_SIZE;
				Y_PIXEL = (Y_MAX - Y_MIN) / Y_SIZE;

				try {
					drawAxis(g);
				} catch (EvalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				drawPolarAxis(g);
				
				Function f = null;
				for (int i = 0; i < NUM_GRAPHS; i++){
					f = functions[i];
					if (f != null){
						if (f.isGraphing()){
							if(f.getGraphType() == 1){
								graphCart(f, g);
							}
							else if(f.getGraphType() == 2){
								graphPolar(f, g);
							}
							else if(f.getGraphType() == 4){
								graphCart(f, g);
							}
						}
							
					}
				}
				if(refPoint)
					drawMousePlacement(g);
//				mainApp.getGridProps().refreshAttributes();
			}
		};

		graph.setBorder(BorderFactory.createTitledBorder(getBorder(), "graph"));
		
		graph.addMouseListener(new MouseListener(){
			private int xStart, yStart;
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				refPoint = true;
				mouseRefX = e.getX();
				mouseRefY= e.getY();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				refPoint = false;
				repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		graph.addMouseMotionListener(new MouseMotionListener(){
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
				try{
				varList.updateVarVal("xMin", (mouseX - e.getX())*X_PIXEL);
				varList.updateVarVal("xMax", (mouseX - e.getX())*X_PIXEL);
				varList.updateVarVal("yMin", (e.getY()- mouseY)*Y_PIXEL);
				varList.updateVarVal("yMax", (e.getY()- mouseY)*Y_PIXEL);
				} catch (EvalException ex){
					ex.printStackTrace();
				}
				repaint();
				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				mouseRefX = e.getX();
				mouseRefY= e.getY();
				repaint();
			}
			
		});
		
		this.addMouseWheelListener(new MouseWheelListener(){

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// TODO Auto-generated method stub
				try {
					zoom(100 - e.getWheelRotation() * 5);
				} catch (EvalException e1) {
					// TODO Auto-generated catch block
					//need to do something for errors
				}
			}
			
		});
		GridBagConstraints bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = 1;
		bCon.weighty = 1;
		bCon.gridheight = 7;
		bCon.gridwidth = 1;
		bCon.gridx = 0;
		bCon.gridy = 0;
		this.add(graph, bCon);
		this.setPreferredSize(new Dimension(xSize, ySize));
		
		JPanel props = new SubPanel(getTopLevelContainer());
		OCButton zoomPlus = new OCButton("Zoom+", 1, 1, 0, 0, props, mainApp){
			public void associatedAction(){
				try {
					zoom(120);
				} catch (EvalException e) {
					// TODO Auto-generated catch block
					//think of something to do here
				}
			}
		};
		
		OCButton zoomMinus = new OCButton("Zoom-", 1, 1, 1, 0, props, mainApp){
			public void associatedAction(){
				try {
					zoom(80);
				} catch (EvalException e) {
					// TODO Auto-generated catch block
					//need something here too
				}
			}
		};
		
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = .1;
		bCon.weighty = .1;
		bCon.gridheight = 1;
		bCon.gridwidth = 1;
		bCon.gridx = 0;
		bCon.gridy = 7;
		this.add(props, bCon);
	}
	 /**
	  * 
	  * @param rate
	 * @throws EvalException 
	  */
	public void zoom(double rate) throws EvalException{
		X_MIN = varList.getVarVal("xMin").toDec().getValue();
		X_MAX = varList.getVarVal("xMax").toDec().getValue();
		Y_MIN = varList.getVarVal("yMin").toDec().getValue();
		Y_MAX = varList.getVarVal("yMax").toDec().getValue();
		
		//hacked solution to prevent graphing, the auto-rescaling of the 
		//grid stops working after the numbers get too big
		if (X_MIN < -7E8 || X_MAX > 7E8 || Y_MIN < -7E8 || Y_MAX > 7E8){
			if (rate < 100)
			{//if the user is trying to zoom out farther, do nothing
				//there was an issue with crashing, I never did actually figure out what line
				//caused it, but this fixes it for now
				return;
			}
		}
		
		varList.updateVarVal("xMin", -1 * (X_MAX-X_MIN)*(100-rate)/100);
		varList.updateVarVal("xMax", (X_MAX-X_MIN)*(100-rate)/100);
		varList.updateVarVal("yMin", -1 * (Y_MAX-Y_MIN)*(100-rate)/100);
		varList.updateVarVal("yMax", (Y_MAX-Y_MIN)*(100-rate)/100);
		
		
		try {
			//these four statements are for resizing the grid after zooming
			if((X_MAX-X_MIN)/X_STEP >= 24)
			{
				//System.out.println("too many x");
				if ((X_MAX-X_MIN)/20 > 1)
				{
					//System.out.println("greater than 1");
					varList.setVarVal("xStep", new Decimal((int)(X_MAX-X_MIN)/20));
					X_STEP = varList.getVarVal("xStep").toDec().getValue();
					
	//				varList.setVarVal("yStep", new Decimal((int)(Y_MAX-Y_MIN)/20));
	//				Y_STEP = varList.getVarVal("yStep").toDec().getValue();
				}
				else
				{
					for (int i = 0; i < 25; i ++){
						if ((X_MAX-X_MIN)/20/Math.pow(.5, i) < .7){
							varList.setVarVal("xStep", new Decimal(Math.pow(.5, i)));
							X_STEP = varList.getVarVal("xStep").toDec().getValue();
						}
					}
				}
			}
			
			else if((X_MAX-X_MIN)/X_STEP <= 16){
				//System.out.println("too few x");
				if ((X_MAX-X_MIN)/20 > 1)
				{
					//System.out.println("greater than 1");
					varList.setVarVal("xStep", new Decimal((int)(X_MAX-X_MIN)/20));
					X_STEP = varList.getVarVal("xStep").toDec().getValue();
					
	//				varList.setVarVal("yStep", new Decimal((int)(Y_MAX-Y_MIN)/20));
	//				Y_STEP = varList.getVarVal("yStep").toDec().getValue();
				}
				else
				{
					//System.out.println("do loop to find dec");
					for (int i = 0; i < 25; i ++){
						if ((X_MAX-X_MIN)/20 < Math.pow(.5, i)){
							varList.setVarVal("xStep", new Decimal(Math.pow(.5, i)));
							X_STEP = varList.getVarVal("xStep").toDec().getValue();
						}
					}
				}
			}
			
			if((Y_MAX-Y_MIN)/Y_STEP >= 24){
				//System.out.println("too many y");
				varList.setVarVal("yStep", new Decimal((Y_MAX-Y_MIN)/20));
				if ((Y_MAX-Y_MIN)/20 > 1)
				{
	//				varList.setVarVal("xStep", new Decimal((int)(X_MAX-X_MIN)/20));
	//				X_STEP = varList.getVarVal("xStep").toDec().getValue();
					
					//System.out.println("greater than 1");
					varList.setVarVal("yStep", new Decimal((int)(Y_MAX-Y_MIN)/20));
					Y_STEP = varList.getVarVal("yStep").toDec().getValue();
				}
				else
				{
					for (int i = 0; i < 25; i ++){
						if ((Y_MAX-Y_MIN)/20/Math.pow(.5, i) < .7){
							varList.setVarVal("yStep", new Decimal(Math.pow(.5, i)));
							Y_STEP = varList.getVarVal("xStep").toDec().getValue();
						}
					}
				}
			}
			
			else if((Y_MAX-Y_MIN)/Y_STEP <= 16){
				//System.out.println("too few y");
				if ((Y_MAX-Y_MIN)/20 > 1)
				{
	//				varList.setVarVal("xStep", new Decimal((int)(X_MAX-X_MIN)/20));
	//				X_STEP = varList.getVarVal("xStep").toDec().getValue();
					//System.out.println("greater than 1");
					varList.setVarVal("yStep", new Decimal((int)(Y_MAX-Y_MIN)/20));
					Y_STEP = varList.getVarVal("yStep").toDec().getValue();
				}
				else
				{
					//System.out.println("do loop to find dec");
					for (int i = 0; i < 25
					; i ++){
						if ((Y_MAX-Y_MIN)/20 < Math.pow(.5, i)){
							varList.setVarVal("yStep", new Decimal(Math.pow(.5, i)));
							Y_STEP = varList.getVarVal("xStep").toDec().getValue();
						}
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		repaint();
		//System.out.println("finished zoom");
	}
	
	public Function[] getFunctions(){
		return functions;
	}

	public void setLineSize(int sizeInPixels) {
		LINE_SIZE = sizeInPixels;
	}

	private void drawMousePlacement(Graphics g){
		float currX = (float) ((mouseRefX * X_PIXEL) + X_MIN);
		float currY = (float) (Y_MAX - (mouseRefY * Y_PIXEL));
		String ptText = "(" + currX + ", " + currY + ")";
		int width = g.getFontMetrics().stringWidth(ptText);
		g.setColor(Color.white);
		g.fillRect( X_SIZE - width - 5, Y_SIZE - 20, width, 12);
		g.setColor(Color.black);
		g.drawString(ptText, X_SIZE - width - 5, Y_SIZE - 10);
	}
	
	private void ptOn(double a, double b, Graphics g) {
		if (a <= X_MAX && a >= X_MIN && b <= Y_MAX && b >= Y_MIN) {
			g.fillRect(roundDouble((a - X_MIN) / X_PIXEL - LINE_SIZE/2.0),
					roundDouble((Y_SIZE - LINE_SIZE/2.0) - (b - Y_MIN) / Y_PIXEL),
					LINE_SIZE, LINE_SIZE);
			// g.fillOval(roundDouble((a - X_MIN)/X_PIXEL), Y_SIZE -
			// roundDouble((b - Y_MIN)/Y_PIXEL),
			// 2*LINE_SIZE, 2*LINE_SIZE);
			// System.out.println("(" + a + "," + b + ")");
			// System.out.println("(" + roundDouble(a/X_PIXEL) + "," +
			// roundDouble(b/Y_PIXEL) + ")" + "\n");
		}
	}
	
	private int gridXPtToScreen(double x){
		return roundDouble((x - X_MIN) / X_PIXEL);
	}
	
	private int gridYPtToScreen(double y){
		return (Y_SIZE - LINE_SIZE) - roundDouble((y - Y_MIN) / Y_PIXEL);
	}
	
	private void polPtOn(double r, double theta, Graphics g){
		ptOn(r*Math.cos(theta), r*Math.sin(theta), g);
	}

	private void drawPolarAxis(Graphics g){
		
		setLineSize(1);
		int numCircles = 0; 
		int startNum = 0;
		
		if (X_MIN < Y_MIN)
		{
			startNum = (int) (((X_MIN % X_STEP) + 1) * X_STEP);
		}
		POL_AX_STEP = X_STEP;
		
		double maxYmagnitude = 0;
		if (Math.abs(Y_MIN) > Math.abs(Y_MAX)){
			maxYmagnitude = Math.abs(Y_MIN);
		}
		else{
			//don't need to worry about negative, if the magnitude of
			//the Y_MAX is greater, than it has to be positive, because
			//the Y_MIN is always less
			maxYmagnitude = Y_MAX;
		}
		
		double minYmagnitude = 0;
		if (Y_MAX < 0){
			minYmagnitude = Math.abs(Y_MAX);
		}
		else if (Y_MIN > 0){
			minYmagnitude = Y_MIN;
		}
		else{
			minYmagnitude = Double.MAX_VALUE;
		}
		
		double maxXmagnitude = 0;
		if (Math.abs(X_MIN) > Math.abs(X_MAX)){
			maxYmagnitude = Math.abs(X_MIN);
		}
		else{
			//don't need to worry about negative, if the magnitude of
			//the X_MAX is greater, than it has to be positive, because
			//the X_MIN is always less
			maxYmagnitude = X_MAX;
		}
		
		double minXmagnitude = 0;
		if (X_MAX < 0){
			minXmagnitude = X_MAX;
		}
		else if (X_MIN > 0){
			minXmagnitude = X_MIN;
		}
		else{
			minXmagnitude = Double.MAX_VALUE;
		}
		
		
		numCircles = (int) (Math.abs(X_MAX - X_MIN)/POL_AX_STEP);

		double closestNotchToOrigin = 0;
		
		if (minYmagnitude < minXmagnitude){
			closestNotchToOrigin = minYmagnitude;
		}
		else{
			closestNotchToOrigin = minXmagnitude;
		}
		
		//System.out.println("minX: " + minXmagnitude);
		//System.out.println("minY: " + minYmagnitude);
		//System.out.println("closestNotchToOrigin: " + closestNotchToOrigin);
		
		for (int i = 0 ; i <= numCircles; i++){
			double currT = 0;
			double lastX = (closestNotchToOrigin + i) * POL_AX_STEP;
//			System.out.println("lastX: " + lastX);
			double lastY = 0, currX, currY;
			for(int j = 1; j < 360; j++){
				currT += POL_STEP * 2;
				currX = (closestNotchToOrigin + i) * POL_AX_STEP * Math.cos(currT);
				currY = (closestNotchToOrigin + i) * POL_AX_STEP * Math.sin(currT);
				drawLineSeg(lastX, lastY, currX, currY, Color.gray, g);
				lastX = currX;
				lastY = currY;
			}
		}
	}

	private void drawAxis(Graphics g) throws EvalException {

		//System.out.println("axis");
		
		
		
		X_STEP = varList.getVarVal("xStep").toDec().getValue();
		Y_STEP = varList.getVarVal("yStep").toDec().getValue();
		
		
		// finds the fist factor of the Y_STEP on the screen
		// used to draw the first dash mark on the y-axis
		double tempY = (int) (Y_MIN / Y_STEP);
		tempY *= Y_STEP;
		int width;
		int height;

		if (X_MIN <= 0 && X_MAX >= 0) {
			while (tempY < Y_MAX) {
				setLineSize(1);
				drawLineSeg(X_MIN, tempY, X_MAX, tempY, Color.GRAY, g);
				setLineSize(LINE_SIZE_DEFAULT);
				drawLineSeg(2 * LINE_SIZE * X_PIXEL, tempY, -2 * LINE_SIZE		
						* X_PIXEL, tempY, Color.BLACK, g);
				if(tempY%(2 * Y_STEP) == 0 && tempY != 0){
					String ptText = Double.toString(tempY);
					width = g.getFontMetrics().stringWidth(ptText);
					height = g.getFontMetrics().getHeight();
					g.setColor(Color.white);
					g.fillRect(gridXPtToScreen(0) - width - 2*LINE_SIZE, gridYPtToScreen(tempY)- 2*LINE_SIZE, width, 11);
					g.setColor(Color.black);
					g.drawString(ptText, gridXPtToScreen(0) - width - 2*LINE_SIZE, gridYPtToScreen(tempY)+ 2*LINE_SIZE);
				} 
				tempY += Y_STEP;
			}
		} else {
			if(X_MIN >= 0){
				while (tempY < Y_MAX) {
					setLineSize(1);
					drawLineSeg(X_MIN, tempY, X_MAX, tempY, Color.GRAY, g);
					setLineSize(LINE_SIZE_DEFAULT);
					g.setColor(Color.BLACK);
					ptOn(X_MIN + 2 * X_PIXEL, tempY, g);
					if(tempY%(2* Y_STEP) == 0 && tempY != 0){
						String ptText = Double.toString(tempY);
						width = g.getFontMetrics().stringWidth(ptText);
						height = g.getFontMetrics().getHeight();
						g.setColor(Color.white);
						g.fillRect(gridXPtToScreen(X_MIN) + 8, gridYPtToScreen(tempY)- 4, width, 11);
						g.setColor(Color.black);
						g.drawString(ptText, gridXPtToScreen(X_MIN) + 8, gridYPtToScreen(tempY)+ 6);
					} 
					tempY += Y_STEP;
				}
			}
			else if(X_MAX <= 0){
				while (tempY < Y_MAX) {
					setLineSize(1);
					drawLineSeg(X_MIN, tempY, X_MAX, tempY, Color.GRAY, g);
					setLineSize(LINE_SIZE_DEFAULT);
					g.setColor(Color.BLACK);
					ptOn(X_MAX - 1 * X_PIXEL, tempY, g);
					if(tempY%(2* Y_STEP) == 0 && tempY != 0){
						String ptText = Double.toString(tempY);
						width = g.getFontMetrics().stringWidth(ptText);
						height = g.getFontMetrics().getHeight();
						g.setColor(Color.white);
						g.fillRect(gridXPtToScreen(X_MAX) - width - 4, gridYPtToScreen(tempY)- 4, width, 11);
						g.setColor(Color.black);
						g.drawString(ptText, gridXPtToScreen(X_MAX)- width - 4, gridYPtToScreen(tempY)+ 6);
					}
					tempY += Y_STEP;
					}
			}
		}
		
		// finds the fist factor of the X_STEP on the screen
		// used to draw the first dash mark on the x-axis
		double tempX = (int) (X_MIN / X_STEP);
		tempX *= X_STEP;
		height = g.getFontMetrics().getHeight();

		//make sure that the strings for the numbers on the axis will fit
		int tempWidth = g.getFontMetrics().stringWidth(Double.toString(tempX)) + 10;
		if(tempWidth > (int) ((X_MAX-X_MIN)/(X_STEP * NUM_FREQ))){
			NUM_FREQ = (int) (((X_MAX-X_MIN)/(X_STEP))/((X_SIZE)/tempWidth)) + 1;
		}
		if (Y_MIN <= 0 && Y_MAX >= 0) {
			while (tempX < X_MAX) {
				setLineSize(1);
				drawLineSeg(tempX, Y_MIN, tempX, Y_MAX, Color.GRAY, g);
				setLineSize(LINE_SIZE_DEFAULT);
				drawLineSeg(tempX, 2 * LINE_SIZE * Y_PIXEL, tempX, -2
						* LINE_SIZE * Y_PIXEL, Color.BLACK, g);
				
				if(tempX%(NUM_FREQ * X_STEP) == 0 && tempX != 0){
					String ptText = Double.toString(tempX);
					width = g.getFontMetrics().stringWidth(ptText);
					height = g.getFontMetrics().getHeight();
					g.setColor(Color.white);
					g.fillRect(gridXPtToScreen(tempX) - (width/2), gridYPtToScreen(0) - 18, width + 2, height - 4);
					g.setColor(Color.black);
					g.drawString(ptText, gridXPtToScreen(tempX) - (width/2), gridYPtToScreen(0) - 8);
				} 
				tempX += X_STEP;
			}
		} else {
			if( Y_MIN >= 0){
				while (tempX < X_MAX) {
					setLineSize(1);
					drawLineSeg(tempX, Y_MIN, tempX, Y_MAX, Color.GRAY, g);
					setLineSize(LINE_SIZE_DEFAULT);
					g.setColor(Color.BLACK);
					ptOn(tempX, Y_MIN, g);
					if(tempX%(NUM_FREQ * X_STEP) == 0 && tempX != 0){
						String ptText = Double.toString(tempX);
						width = g.getFontMetrics().stringWidth(ptText);
						height = g.getFontMetrics().getHeight();
						g.setColor(Color.white);
						g.fillRect(gridXPtToScreen(tempX) - (width/2), gridYPtToScreen(Y_MIN) - 18, width + 2, height - 4);
						g.setColor(Color.black);
						g.drawString(ptText, gridXPtToScreen(tempX) - (width/2), gridYPtToScreen(Y_MIN) - 8);
					} 
					tempX += X_STEP;
				}
			}
			if( Y_MAX <= 0){
				while (tempX < X_MAX) {
					setLineSize(1);
					drawLineSeg(tempX, Y_MIN, tempX, Y_MAX, Color.GRAY, g);
					setLineSize(LINE_SIZE_DEFAULT);
					g.setColor(Color.BLACK);
					ptOn(tempX, Y_MAX - 1 * LINE_SIZE * Y_PIXEL, g);
					if(tempX%(NUM_FREQ * X_STEP) == 0 && tempX != 0){
						String ptText = Double.toString(tempX);
						width = g.getFontMetrics().stringWidth(ptText);
						height = g.getFontMetrics().getHeight();
						g.setColor(Color.white);
						g.fillRect(gridXPtToScreen(tempX) - (width/2), gridYPtToScreen(Y_MAX) + 12, width + 2, height - 4);
						g.setColor(Color.black);
						g.drawString(ptText, gridXPtToScreen(tempX) - (width/2), gridYPtToScreen(Y_MAX) + 22);
					} 
					tempX += X_STEP;
				}
			}
		}

		if (X_MIN <= 0 && X_MAX >= 0)
			drawLineSeg(0, Y_MIN, 0, Y_MAX, Color.BLACK, g);

		if (Y_MIN <= 0 && Y_MAX >= 0)
			drawLineSeg(X_MIN, 0, X_MAX, 0, Color.BLACK, g);
	}

	protected void drawLineSeg(double x1, double y1, double x2, double y2,
			Color color, Graphics g) {
		
		//right now this ignores the LINE_SIZE currently set, but it draws much faster than before
		//I'll modify it to handle line thickness soon
//		g.setColor(color);
//		g.drawLine(gridXPtToScreen(x1), gridYPtToScreen(y1), gridXPtToScreen(x2), gridYPtToScreen(y2));
//		if (LINE_SIZE == 2){
//			g.drawLine(gridXPtToScreen(x1)+1, gridYPtToScreen(y1)+1, gridXPtToScreen(x2)+1, gridYPtToScreen(y2));
//		}
		if (Double.isNaN(y2) || Double.isNaN(y1)){
			return;
		}
		if (x1 > X_MAX && x2 > X_MAX){
			return;
		}
		if (x1 < X_MIN && x2 < X_MIN){
			return;
		}
		if (y1 > Y_MAX && y2 > Y_MAX){
			return;
		}
		if (y1 < Y_MIN && y2 < Y_MIN){
			return;
		}
		
		g.setColor(color);
		if (LINE_SIZE == 2){
			if (color.equals(Color.black)){
				g.setColor(Color.gray.brighter());
			}
			else{
				g.setColor(color.brighter());
			}
			
			if (x1 == x2){//the line is horizontal
				g.drawLine(gridXPtToScreen(x1) - 1, gridYPtToScreen(y1), gridXPtToScreen(x2) - 1, gridYPtToScreen(y2));
				g.drawLine(gridXPtToScreen(x1) + 1, gridYPtToScreen(y1), gridXPtToScreen(x2) + 1, gridYPtToScreen(y2));
			}
			else if (y1 == y2){
				g.drawLine(gridXPtToScreen(x1), gridYPtToScreen(y1) - 1, gridXPtToScreen(x2), gridYPtToScreen(y2) - 1);
				g.drawLine(gridXPtToScreen(x1), gridYPtToScreen(y1) + 1, gridXPtToScreen(x2), gridYPtToScreen(y2) + 1);
			}
			else{
				g.drawLine(gridXPtToScreen(x1), gridYPtToScreen(y1)-1, gridXPtToScreen(x2), gridYPtToScreen(y2)-1);
				g.drawLine(gridXPtToScreen(x1), gridYPtToScreen(y1)+1, gridXPtToScreen(x2), gridYPtToScreen(y2)+1);
				
				g.drawLine(gridXPtToScreen(x1) - 1, gridYPtToScreen(y1), gridXPtToScreen(x2) - 1, gridYPtToScreen(y2));
				g.drawLine(gridXPtToScreen(x1) + 1, gridYPtToScreen(y1), gridXPtToScreen(x2) + 1, gridYPtToScreen(y2));
			}
		}
		g.setColor(color);
		g.drawLine(gridXPtToScreen(x1), gridYPtToScreen(y1), gridXPtToScreen(x2), gridYPtToScreen(y2));
	}

	public int roundDouble(double a) {
		if (a % 1 >= .5)
			return (int) a + 1;
		else
			return (int) a;
	}

	public void graphCart(Function f, Graphics g) {
		//System.out.println("graphcart");
		String eqtn = f.getFuncEqtn();
		Var ind = f.getIndependentVar();
		Var dep = f.getDependentVar();
		
		//used to temporarily store the value stored in the independent and dependent vars,
		//this will allow it to be restored after graphing, so that if in the terminal a
		//value was assingned to x, it will not be overriden by the action of graphing
		
		Number xVal = ind.getValue();
		Number yVal = dep.getValue();
		Color color = f.getColor();
		boolean tracing = f.isTracingPt();
		double tracePt = f.getTraceVal();
		boolean isConnected = f.isConneted();
		boolean takeIntegral = f.isTakingIntegral();
		double a = f.getStartIntegral();
		double b = f.getEndIntegral();
		double derivative = f.getDerivative();
		boolean deriving = f.isDeriving();
		
		double lastX, lastY, currX, currY;
		g.setColor(color);
		try{
			Expression expression = parser.ParseExpression(eqtn);
			ind.setValue(new Decimal(X_MIN));
			expression.eval();
			lastX = ind.getValue().toDec().getValue();
			lastY = dep.getValue().toDec().getValue();
			for (int i = 1; i < X_SIZE; i += 2) {
				ind.updateValue(2 * X_PIXEL);
				expression.eval();
				currX = ind.getValue().toDec().getValue();
				currY = dep.getValue().toDec().getValue();
	
				if (isConnected){
					setLineSize(LINE_SIZE_DEFAULT);
					drawLineSeg(lastX, lastY, currX, currY, color, g);
				}
	
				if (takeIntegral) {
					if (currX >= a && currX <= b) {
						color = color.brighter();
						if (currY < Y_MAX && currY > Y_MAX)
							drawLineSeg(currX, 0, currX, Y_MAX, color, g);
						else if (currY < Y_MAX && currY > Y_MIN)
							drawLineSeg(currX, 0, currX, currY, color, g);
						else if (currY <= Y_MIN)
							drawLineSeg(currX, 0, currX, Y_MIN, color, g);
						else if (currY >= Y_MAX)
							drawLineSeg(currX, 0, currX, Y_MAX, color, g);
						else
							;// do nothing
						color = color.darker();
					}
				}
				lastX = ind.getValue().toDec().getValue();
				lastY = dep.getValue().toDec().getValue();
			}
			if (tracing) {
				g.setColor(Color.black);
				ind.setValue(new Decimal(tracePt));
				
				drawTracer(tracePt, expression.eval().toDec().getValue(), g);
			}
			
			//draws a tangent line that is always 20 pixels in length, this is broken, will fix later
			if (deriving)
			{//this will be redone later
				/*
				CURRCALC.parse(eqtn);
				double slope = CURRCALC.deriveAtPoint(derivative);
				ind.setValue(derivative);
				double depVal = CURRCALC.solve();
				double xChange = Math.sin(Math.atan(slope))*20;
				double yChange = 20 - xChange;
				if(slope > 1)
					yChange = -1*yChange;
				drawLineSeg(derivative - xChange*X_PIXEL, depVal - yChange*Y_PIXEL, 
						derivative + xChange*X_PIXEL, depVal + yChange*Y_PIXEL, new Color(255, 69, 0), g);
				*/
			}
			
			//restore the previous values of x and y
			ind.setValue(xVal);
			dep.setValue(yVal);
			
		}
		catch(Exception e)
		{
			System.out.println("error while drawing graph");
		}
		
	}
	
	public void graphPolar(Function f, Graphics g) {
		String eqtn = f.getFuncEqtn();
		Var ind = f.getIndependentVar();
		Var dep = f.getDependentVar();
		Color color = f.getColor();
		boolean tracing = f.isTracingPt();
		double tracePt = f.getTraceVal();
		boolean isConnected = f.isConneted();
		boolean takeIntegral = f.isTakingIntegral();
		double a = f.getStartIntegral();
		double b = f.getEndIntegral();
		int angleUnits = parser.getAngleUnits();
		setLineSize(LINE_SIZE_DEFAULT);
		
		double currR, currT, lastX, lastY, currX, currY;
		g.setColor(color);
		
		try{
			ind.setValue(new Decimal(THETA_MIN));
			Expression expression = parser.ParseExpression(eqtn);
			expression.eval();
			currR = dep.getValue().toDec().getValue();
			currT = ind.getValue().toDec().getValue();
			
			lastX = currR * Math.cos(currT);
			lastY = currR * Math.sin(currT);
			int numCalcs = (int)((THETA_MAX-THETA_MIN)/THETA_STEP);
			for (int i = 1; i <= numCalcs; i++) {
				ind.updateValue(THETA_STEP);
				expression.eval();
				currR = dep.getValue().toDec().getValue();
				currT = ind.getValue().toDec().getValue();
				
				if(angleUnits == 2)
					currT *= (Math.PI/180);
				if(angleUnits == 3)
					currT *= (Math.PI/200);
				currX = currR * Math.cos(currT);
				currY = currR * Math.sin(currT);
				//polPtOn(currT, currR, g);
				drawLineSeg(lastX, lastY, currX, currY, color, g);
				lastX = currX;
				lastY = currY;
			}
		}
		catch (Exception e){
			//do something
		}
	}

	public void drawTracer(double x, double y, Graphics g) {
		ptOn(x, y, g);
		ptOn(x + LINE_SIZE * X_PIXEL, y + LINE_SIZE * Y_PIXEL, g);
		ptOn(x + LINE_SIZE * X_PIXEL, y - LINE_SIZE * Y_PIXEL, g);
		ptOn(x - LINE_SIZE * X_PIXEL, y + LINE_SIZE * Y_PIXEL, g);
		ptOn(x - LINE_SIZE * X_PIXEL, y - LINE_SIZE * Y_PIXEL, g);
	}
}
