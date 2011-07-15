/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import gui.FuncCalcPanel;
import gui.Function;
import gui.FunctionsPane;
import gui.GraphAttributesPanel;
import gui.GraphOld;
import gui.MainApplet;
import gui.OCButton;
import gui.OCLabel;
import gui.OCTextField;
import gui.SubPanel;
import gui.TopLevelContainerOld;

public class SingleGraphAttributes extends SubPanel {

	/**mainApp
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OCTextField graphEntry;
	private MainApplet mainApp;
	private Color color;
	private SingleGraph singleGraph;
	private JComboBox graphType;
	private String[] graphTypes;
	private SingleGraphAttributes thisPanel;
	private JPanel colorBox;
	private FuncCalcPanel funcCalcPanel;
	private OCLabel depVar;
	private GraphWindow graphWindow;

	public SingleGraphAttributes(final MainApplet mainApp, TopLevelContainerOld topLevelComp,
			SingleGraphsPanel graphsPanel, SingleGraph sg, GraphWindow gw) {

		super(topLevelComp);
		singleGraph = sg;
		this.graphWindow = gw;
		thisPanel = this;
		this.mainApp = mainApp;
		graphTypes = graphsPanel.getGraphTypes();
		color = singleGraph.getColor();
		singleGraph = new GraphedCartFunction(mainApp.getParser(), 
				graphWindow.getGraph(), color);

		colorBox = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g) {
				g.setColor(color);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
		};
		
		graphType = new JComboBox(graphTypes);
		
		graphType.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e){
				int type = graphType.getSelectedIndex() + 1;
				
				if (type == 1){
					graphWindow.getGraph().removeSingleGraph(singleGraph);
					singleGraph = new GraphedCartFunction(mainApp.getParser(), 
							graphWindow.getGraph(), color);
					((GraphedCartFunction)singleGraph).setDependentVar("y");
					((GraphedCartFunction)singleGraph).setIndependentVar("x");
				}
				
				else if (type == 2){
					graphWindow.getGraph().removeSingleGraph(singleGraph);
					singleGraph = new GraphedPolarExpression(mainApp.getParser(), 
							graphWindow.getGraph(), color);
					((GraphedPolarExpression)singleGraph).setDependentVar("r");
					((GraphedPolarExpression)singleGraph).setIndependentVar("theta");
				}
				
				else if (type == 4){
//					func.setDependentVar("U_n");
//					func.setIndependentVar("n");
//					func.setGraphType(4);
				}
				thisPanel.paintComponent();
			}
		});
		
		graphEntry = new OCTextField(getTopLevelContainer(), true, 10, 5, 1, 4, 0, this, mainApp) {
			public void associatedAction() {
				graph();
			}
		};
		
		this.paintComponent();
	}
	
	private void graph(){
		if(!graphEntry.getField().getText().equals("") ){
			if (graphType.getSelectedIndex() == 0){
				if ( ! ((GraphedCartFunction)singleGraph).getFuncEqtn().equals("y=" + graphEntry.getField().getText()))
				{//make sure that the graph calculations are not removed because the user clicked on the field
					//the remove command below will take them off, and then restore the same graph
					graphWindow.getGraph().removeSingleGraph(singleGraph);
					graphWindow.repaint();
					singleGraph = new GraphedCartFunction(mainApp.getParser(), graphWindow.getGraph(), 
							"y=" + graphEntry.getField().getText(), "x", "y", true, color);
					graphWindow.getGraph().AddGraph(singleGraph);
					graphWindow.repaint();
					if (funcCalcPanel != null)
					{
						funcCalcPanel.refreshFields();
					}
				}
			}
			
			else if (graphType.getSelectedIndex() == 1){
				graphWindow.getGraph().removeSingleGraph(singleGraph);
				graphWindow.repaint();
				singleGraph = new GraphedPolarExpression(mainApp.getParser(), graphWindow.getGraph(), 
						"r=" + graphEntry.getField().getText(), "theta", "r", true, color);
				graphWindow.getGraph().AddGraph(singleGraph);
				graphWindow.repaint();
				if (funcCalcPanel != null)
				{
					funcCalcPanel.refreshFields();
				}
			}
		}
		else{
//			remove graph
			graphWindow.getGraph().removeSingleGraph(singleGraph);
			graphWindow.repaint();
		}
			
	}
	
	public void paintComponent(){
		this.removeAll();
		this.revalidate();
		
		
		GridBagConstraints pCon = new GridBagConstraints();
		
		pCon.fill = GridBagConstraints.BOTH;
		pCon.ipadx = 2;
		pCon.gridx = 1;
		this.add(colorBox, pCon);
		
		pCon.gridx = 2;
		this.add(graphType, pCon);
		
		depVar = new OCLabel(((GraphWithExpression)singleGraph).getDependentVar().getName() + " =", 1, 1, 3, 0, 
				.1, .1, this, mainApp);
		
		pCon.fill = GridBagConstraints.BOTH;
		pCon.gridx = 4;
		pCon.gridy = 0;
		pCon.gridheight = 1;
		pCon.gridwidth = 5;
		pCon.ipadx = 0;
		pCon.ipady = 0;
		pCon.weightx = 1;
		pCon.weighty = 1;
		this.add(graphEntry, pCon);
		
		this.revalidate();
		this.repaint();
	}

}
