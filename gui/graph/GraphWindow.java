/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui.graph;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import tree.EvalException;
import tree.ParseException;
import tree.ValueNotStoredException;
import tree.VarStorage;

import gui.MainApplet;
import gui.OCFrame;
import gui.SubPanel;
import gui.TopLevelContainerOld;

public class GraphWindow extends SubPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MainApplet mainApp;
	private int textHeight;
	private Graph graph;
	private JPanel graphPane;
	private SelectionGraphic selectionGraphic;
	private BufferedImage buffer;
	private int mouseX, mouseY;
	
	//used to keep track of how far off from the top left corner of a box a user clicked
	//allows for smoother dragging
	private int boxOffsetX, boxOffsetY;
	boolean refPoint, dragxSelectionRange, dragxSinglePt, justFinishedPic, isTimeToRedraw, movingxSelectionEnd,
			draggingGraph, dragDiskShowing, draggingInfoBox;
	private CalcInfoBox boxToDrag;
	private int heightInfoBar = 40;
	private Runnable current;
	private Object currObj;
	private GraphPanel graphPanel;
	private GridPropsPanel gridProps;
	
	public GraphWindow(MainApplet mainApp, TopLevelContainerOld topLevelComp, GraphPanel gp, int xSize, int ySize){
		super(topLevelComp);
		graphPanel = gp;
		this.setPreferredSize(new Dimension(xSize, ySize));
		this.mainApp = mainApp;
		graph = new Graph(this, mainApp);
		selectionGraphic = new SelectionGraphic(graph, Color.orange);
		dragxSelectionRange = false;
		movingxSelectionEnd = false;
		draggingInfoBox = false;
		
		try {
			gridProps = new GridPropsPanel(mainApp, getTopLevelContainer(), this);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValueNotStoredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EvalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		graph.setSelection(selectionGraphic);
		buffer = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		repaint();
		
		graphPane = new JPanel(){
			
			public void paint(Graphics g) {
				graph.repaint(g, getWidth(), getHeight());
				if (gridProps != null){
					gridProps.refreshAttributes();
				}
			}
		};
		
		graphPane.addMouseListener(new MouseListener(){
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				refPoint = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				mouseX = e.getX();
				mouseY = e.getY();
				refPoint = false;
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
				
				//check if the user clicked a CalcInfoBox
				CalcInfoBox tempBox = graph.getGraphCalcGraphics().boxContainsPt(e.getX(), e.getY());
				if (tempBox != null){
					graph.getDragDisk().setShowingDisk(false);
					dragxSinglePt = false;
					dragxSelectionRange = false;
					draggingGraph = false;
					draggingInfoBox = true;
					boxToDrag = tempBox;
					boxOffsetX = e.getX() - tempBox.getX();
					boxOffsetY = e.getY() - tempBox.getY();
					repaint();
					return;
				}
				
				//check if the user clicked on the dragDisk in the center of the screen
				if (Math.sqrt(Math.pow(e.getX() - graph.X_SIZE/2.0, 2) + 
						Math.pow(e.getY() - graph.Y_SIZE/2.0, 2)) <= graph.getDragDisk().getPixelRadius()){
					if (graph.getDragDisk() != null && graph.getDragDisk().isShowingDisk()){
						draggingGraph = true;
						dragxSelectionRange = false;
						dragxSinglePt = false;
						draggingInfoBox = false;
						mouseX = e.getX();
						mouseY = e.getY();
						return;
					}
				}
				
				//check if the user clicked on a function
				if (selectClosestGraph(e.getX(), e.getY())){
					return;
				}
				
				if (selectionGraphic.getStart() != Selection.EMPTY)
				{//if an x selection is currently being displayed, check if user clicked on the end of it
					if (Math.abs(e.getX() - selectionGraphic.gridxToScreen(selectionGraphic.getEnd())) < 5){
						movingxSelectionEnd = true;
						dragxSelectionRange = true;
						return;
					}
					else if (Math.abs(e.getX() - selectionGraphic.gridxToScreen(
							selectionGraphic.getStart())) < 5)
					{//check if user clicked on the start of the currently displayed selection
						if (selectionGraphic.getEnd() == Selection.EMPTY)
						{//check if the selection showing is just a single x value (i.e, has no end val)
							graph.getDragDisk().setShowingDisk(false);
							repaint();
							dragxSinglePt = true;
							dragxSelectionRange = false;
							draggingGraph = false;
							return;
						}
						else
						{//the selection has an end value
							movingxSelectionEnd = false;
							dragxSelectionRange = true;
							return;
						}
					}
				}
				else
				{//user has not clicked on any currently displayed elements, start a new x selection
					selectionGraphic.setStart(e.getX() * graph.X_PIXEL + graph.X_MIN);
					
					//snap if close to a line on the grid
					if (Math.abs((selectionGraphic.getStart() - Math.round(selectionGraphic.getStart() / 
							graph.X_STEP) * graph.X_STEP) / graph.X_PIXEL) < 8){
						selectionGraphic.setStart(Math.round(selectionGraphic.getStart() / graph.X_STEP)
								* graph.X_STEP);
					}
					
					selectionGraphic.setEnd(Selection.EMPTY);
					movingxSelectionEnd = true;
					dragxSelectionRange = true;
					graphPanel.getBottomToolbar().getSelection().refreshFields();
					repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				graph.getDragDisk().setShowingDisk(false);
				dragxSinglePt = false;
				dragxSelectionRange = false;
				draggingGraph = false;
				draggingInfoBox = false;
				repaint();
			}
			
		});
		
		graphPane.addMouseMotionListener(new MouseMotionListener(){
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				graphPanel.getBottomToolbar().updateMouse(
						graph.getSelection().screenxToGrid(e.getX()), graph.getSelection().screenyToGrid(e.getY()));
				
				if (draggingGraph){
					graph.shiftGraph(2 * (e.getX() - mouseX), 2 * (mouseY - e.getY()));
					mouseX = e.getX();
					mouseY = e.getY();
				}
				
				if (draggingInfoBox){
					boxToDrag.setX(e.getX() - boxOffsetX);
					boxToDrag.setY(e.getY() - boxOffsetY);
					graph.getGraphCalcGraphics().bringBoxIntoWindow(boxToDrag);
					repaint();
				}
				
				if (dragxSinglePt){
					selectionGraphic.setStart(selectionGraphic.screenxToGrid(e.getX()));
					if (Math.abs((selectionGraphic.getStart() - Math.round(selectionGraphic.getStart() / 
							graph.X_STEP) * graph.X_STEP) / graph.X_PIXEL) < 8)
					{//if within 7 pixels of a tick mark on grid, snap to it
						selectionGraphic.setStart(Math.round(selectionGraphic.getStart() / graph.X_STEP) * graph.X_STEP);
					}
				}
				
				if (dragxSelectionRange){
					if (selectionGraphic.getEnd() == Selection.EMPTY)
					{//only the start of the selection has been set, check if mouse has moved enough
						//before creating a range selection, prevents small mouse movements from turning
						//intended point selections into very small range selections
						if (Math.abs(selectionGraphic.gridxToScreen(selectionGraphic.getStart()) - e.getX()) < 3){
							return;
						}
					}
					
					if (movingxSelectionEnd){
						if (e.getX() < selectionGraphic.gridxToScreen(selectionGraphic.getStart())){
							selectionGraphic.setEnd(selectionGraphic.getStart());
							selectionGraphic.setStart(selectionGraphic.screenxToGrid(e.getX()));
							movingxSelectionEnd = !movingxSelectionEnd;
							graphPanel.getBottomToolbar().getSelection().refreshFields();
							return;
						}
						
						selectionGraphic.setEnd(selectionGraphic.screenxToGrid(e.getX()));
						if (Math.abs((selectionGraphic.getEnd() - Math.round(selectionGraphic.getEnd() / 
								graph.X_STEP) * graph.X_STEP) / graph.X_PIXEL) < 8){
							selectionGraphic.setEnd(Math.round(selectionGraphic.getEnd() / graph.X_STEP) * graph.X_STEP);
						}
					}
					else{
						
						if (e.getX() > selectionGraphic.gridxToScreen(selectionGraphic.getEnd())){
							selectionGraphic.setStart(selectionGraphic.getEnd());
							selectionGraphic.setEnd(selectionGraphic.screenxToGrid(e.getX()));
							movingxSelectionEnd = !movingxSelectionEnd;
							graphPanel.getBottomToolbar().getSelection().refreshFields();
							return;
						}
						
						selectionGraphic.setStart(selectionGraphic.screenxToGrid(e.getX()));
						if (Math.abs((selectionGraphic.getStart() - Math.round(selectionGraphic.getStart() / 
								graph.X_STEP) * graph.X_STEP) / graph.X_PIXEL) < 8)
						{//if within 7 pixels of a tick mark on grid, snap to it
							selectionGraphic.setStart(Math.round(selectionGraphic.getStart() / graph.X_STEP) * graph.X_STEP);
						}
					}
				}
				
				graphPanel.getBottomToolbar().getSelection().refreshFields();
				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				graphPanel.getBottomToolbar().updateMouse(
						graph.getSelection().screenxToGrid(e.getX()), graph.getSelection().screenyToGrid(e.getY()));
				if (Math.sqrt(Math.pow(e.getX() - graph.X_SIZE/2.0, 2) + 
						Math.pow(e.getY() - graph.Y_SIZE/2.0, 2)) <= graph.getDragDisk().getPixelRadius()){
					if ( ! graph.getDragDisk().isShowingDisk()){
						graph.getDragDisk().setShowingDisk(true);
						repaint();	
					}	
				}
				else{
					if (graph.getDragDisk().isShowingDisk()){
						graph.getDragDisk().setShowingDisk(false);
						repaint();
					}
				}
			}	
		});
		
		graphPane.addMouseWheelListener(new MouseWheelListener(){

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				try {
					graph.zoom(100 - e.getWheelRotation() * 5);
					Runnable timer = new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							repaint();
						}
						
					};
					javax.swing.SwingUtilities.invokeLater(timer);
				} catch (EvalException e1) {
					//TODO Auto-generated catch block
					//need to do something for errors
				}
				
				graphPanel.getBottomToolbar().updateMouse(
						graph.getSelection().screenxToGrid(e.getX()), graph.getSelection().screenyToGrid(e.getY()));
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
		this.add(graphPane, bCon);
		this.setPreferredSize(new Dimension(xSize, ySize));
	}
	
	public Graph getGraph(){
		return graph;
	}
	
	public void showGridPropsWindow(){
		if (gridProps == null || !gridProps.isShowing()){
			OCFrame grid = new OCFrame("Grid Properties");
			grid.add(gridProps);
			grid.setPreferredSize(new Dimension(350, 425));
			grid.pack();
			grid.setVisible(true);
		}
	}
	
	public boolean hasRangeSelection(){
		if (selectionGraphic.getStart() != Selection.EMPTY 
				&& selectionGraphic.getEnd() != Selection.EMPTY){
			return true;
		}
		return false;
	}
	
	public boolean selectClosestGraph(int x, int y){
		
		if (graph.getGraphs().size() > 0){
			Vector<Double> shortestDistToGraphs = new Vector<Double>();
			
			for (int i = 0; i < graph.getGraphs().size(); i++){
				shortestDistToGraphs.add(Double.MAX_VALUE);
			}
			SingleGraph sg;
			for (int i = 0 ; i < graph.getGraphs().size(); i++ )
			{//cycle through all of the graphs on the screen
				sg = graph.getGraphs().get(i);
				shortestDistToGraphs.set(graph.getGraphs().indexOf(sg), Double.MAX_VALUE);
				Vector<Integer> xVals = sg.getxVals();
				Vector<Integer> yVals = sg.getyVals();
				if (sg instanceof GraphWithExpression && xVals.size() > 0 && yVals.size() > 0)
				{//if the current graph is associated with a function
					//and if there are points used to draw the graph currently being displayed
					//some graphs will not be showing any points if zooming in on parts of the screen
					double tempDist;
					int lastX = xVals.get(0), lastY = yVals.get(0);
					for (int j = 1; j < xVals.size(); j++)
					{//go through all of the points that were used to draw the graph
						//determine the shortest distance from the click to any of them
						double distBetweenLast2Pts = Math.sqrt(Math.pow(xVals.get(j) - lastX, 2) + 
								Math.pow(yVals.get(j) - lastY,2));
						int distanceThreshold = 3;
						if (distBetweenLast2Pts > distanceThreshold)
						{//the distance between this point and the last one checked is greater than 3
							//some points between the two must be checked
							if ((xVals.get(j) - lastX) == 0){
								continue;
							}
							double slope = (yVals.get(j) - lastY)/(xVals.get(j) - lastX);
//							if (Math.abs(slope) > 50)
//							{//this segment of the line is almost vertical, it doesn't need
//								//all of its segments searched
//								
//								//the y value to be used to determine the distance from this point
//								//if the y value from the click is above the entire segment
//								//the maximum y is used, if it is below the lower point, the smaller y is used
//								double yValOnSegment;
//								double largerY, smallerY;
//								if (lastY > yVals.get(j)){
//									largerY = lastY;
//									smallerY = yVals.get(j);
//								}
//								else{
//									largerY = yVals.get(j);
//									smallerY = lastY;
//								}
//								if (y < smallerY){
//									yValOnSegment = smallerY;
//								}
//								else if (y > largerY){
//									yValOnSegment = largerY;
//								}
//								else{
//									yValOnSegment = y;
//								}
//								
//								tempDist = Math.sqrt(Math.pow(xVals.get(j) - x, 2) 
//										+ Math.pow(yValOnSegment - y, 2)/2);
//								if (tempDist < shortestDistToGraphs.get(i)){
//									shortestDistToGraphs.set(i, tempDist);
//								}
//								lastX = xVals.get(j);
//								lastY = yVals.get(j);
//								continue;
//							}
							double xStep = Math.abs(distanceThreshold/slope);
							double currSubX = lastX;
							double currSubY;
							do{
								currSubX += xStep;
								currSubY = lastY + (currSubX - lastX) * slope;
								tempDist = Math.sqrt(Math.pow(currSubX - x,2) 
										+ Math.pow(currSubY - y, 2));
								if (tempDist < shortestDistToGraphs.get(i)){
									shortestDistToGraphs.set(i, tempDist);
								}
							} while (currSubX < xVals.get(j));
						}
						tempDist = Math.sqrt(Math.pow(xVals.get(j) - x,2) 
								+ Math.pow(yVals.get(j) - y, 2));
						
						if (tempDist < shortestDistToGraphs.get(i)){
							shortestDistToGraphs.set(i, tempDist);
						}
						lastX = xVals.get(j);
						lastY = yVals.get(j);
					}
				}
			}
			
			double shortestDist = Double.MAX_VALUE;
			int indexShortestDist = 0;
			
//			de-selects all of the graphs, currently set to let multiple be selected at once
//			for (SingleGraph s : graph.getGraphs()){
//				s.setfocus(false);
//			}
			
			for (int i = 0; i < shortestDistToGraphs.size(); i++){
				if (shortestDistToGraphs.get(i) < shortestDist){
					shortestDist = shortestDistToGraphs.get(i);
					indexShortestDist = i;
				}
			}
			if (shortestDist < 10){
				
				//flip focus from on to off with each click on a graph
				graph.getGraphs().get(indexShortestDist).setfocus( 
						! graph.getGraphs().get(indexShortestDist).hasFocus);
				
				if (graph.getGraphs().get(indexShortestDist).hasFocus)
				{//if the graph gain focus bring it and its integrations to the front
					//add this graph to the end of the list so it graphs last and shows on the top
					graph.getGraphs().add(graph.getGraphs().remove(indexShortestDist));
					
					//move this graph's integrals to the top of the display
					graph.getGraphCalcGraphics().bringGraphToFront(
							graph.getGraphs().get(graph.getGraphs().size() - 1));
				}
				
				repaint();
//				if (graphPanel.getBottomToolbar().getSelection() != null){
//					graphPanel.getBottomToolbar().getSelection().repaintGraphSelection();
//				}
				return true;
			}
			else{
				for (SingleGraph s : graph.getGraphs()){
					s.setfocus(false);
				}
			}
		}
		return false;
	}
	
}
