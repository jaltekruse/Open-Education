/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui.graph;

import gui.MainApplet;
import gui.OCButton;
import gui.OCTextField;
import gui.OCTextWithValButton;
import gui.SubPanel;
import gui.TopLevelContainerOld;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextField;

import tree.*;
import tree.Number;

public class GridPropsPanel extends SubPanel {

	private MainApplet mainApp;
	private OCTextWithValButton xMin, xMax, yMin, yMax, xStep, yStep, thetaMin, thetaMax, thetaStep;
	private OCButton defaultGrid, zoom, zoomTrig;
	private OCTextField zoomRate;
	private SubPanel fields;
	private JScrollPane scroll;
	private ExpressionParser parser;
	private GraphWindow graphWindow;
	
	public GridPropsPanel(MainApplet currmainApp, TopLevelContainerOld topLevelComp, GraphWindow gw) 
			throws ParseException, ValueNotStoredException, EvalException {
		super(topLevelComp);
		graphWindow = gw;
		this.setLayout(new GridBagLayout());
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		mainApp = currmainApp;
		parser = mainApp.getParser();
		fields = new SubPanel(getTopLevelContainer());
		fields.setLayout(new GridBagLayout());
		fields.setPreferredSize(new Dimension(320, 300));
		scroll = new JScrollPane(fields);
		GridBagConstraints pCon = new GridBagConstraints();
		
		
		pCon.fill = GridBagConstraints.BOTH;
		pCon.weightx = 1;
		pCon.weighty = 1;
		pCon.gridx = 0;
		pCon.gridy = 0;
		this.add(scroll, pCon);

		/* OCTextWithValButton(String s, boolean editable, int length,
		 * 		int gridWidth, int gridHeight, int gridx, int gridy,
		 * 		JComponent comp, final NewCalc currmainApp)
		 */

		xMin = new OCTextWithValButton(getTopLevelContainer(), "xMin", true, 15, 3, 1, 0, 0, fields, mainApp) {
			public void associatedAction() {
				graphAttributeAction(getVarName(), getOCTextField());
			}
		};
		xMax = new OCTextWithValButton(getTopLevelContainer(), "xMax", true, 15, 3, 1, 0, 1, fields,
				mainApp) {
			public void associatedAction() {
				graphAttributeAction(getVarName(), getOCTextField());
			}
		};
		yMin = new OCTextWithValButton(getTopLevelContainer(), "yMin", true, 15, 3, 1, 0, 2, fields,
				mainApp) {
			public void associatedAction() {
				graphAttributeAction(getVarName(), getOCTextField());
			}
		};
		yMax = new OCTextWithValButton(getTopLevelContainer(), "yMax", true, 15, 3, 1, 0, 3, fields,
				mainApp) {
			public void associatedAction() {
				graphAttributeAction(getVarName(), getOCTextField());
			}
		};
		
		xStep = new OCTextWithValButton(getTopLevelContainer(), "xStep", true, 15, 3, 1, 0, 4, fields,
				mainApp) {
			public void associatedAction() {
				graphAttributeAction(getVarName(), getOCTextField());
			}
		};
		
		yStep = new OCTextWithValButton(getTopLevelContainer(), "yStep", true, 15, 3, 1, 0, 5, fields,
				mainApp) {
			public void associatedAction() {
				graphAttributeAction(getVarName(), getOCTextField());
			}
		};
		
		thetaMin = new OCTextWithValButton(getTopLevelContainer(), "thetaMin", true, 15, 3, 1, 0, 6, fields,
				mainApp) {
			public void associatedAction() {
				graphAttributeAction(getVarName(), getOCTextField());
			}
		};
		
		thetaMax = new OCTextWithValButton(getTopLevelContainer(), "thetaMax", true, 15, 3, 1, 0, 7, fields,
				mainApp) {
			public void associatedAction() {
				graphAttributeAction(getVarName(), getOCTextField());
			}
		};
		
		thetaStep = new OCTextWithValButton(getTopLevelContainer(), "thetaStep", true, 15, 3, 1, 0, 8, fields,
				mainApp) {
			public void associatedAction() {
				String currText = getOCTextField().getField().getText();
				if (!currText.equals(null) && !currText.equals("") && mainApp != null)
				{
					try
					{
						Expression v = mainApp.evalCalc(currText);
						//check here to make sure value isn't too small, will cause crashes
						//Permanent solution will be multi-threading, and handling graphing better
						getOCTextField().getField().setText(v.toString());
						mainApp.getParser().getVarList().setVarVal(getVarName(), (Number) v);
						mainApp.updateGraph();
					}
					catch(Exception e)
					{
						getOCTextField().getField().setText("error");
					}
				}
			}
		};
		
		zoomRate = new OCTextField(getTopLevelContainer(), true, 5, 2, 1, 0, 9, fields, mainApp){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public void associatedAction(){
				zoom();
			}
			
		};
		
		zoom = new OCButton("Zoom", 1, 1, 2, 9, fields, mainApp){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public void associatedAction() throws ParseException, ValueNotStoredException, EvalException{
				zoomRate.primaryAction();
			}
		};
		
		defaultGrid = new OCButton("default", 1, 2, 2, 10, fields, mainApp) {
			public void associatedAction() {
				mainApp.getParser().getVarList().setVarVal("xMin", new Fraction(-10,1));
				xMin.setText("-10");
				mainApp.getParser().getVarList().setVarVal("xMax", new Fraction(10,1));
				xMax.setText("10");
				mainApp.getParser().getVarList().setVarVal("yMin", new Fraction(-10,1));
				yMin.setText("-10");
				mainApp.getParser().getVarList().setVarVal("yMax", new Fraction(10,1));
				yMax.setText("10");
				mainApp.getParser().getVarList().setVarVal("xStep", new Fraction(1,1));
				yMax.setText("1");
				mainApp.getParser().getVarList().setVarVal("yStep", new Fraction(1,1));
				yMax.setText("1");
				mainApp.getParser().getVarList().setVarVal("thetaMin", new Fraction(0,1));
				thetaMin.setText("0");
				
				if(mainApp.getParser().getAngleUnits() == 1){
					mainApp.getParser().getVarList().setVarVal("thetaMax", new Decimal(2*Math.PI));
					thetaMax.setText("2pi");
					mainApp.getParser().getVarList().setVarVal("thetaStep", new Decimal(Math.PI/360));
					thetaStep.setText("2pi/360");
				}
				else if(mainApp.getParser().getAngleUnits() == 2){
					mainApp.getParser().getVarList().setVarVal("thetaMax", new Fraction(360,1));
					thetaMax.setText("360");
					mainApp.getParser().getVarList().setVarVal("thetaStep", new Fraction(3,1));
					thetaStep.setText("3");
				}
				
				else if(mainApp.getParser().getAngleUnits() == 3){
					mainApp.getParser().getVarList().setVarVal("thetaMax", new Fraction(400,1));
					thetaMax.setText("400");
					mainApp.getParser().getVarList().setVarVal("thetaStep", new Fraction(4,1));
					thetaStep.setText("4");
				}
				
				mainApp.updateGraph();
			}
		};
		
		zoomTrig = new OCButton("zoomTrig", 1, 2, 1, 10, fields, mainApp) {
			public void associatedAction() {
				mainApp.getParser().getVarList().setVarVal("xMin", new Decimal(-2*Math.PI));
				xMin.setText("-2pi");
				mainApp.getParser().getVarList().setVarVal("xMax", new Decimal(2*Math.PI));
				xMax.setText("2pi");
				mainApp.getParser().getVarList().setVarVal("yMin", new Fraction(-2, 1));
				yMin.setText("-2");
				mainApp.getParser().getVarList().setVarVal("yMax", new Fraction(2,1));
				yMax.setText("2");
				mainApp.getParser().getVarList().setVarVal("xStep", new Fraction(1,1));
				yMax.setText("1");
				mainApp.getParser().getVarList().setVarVal("yStep", new Fraction(1,1));
				yMax.setText("1");
				mainApp.getParser().getVarList().setVarVal("thetaMin", new Fraction(0,1));
				thetaMin.setText("0");
				if(mainApp.getParser().getAngleUnits() == 1){
					mainApp.getParser().getVarList().setVarVal("thetaMax", new Decimal(2*Math.PI));
					thetaMax.setText("2pi");
					mainApp.getParser().getVarList().setVarVal("thetaStep", new Decimal(Math.PI/360));
					thetaStep.setText("2pi/360");
				}
				else if(mainApp.getParser().getAngleUnits() == 2){
					mainApp.getParser().getVarList().setVarVal("thetaMax", new Fraction(360,1));
					thetaMax.setText("360");
					mainApp.getParser().getVarList().setVarVal("thetaStep", new Fraction(3,1));
					thetaStep.setText("3");
				}
				
				else if(mainApp.getParser().getAngleUnits() == 3){
					mainApp.getParser().getVarList().setVarVal("thetaMax", new Fraction(400,1));
					thetaMax.setText("400");
					mainApp.getParser().getVarList().setVarVal("thetaStep", new Fraction(4,1));
					thetaStep.setText("4");
				}
				
				
				
				mainApp.updateGraph();
			}
		};
		
		this.revalidate();
		refreshAttributes();
		this.repaint();
	}
	
