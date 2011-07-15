/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tree.Expression;
import gui.MainApplet;
import gui.OCButton;
import gui.OCLabel;
import gui.OCTextField;
import gui.SubPanel;
import gui.TopLevelContainerOld;
import gui.graph.Selection;

public class SelectionPanel extends SubPanel {

	private OCTextField startField , endField;
	private GraphWindow graphWindow;
	private MainApplet mainApp;
	private JPanel colorBox;
	private SubPanel thisPanel;
	
	public SelectionPanel(MainApplet mainApplet, TopLevelContainerOld topLevelComp, GraphWindow gw) {
		super(topLevelComp);
		this.mainApp = mainApplet;
		graphWindow = gw;
		thisPanel = this;
		this.setBorder(BorderFactory.createTitledBorder("Selection"));
		
		OCLabel start = new OCLabel("start:", 1, 1, 0, 1, this, mainApp);
		startField = new OCTextField(topLevelComp, true, 10, 1, 1, 1, 1, this, mainApp){
			
			public void associatedAction(){
				try{
					if (getField().getText().equals("") || getField().getText().equals("null")){
						graphWindow.getGraph().getSelection().setStart(Selection.EMPTY);
						graphWindow.repaint();
					}
					Expression temp = mainApp.getParser().ParseExpression(getField().getText());
					double x = temp.eval().toDec().getValue();
					if (x > graphWindow.getGraph().getSelection().getEnd()){
						getField().setText("null");
						endField.getField().setText("null");
						graphWindow.getGraph().getSelection().setStart(Selection.EMPTY);
						graphWindow.getGraph().getSelection().setEnd(Selection.EMPTY);
						graphWindow.repaint();
						return;
					}
					graphWindow.getGraph().getSelection().setStart(x);
					graphWindow.repaint();
				} catch (Exception e){
					//e.printStackTrace();
				}
//				graphWindow.getGraph().getSelection();
			}
		};
		
		OCLabel end = new OCLabel("end:", 1, 1, 2, 1, this, mainApp);
		endField = new OCTextField(topLevelComp, true, 10, 1, 1, 3, 1, this, mainApp){
			
			public void associatedAction(){
				try{
					if (getField().getText().equals("") || getField().getText().equals("null")){
						graphWindow.getGraph().getSelection().setEnd(Selection.EMPTY);
						graphWindow.repaint();
					}
					Expression temp = mainApp.getParser().ParseExpression(getField().getText());
					double x = temp.eval().toDec().getValue();
					if (x < graphWindow.getGraph().getSelection().getStart()){
						getField().setText("null");
						graphWindow.getGraph().getSelection().setEnd(Selection.EMPTY);
						graphWindow.repaint();
						return;
					}
					graphWindow.getGraph().getSelection().setEnd(x);
					graphWindow.repaint();
				} catch (Exception e){
					;
				}
				graphWindow.getGraph().getSelection();
			}
		};
		
		OCButton clearSelection = new OCButton(true, "clear", 1, 1, 4, 1, this, mainApp){
			public void associatedAction(){
				clearSelection();
			}
		};
		
//		OCLabel graphs = new OCLabel("graphs:", 1, 1, 0, 1, this, mainApp);
//		
//		colorBox = new JPanel() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			public void paint(Graphics g) {
//				int counter = 0;
//				g.setColor(Color.WHITE);
//				g.fillRect(0, 0, this.getWidth(), this.getHeight());
//				for (SingleGraph sg : graphWindow.getGraph().getGraphs()){
//					g.setColor(sg.getColor());
//					g.fillRect(14 * counter + 2, 4, 10, this.getHeight() - 8);
//					if (sg.hasFocus){
//						g.setColor(Color.BLACK);
//						g.drawRect(14 * counter + 2, 4, 10, this.getHeight() - 8);
//					}
//					counter++;
//				}
//				this.setSize(new Dimension(14,
//						14 * graphWindow.getGraph().getGraphs().size() + 2));
////				this.setPreferredSize(new Dimension(14,
////						14 * graphWindow.getGraph().getGraphs().size() + 2));
//			}
//		};
//		
//		JScrollPane graphScrollPane = new JScrollPane(colorBox);
//		graphScrollPane.setWheelScrollingEnabled(true);
//		
//		GridBagConstraints bCon = new GridBagConstraints();
//		bCon.fill = GridBagConstraints.BOTH;
//		bCon.weightx = 1;
//		bCon.weighty = 1;
//		bCon.gridheight = 1;
//		bCon.gridwidth = 1;
//		bCon.gridx = 1;
//		bCon.gridy = 1;
//		this.add(graphScrollPane, bCon);
		
		clearSelection();
		
	}

//	public void repaintGraphSelection(){
//		colorBox.repaint();
//	}
	
	public void clearSelection(){
		graphWindow.getGraph().getSelection().setStart(Selection.EMPTY);
		graphWindow.getGraph().getSelection().setEnd(Selection.EMPTY);
		startField.getField().setText("null");
		endField.getField().setText("null");
		graphWindow.repaint();
	}
	
	public void refreshFields(){
		startField.getField().setText( shortDouble(graphWindow.getGraph().getSelection().getStart()));
		if (graphWindow.getGraph().getSelection().getStart() == Selection.EMPTY){
			startField.getField().setText("null");
		}
		
		endField.getField().setText( shortDouble(graphWindow.getGraph().getSelection().getEnd()));
		if (graphWindow.getGraph().getSelection().getEnd() == Selection.EMPTY){
			endField.getField().setText("null");
		}
	}
	
	private String shortDouble(double d){
		String shorter = "" + (float) d;
		return shorter;
		
	}
	
	public void addValsToHistories(){
		startField.associatedAction();
		endField.associatedAction();
	}
	
	public OCTextField getStartField(){
		return startField;
	}
	
	public OCTextField getEndField(){
		return endField;
	}

	public void setGraphWindow(GraphWindow graphWindow) {
		this.graphWindow = graphWindow;
	}

	public GraphWindow getGraphWindow() {
		return graphWindow;
	}
}
