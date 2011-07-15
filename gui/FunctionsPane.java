/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import tree.EvalException;
import tree.ParseException;
import tree.ValueNotStoredException;

public class FunctionsPane extends SubPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7861065934708847322L;
	
	private Color[] graphColors = { Color.GREEN.darker(), Color.BLUE.darker(),
			Color.MAGENTA.darker(), Color.ORANGE.darker(), Color.cyan.darker(),
			Color.RED.darker(), Color.YELLOW.darker(), Color.PINK, Color.CYAN};
	
	private String[] graphTypes = {"cart", "pol"};
	private GraphOld graphOld;
	private Function[] functions;
	private MainApplet mainApp;
	
	public FunctionsPane(MainApplet currmainApp, TopLevelContainerOld topLevelComp){
		super(topLevelComp);
		mainApp = currmainApp;
		graphOld = mainApp.getGraphObj();
		
		//function objects(with data about equations, colors, trace values, etc.) are stored
		//in the graph itself, this list is grabbed from the graph here
		functions = graphOld.getFunctions();
		
		GridBagConstraints pCon = new GridBagConstraints();
		pCon.gridx = 0;
		pCon.gridy = 0;
		pCon.gridheight = 1;
		pCon.gridwidth = 6;
		pCon.fill = GridBagConstraints.BOTH;
		pCon.weightx = 1;
		pCon.weighty = 1;
		
		JPanel funcVars = new SubPanel(getTopLevelContainer());
		funcVars.setBorder(BorderFactory.createTitledBorder("Function Vars"));
		this.add(funcVars, pCon);
		OCButton x = new OCButton("x", 1, 1, 0, 0, funcVars, mainApp);
		OCButton theta = new OCButton("theta", 1, 1, 1, 0, funcVars, mainApp);
		
		for (int i = 0; i < graphColors.length; i++) {
			pCon.gridy = i + 1;
			pCon.insets = new Insets(6, 2, 6, 2);
			this.add(new GraphAttributesPanel(mainApp, getTopLevelContainer(), this, graphColors[i], new Function(mainApp.getParser())), pCon);
		}
		OCButton graph = new OCButton("Graph", 1, 1, 0, graphColors.length + 1, this, mainApp){
			public void associatedAction() throws ParseException, ValueNotStoredException, EvalException{
				mainApp.getCurrTextField().primaryAction();
			}
		};
	}
	
	public String[] getGraphTypes(){
		return graphTypes;
	}
}
