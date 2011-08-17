/*

 OpenCalc is a Graphing Calculator for the web.
 Copyright (C) 2009, 2010 Jason Altekruse

 This file is part of OpenCalc.

 OpenCalc is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OpenCalc is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with OpenCalc  If not, see <http://www.gnu.org/licenses/>.
 */

package gui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import math_rendering.RootNodeGraphic;
import math_rendering.CompleteProblem;
import math_rendering.NodeGraphic;

import tree.*;

public class ProblemPanel extends SubPanel {

	private static final long serialVersionUID = 1L;
	private MainApplet mainApp;
	private OCTextField entryLeft, entryRight, mousePos;
	
	private JTextArea selected;
	private OCButton renderButton;
	private ExpressionParser parser;
	private boolean justParsed;

    private SubPanel thisPanel;
    private JPanel render;
    private CompleteProblem problem;
    

	public ProblemPanel(final MainApplet currmainApp, TopLevelContainerOld topLevelComp) {
		super(topLevelComp);
		mainApp = currmainApp;
		thisPanel = this;
		justParsed = false;
		
		parser = mainApp.getParser();
		
		GridBagConstraints tCon = new GridBagConstraints();

		//add the line for expression entry
		entryLeft = new OCTextField(getTopLevelContainer(), true, 16, 5, 1, 0, 10, this, mainApp) {
			public void associatedAction() {
				try {
					justParsed = true;
					render.repaint();
				} catch (Exception e) {
					selected.setText("error with render: " + e.getMessage());
				}
			}
		};
		
		OCLabel equals = new OCLabel("=", 1, 1, 6, 10, this, mainApp);
		
		entryRight = new OCTextField(getTopLevelContainer(), true, 16, 5, 1, 7, 10, this, mainApp) {
			public void associatedAction() {
				try {
					justParsed = true;
					render.repaint();
				} catch (Exception e) {
					selected.setText("error with render: " + e.getMessage());
				}
			}
		};
		
		OCButton sqrt = new OCButton("sqrt", "Take sqrt of both sides", 1, 1, 13, 0, this, mainApp) {
			public void associatedAction() {
				try {
					problem.applyUnarytoBothSides(new UnaryExpression(Operator.SQRT));
					render.repaint();
				} catch (Exception e) {
					selected.setText("error with render: " + e.getMessage());
				}
			}
		};
		OCButton sin_1 = new OCButton("sin-1", "Take inverse sine of both sides", 1, 1, 13, 1, this, mainApp) {
			public void associatedAction() {
				try {
					problem.applyUnarytoBothSides(new UnaryExpression(Operator.INV_SIN));
					render.repaint();
				} catch (Exception e) { 
					selected.setText("error with render: " + e.getMessage());
				}
			}
		};
		OCButton move = new OCButton("move", "move term to other side", 1, 1, 13, 2, this, mainApp) {
			public void associatedAction() {
				try {
					problem.MoveTermToOtherSide();
					render.repaint();
				} catch (Exception e) {
					selected.setText("error with render: " + e.getMessage());
				}
			}
		};
		
		//a textfield to show the list of components once an expression has been clicked
		selected = new JTextArea(16,6);
		
		parser = mainApp.getParser();
		
		//set a default message for the terminal
		Font terminalFont = new Font("newFont", 1, 14);
		selected.setFont(terminalFont);
		selected.setEditable(false);
		selected.setText("Enter an expression in the line,\n" +
				"then click on the rendered expression\n" +
				"in the window to find what valueGraphics\n" +
				"each component belongs to.");
		JScrollPane termScrollPane = new JScrollPane(selected);
		termScrollPane.setWheelScrollingEnabled(true);
		
		//add a field to show the x and y position of a mouseclick
		mousePos = new OCTextField(getTopLevelContainer(), false, 16, 1, 1, 0, 11, this, mainApp) {};

		// create a button that does the same thing as hitting enter while in the entry textfield
		renderButton = new OCButton("render", "Render the current expression.", 1, 1, 13, 10, this, mainApp) {
			public void associatedAction() {
				try {
					justParsed = true;
					render.repaint();
										
					entryLeft.primaryAction();
					selected.setText("click on the rendered expression\n" +
							"in the window to find what valueGraphics\n" +
							"each component belongs to.");
				} catch (Exception e) {
					selected.setText("error with render: " + e.getMessage());
				}
			}
		};
		
		//create the panel to render stuff onto
		render = new JPanel(){
			public void paint(Graphics g){
				try {
						if (!entryLeft.getField().getText().equals(""))
					{
						render((Graphics2D)g);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					mainApp.makeErrorDialog(e.getMessage());
				}
			}
		};
		render.setMinimumSize(new Dimension(700, 700));
		JScrollPane renderScrollPane = new JScrollPane(render);
		
		render.setPreferredSize(new Dimension(400, 300));
		tCon.fill = GridBagConstraints.BOTH;
		tCon.weightx = 1;
		tCon.weighty = 1;
		tCon.gridx = 0;
		tCon.gridy = 0;
		tCon.gridheight = 10;
		tCon.gridwidth = 13;		
		this.add(renderScrollPane, tCon);
		//this.add(comp, tCon);
		
		tCon.fill = GridBagConstraints.BOTH;
		tCon.weightx = 1;
		tCon.weighty = .1;
		tCon.gridx = 0;
		tCon.gridy = 13;
		tCon.gridheight = 2;
		tCon.gridwidth = 13;
		this.add(termScrollPane, tCon);
		
		//this is just to prevent having to type input each time the applet is run
		entryLeft.getField().setText("5x^2-4x-4");
		entryRight.getField().setText("3x^2+7x+2");
		justParsed = true;
		render.repaint();
		
		render.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {

				render.requestFocus();
				if (problem == null){
					return;
				}
				int mouseX = e.getX();
				int mouseY = e.getY();
				selected.setText("");
				mousePos.getField().setText("x: " + mouseX + "   y: " + mouseY);
				problem.selectTerm(e.getX(), e.getY());
				render.repaint();
				
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
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		render.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_LEFT){
						if (problem.getCurrentCEG().getCursor().getValueGraphic() != null){
							problem.getCurrentCEG().getCursor().getValueGraphic().moveCursorWest();
						}
//						if (ceg.getSelectedVals().get(0).getWest() != null){						
//						ceg.getSelectedVals().get(0).setSelected(false);
//						ceg.getSelectedVals().set(0, ceg.getSelectedVals().get(0).getWest());
//						ceg.getSelectedVals().get(0).setSelected(true);
//						System.out.println(ceg.getSelectedVals().get(0).getValue());
//					}
					render.repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){

					if (problem.getCurrentCEG().getCursor().getValueGraphic() != null){
						problem.getCurrentCEG().getCursor().getValueGraphic().moveCursorEast();
					}
//					if (ceg.getSelectedVals().get(0).getEast() != null){
//						ceg.getSelectedVals().get(0).setSelected(false);
//						ceg.getSelectedVals().set(0, ceg.getSelectedVals().get(0).getEast());
//						ceg.getSelectedVals().get(0).setSelected(true);
//						System.out.println(ceg.getSelectedVals().get(0).getValue());
//					}
					render.repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_UP){
					if (problem.getCurrentCEG().getCursor().getValueGraphic() != null){
						System.out.println("renderPanel: north not null:" + 
								problem.getCurrentCEG().getCursor().getValueGraphic().getValue().toString());
						problem.getCurrentCEG().getCursor().getValueGraphic().moveCursorNorth();
					}
//					if (ceg.getSelectedVals().get(0).getNorth() != null){
//						ceg.getSelectedVals().get(0).setSelected(false);
//						ceg.getSelectedVals().set(0, ceg.getSelectedVals().get(0).getNorth());
//						ceg.getSelectedVals().get(0).setSelected(true);
//						System.out.println(ceg.getSelectedVals().get(0).getValue());
//					}
					render.repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN){
					if (problem.getCurrentCEG().getCursor().getValueGraphic() != null){
						problem.getCurrentCEG().getCursor().getValueGraphic().moveCursorSouth();
					}
//					if (problem.getCurrentCEG().getSelectedVals().get(0).getSouth() != null){
//						problem.getCurrentCEG().getSelectedVals().get(0).setSelected(false);
//						problem.getCurrentCEG().getSelectedVals().set(0, problem.getCurrentCEG().getSelectedVals().get(0).getSouth());
//						problem.getCurrentCEG().getSelectedVals().get(0).setSelected(true);
//						System.out.println(problem.getCurrentCEG().getSelectedVals().get(0).getValue());
//					}
					render.repaint();
				}
				System.out.println("current obj: " + problem.getCurrentCEG().getCursor().getValueGraphic());
				System.out.println("pos: " + problem.getCurrentCEG().getCursor().getPos());
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	

	public void render(Graphics2D g) throws ParseException, Exception{
		if (justParsed)
		{
			RootNodeGraphic ceg = new RootNodeGraphic(
					parser.ParseExpression(entryLeft.getField().getText() + "="
					+ entryRight.getField().getText()));
			ceg.generateExpressionGraphic((Graphics2D) g, 30, 40);
			problem = new CompleteProblem(ceg, 30, 40);
		}
		g.setColor(Color.white);
		g.fillRect(0, 0, render.getWidth(), render.getHeight());
		g.setColor(Color.black);
		if (problem != null){
			problem.setGraphics(g);
			problem.draw();
		}
		justParsed = false;
	}
	
	public OCTextField getEntryLine() {
		return entryLeft;
	}
	
}
