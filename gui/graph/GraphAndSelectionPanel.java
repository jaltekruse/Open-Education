/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui.graph;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import gui.MainApplet;
import gui.OCTextField;
import gui.SubPanel;
import gui.TopLevelContainerOld;

public class GraphAndSelectionPanel extends SubPanel {
	
	GraphWindow graphWindow;
	MainApplet mainApp;
	private GraphNavigationPanel navigation;
	private SelectionPanel selection;
	private OCTextField mousePt;
//	calculation;

	public GraphAndSelectionPanel(MainApplet mainApp, TopLevelContainerOld topLevelComp, GraphWindow gw) {
		super(topLevelComp);
		graphWindow = gw;
		this.mainApp = mainApp;
		navigation = new GraphNavigationPanel(mainApp, topLevelComp, gw);
		navigation.setSize(new Dimension(40, 50));
		selection = new SelectionPanel(mainApp, topLevelComp, gw);
		
		GridBagConstraints bCon = new GridBagConstraints();
		
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = 1;
		bCon.weighty = 1;
		bCon.gridheight = 1;
		bCon.gridwidth = 1;
		bCon.gridx = 0;
		bCon.gridy = 0;
		this.add(graphWindow, bCon);
		
		mousePt = new OCTextField(topLevelComp, false, 10, 4, 1, 0, 1, this, mainApp){};
		
		bCon.fill = GridBagConstraints.HORIZONTAL;
		bCon.weightx = .01;
		bCon.weighty = .01;
		bCon.gridheight = 1;
		bCon.gridwidth = 1;
		bCon.gridx = 0;
		bCon.gridy = 2;
		this.add(selection, bCon);
		
	}

	public GraphNavigationPanel getNavigation() {
		return navigation;
	}

	public SelectionPanel getSelection() {
		return selection;
	}
	
	public void updateMouse(double x, double y){
		mousePt.getField().setText("Mouse Position: ( " + (float) x + " , " + (float) y + " )" );
	}
}
