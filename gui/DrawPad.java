/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DrawPad extends SubPanel {

	public static int X_SIZE;
	public static int Y_SIZE;
	private boolean drawing, pen;
	private SubPanel draw, props;
	private OCButton undo, clear;
	private ArrayList<Stroke> strokes;
	
	MainApplet mainApp;
	
	public DrawPad(MainApplet currmainApp, TopLevelContainerOld topLevelComp, int xSize, int ySize) {
		super(topLevelComp);
		mainApp = currmainApp;
		X_SIZE = xSize;
		Y_SIZE = ySize;
		
		strokes = new ArrayList<Stroke>();
		GridBagConstraints bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = 1;
		bCon.weighty = 1;
		bCon.gridheight = 7;
		bCon.gridwidth = 1;
		bCon.gridx = 0;
		bCon.gridy = 0;
		drawing = true;
		pen = true;
		draw = new SubPanel(getTopLevelContainer()){
			public void paint(Graphics g){
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, draw.getSize().width, draw.getSize().height);
				if(strokes.size() > 0){
					g.setColor(Color.BLACK);
					Stroke currStroke;
					for(int i = 0; i < strokes.size();i++){
						currStroke = (Stroke) strokes.get(i);
						ArrayList<Integer> xPts = currStroke.getXPoints();
						ArrayList<Integer> yPts = currStroke.getYPoints();
						for(int j = 0; j < currStroke.getNumPoints() - 1; j++){
							g.drawLine(xPts.get(j), yPts.get(j), xPts.get(j+1), yPts.get(j+1));
						}
					}
				}
			}
		};
		this.add(draw, bCon);
		
		
		props = new SubPanel(getTopLevelContainer());
		undo = new OCButton("Undo", 1, 1, 0, 0, props, mainApp){
			public void associatedAction(){
				if(strokes.size() > 0)
					strokes.remove(strokes.size() - 1);
				draw.repaint();
			}
		};
		
		clear = new OCButton("Clear", 1, 1, 1, 0, props, mainApp){
			public void associatedAction(){
				strokes = new ArrayList<Stroke>();
				draw.repaint();
			}
		};
		
		bCon.weightx = .1;
		bCon.weighty = .1;
		bCon.gridheight = 1;
		bCon.gridwidth = 1;
		bCon.gridx = 0;
		bCon.gridy = 7;
		this.add(props, bCon);
		this.setPreferredSize(new Dimension(xSize, ySize));
		
		draw.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if (pen){
					setDrawing(true);
					strokes.add(new Stroke());
					((Stroke)strokes.get(strokes.size() - 1)).addPoint(e.getX(), e.getY());
					draw.repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				draw.repaint();
			}
			
		});
		
		draw.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				strokes.get(strokes.size() - 1).addPoint(e.getX(), e.getY());
				draw.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}

	public void setDrawing(boolean drawing) {
		this.drawing = drawing;
	}

	public boolean isDrawing() {
		return drawing;
	}
}
