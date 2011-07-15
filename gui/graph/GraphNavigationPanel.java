/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui.graph;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;

import tree.EvalException;
import gui.MainApplet;
import gui.OCButton;
import gui.TopLevelContainerOld;
import gui.SubPanel;

public class GraphNavigationPanel extends SubPanel {

	GraphWindow graphWindow;
	private MainApplet mainApp;
	
	public GraphNavigationPanel(MainApplet mainApp, TopLevelContainerOld topLevelComp, GraphWindow gw) {
		super(topLevelComp);
		graphWindow = gw;
		this.mainApp = mainApp;

		OCButton up = new OCButton("^", 2, 1, 1, 0, this, mainApp){
			public void associatedAction(){
				graphWindow.getGraph().shiftGraph(0, 30);
			}
		};
		OCButton left = new OCButton("<", 1, 2, 0, 1, this, mainApp){
			public void associatedAction(){
				graphWindow.getGraph().shiftGraph(-30, 0);
			}
		};
		
		OCButton right = new OCButton(">", 1, 2, 3, 1, this, mainApp){
			public void associatedAction(){
				graphWindow.getGraph().shiftGraph(30, 0);
			}
		};
		
		OCButton down = new OCButton("v", 2, 1, 1, 3, this, mainApp){
			public void associatedAction(){
				graphWindow.getGraph().shiftGraph(0, -30);
			}
		};
		
		OCButton zoomPlus = new OCButton("+", 1, 2, 1, 1, this, mainApp){
			public void associatedAction(){
				try {
					graphWindow.getGraph().zoom(120);
				} catch (EvalException e) {
					// TODO Auto-generated catch block
					//think of something to do here
				}
			}
		};
		
		OCButton zoomMinus = new OCButton("-", 1, 2, 2, 1, this, mainApp){
			public void associatedAction(){
				try {
					graphWindow.getGraph().zoom(80);
				} catch (EvalException e) {
					// TODO Auto-generated catch block
					//need something here too
				}
			}
		};
		
		OCButton properties = new OCButton("Properties", 1, 4, 4, 0, this, mainApp){
			public void associatedAction(){
				graphWindow.showGridPropsWindow();
			}
		};
		
		
	}

}
