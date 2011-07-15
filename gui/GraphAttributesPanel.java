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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GraphAttributesPanel extends SubPanel {

	/**mainApp
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OCTextField graphEntry;
	private GraphOld graphOld;
	private MainApplet mainApp;
	private Color color;
	private Function func;
	private JComboBox graphType;
	private String[] graphTypes;
	private OCLabel depVar;
	private OCButton indVar;
	private GraphAttributesPanel redundant;
	private JPanel colorBox;
	private JCheckBox graphing;
	private FuncCalcPanel funcCalcPanel;

	public GraphAttributesPanel(final MainApplet mainApp, TopLevelContainerOld topLevelComp, FunctionsPane fp, Color c, Function f) {
		// TODO Auto-generated constructor stub
		super(topLevelComp);
		func = f;
		redundant = this;
		this.mainApp = mainApp;
		graphTypes = fp.getGraphTypes();
		color = c;
		graphOld = mainApp.getGraphObj();

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
		
//		graphing = new JCheckBox();
//		graphing.setSelected(true);
//		graphing.addActionListener(new ActionListener(){
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				func.setGraphing( ! func.isGraphing() );
//				mainApp.getGraphObj().repaint();
//			}
//			
//		});
		
		graphType = new JComboBox(graphTypes);
		
		graphType.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e){
				int type = graphType.getSelectedIndex() + 1;
				
				
				if (type == 1){
					func.setDependentVar("y");
					func.setIndependentVar("x");
					func.setGraphType(1);
				}
				
				else if (type == 2){
					func.setDependentVar("r");
					func.setIndependentVar("theta");
					func.setGraphType(2);
				}
				
				else if (type == 4){
					func.setDependentVar("U_n");
					func.setIndependentVar("n");
					func.setGraphType(4);
				}
				redundant.paintComponent();
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
		if(!graphEntry.getField().getText().equals("")){
			func.setFuncEqtn(func.getDependentVar().getName() + "=" 
					+ graphEntry.getField().getText());
			func.setColor(color);
			func.setGraphing(true);
			graphOld.repaint();
			if (funcCalcPanel != null)
			{
				funcCalcPanel.refreshFields();
			}
		}
		else{
			func.setGraphing(false);
			func.setFuncEqtn("");
			graphOld.repaint();
		}
			
	}
	
	public void paintComponent(){
		this.removeAll();
		this.revalidate();
		
		
		GridBagConstraints pCon = new GridBagConstraints();
//		pCon.fill = GridBagConstraints.BOTH;
//		pCon.gridx = 0;
//		pCon.gridy = 0;
//		pCon.gridheight = 1;
//		pCon.gridwidth = 1;
//		pCon.ipadx = 1;
//		pCon.ipady = 3;
//		this.add(graphing, pCon);
		
		pCon.ipadx = 2;
		pCon.gridx = 1;
		this.add(colorBox, pCon);
		
		pCon.gridx = 2;
		this.add(graphType, pCon);
		
		depVar = new OCLabel(func.getDependentVar().getName() + " =", 1, 1, 3, 0, 
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
		
//		OCButton advanced = new OCButton("adv.", 1, 1, 10, 0, this, mainApp){
//			public void associatedAction() {
//				
//				if (funcCalcPanel == null || !funcCalcPanel.isShowing()){
//					OCFrame calcs = new OCFrame(mainApp, "Advanced Graph Options");
//					funcCalcPanel = new FuncCalcPanel(mainApp, calcs, func, color);
//					calcs.add(funcCalcPanel);
//					calcs.setPreferredSize(new Dimension(600, 160));
//					calcs.pack();
//					calcs.setVisible(true);
//				}
//			}
//		};
		
		this.revalidate();
		this.repaint();
	}

}