	public void zoom(){
		String currText = zoomRate.getField().getText();
		String ansText = new String();
		if (!currText.equals(null) && !currText.equals("")
				&& mainApp != null)
		{
			JTextField currField = mainApp.getCurrTextField().getField();
			currField.setText("");
			try
			{
				graphWindow.getGraph().zoom(mainApp.getParser().ParseExpression(currText)
						.eval().toDec().getValue());
			} catch (EvalException e) {
				// TODO Auto-generated catch block
				//do something
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//do something
			}
		}
		refreshAttributes();
	}
	
	public void graphAttributeAction(String s, OCTextField field)
	{
		String currText = field.getField().getText();
		if (!currText.equals(null) && !currText.equals("") && mainApp != null)
		{
			try
			{
				Expression v = mainApp.evalCalc(currText);
				field.getField().setText(v.toString());
				mainApp.getParser().getVarList().setVarVal(s, (Number) v);
				mainApp.updateGraph();
			}
			catch(Exception e)
			{
					field.getField().setText("error");
			}
		}
	}
	
	public void refreshAttributes(){
		try{
			xMin.setText(mainApp.getParser().getVarList().getVarVal("xMin").toString());
			xMax.setText(mainApp.getParser().getVarList().getVarVal("xMax").toString());
			yMin.setText(mainApp.getParser().getVarList().getVarVal("yMin").toString());
			yMax.setText(mainApp.getParser().getVarList().getVarVal("yMax").toString());
			xStep.setText(mainApp.getParser().getVarList().getVarVal("xStep").toString());
			yStep.setText(mainApp.getParser().getVarList().getVarVal("yStep").toString());
			thetaMin.setText(mainApp.getParser().getVarList().getVarVal("thetaMin").toString());
			thetaMax.setText(mainApp.getParser().getVarList().getVarVal("thetaMax").toString());
			thetaStep.setText(mainApp.getParser().getVarList().getVarVal("thetaStep").toString());
		}
		catch(Exception e){
			System.out.println("error with refreshing graphProps panel");
		}
		this.revalidate();
		this.repaint();
	}
}
