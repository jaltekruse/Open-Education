/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import tree.ExpressionParser;

public class CalcFrame extends OCFrame {

	private static CalcPanel text;
	private ValStoragePanel varPanel, constPanel;
	private JScrollPane varScrollPane, constScrollPane;
	private JTabbedPane calcTabs, mathFunc;
	
	public CalcFrame(MainApplet mainApp) {
		super("Calculator");
		// TODO Auto-generated constructor stub
		text = new CalcPanel(mainApp, this);
		NumsAndOppsPanel Nums = new NumsAndOppsPanel(mainApp, this);
		
		ExpressionParser parser = mainApp.getParser();
		
		mathFunc = new JTabbedPane();
		varPanel = new ValStoragePanel(mainApp, this, parser.getVarList());
		parser.getVarList().setStorageGUI(varPanel);
		varScrollPane = new JScrollPane(varPanel);
		constPanel = new ValStoragePanel(mainApp, this, parser.getConstantList());
		parser.getConstantList().setStorageGUI(constPanel);
		constScrollPane = new JScrollPane(constPanel);

		mathFunc.add(Nums, "Math");
		mathFunc.add(varScrollPane, "Vars");
		mathFunc.add(constScrollPane, "Const");
		
		GridBagConstraints pCon = new GridBagConstraints();
		pCon.fill = GridBagConstraints.BOTH;
		pCon.insets = new Insets(2, 2, 2, 2);
		pCon.weightx = 1;
		pCon.weighty = 1;
		pCon.gridheight = 1;
		pCon.gridwidth = 1;
		pCon.weightx = 1;
		pCon.weighty = 1;
		pCon.gridx = 0;
		pCon.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.add(text, pCon);
		this.setVisible(true);
		
		pCon.weighty = .1;
		pCon.gridy = 1;
		pCon.fill = GridBagConstraints.HORIZONTAL;
		this.add(mathFunc, pCon);
	}

}
