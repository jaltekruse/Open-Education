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
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tree.Decimal;
import tree.EvalException;
import tree.ParseException;
import tree.Expression;
import tree.Number;
import tree.ValueNotStoredException;

public class FuncCalcPanel extends SubPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainApplet mainApp;
	private Function func;
	private GraphOld graphOld;
	private OCButton trace, integrate, graphButton, derive;
	private OCTextField pt2Trace, tracePtVal, startInt, endInt, intVal, slopeVal, indVarVal;
	private OCLabel indVar, depVar, start, end, intApprox, slopeApprox, spacer;
	private Color color;
	private SubPanel  traceBox, intBox, deriveBox;
	private GlassPane glassPanel;

	
	public FuncCalcPanel(MainApplet currmainApp, TopLevelContainerOld comp, Function f, Color c){
		super(comp);
		mainApp = currmainApp;
		graphOld = mainApp.getGraphObj();
		func = f;
		color = c;
		glassPanel = new GlassPane(mainApp, comp);
		this.setLayout(new GridBagLayout());
		
		refreshFields();
	}
	
	/**
	 * Need to rewrite this for the tree
	 */
	public void integrate(){
		if ("".equals(func.getFuncEqtn())) {
			intVal.getField().setText("no eqtn");
			intVal.getField().setText("no eqtn");
		}
		else if (!startInt.getField().getText().equals("") && !endInt.getField().getText().equals("")){
			double a = 0, b = 0;
			try{
				a = mainApp.getParser().ParseExpression(startInt.getField().getText()).eval().toDec().getValue();
				b = mainApp.getParser().ParseExpression(endInt.getField().getText()).eval().toDec().getValue();
			}catch (Exception ex){
				intVal.getField().setText("error");
				return;
			}
			func.setIntegral(a, b);
			startInt.getField().setText("" + a);
			endInt.getField().setText("" + b);
			graphOld.repaint();
			//mainApp.getBasicmainApp().parse(func.getFuncEqtn());
			String integral = new String();
			//integral += (float) mainApp.getBasicmainApp().integrate(a, b);
			intVal.getField().setText(integral);
		}
		else{
			func.setIsTakingIntegral(false);
			intVal.getField().setText("");
			graphOld.repaint();
		}
	}
	
	/**
	 * Need to rewrite this for tree.
	 * @throws EvalException 
	 * @throws ParseException 
	 */
	public void derive() throws EvalException, ParseException{
		if ("".equals(func.getFuncEqtn())) {
			slopeVal.getField().setText("no eqtn");
		}
		else if (!indVarVal.getField().getText().equals("")){
			System.out.println("got to int");
			Expression temp = mainApp.getParser().ParseExpression((indVarVal.getField().getText()));
			double x = temp.eval().toDec().getValue();
			func.setDerivative(x);
			func.setDeriving(true);
			graphOld.repaint();
			String derivative = new String();
			slopeVal.getField().setText(mainApp.getParser().ParseExpression(
					func.getFuncEqtn()).deriveAtPt(x, "x", "y").toString());
		}
		else{
			func.setDeriving(false);
			indVarVal.getField().setText("");
			graphOld.repaint();
		}
	}

	public void tracePt(){
		if (pt2Trace.getField().getText().equals("")) {
			func.setTracingPt(false);
			tracePtVal.getField().setText("");
		}
		else {
			if ("".equals(func.getFuncEqtn())) {
				pt2Trace.getField().setText("no eqtn");
				tracePtVal.getField().setText("error");
			}
			else {
				try{
					Expression xAns = mainApp.evalCalc(pt2Trace.getField().getText());
					if (!"error".equals(xAns)) {
						double xVal = xAns.toDec().getValue();
						func.setTrace(xVal);
						pt2Trace.getField().setText(xAns.toString());
						func.getIndependentVar().setValue(new Decimal(xVal));
						Expression yVal = mainApp.getParser().ParseExpression(func.getFuncEqtn()).eval();
						func.getDependentVar().setValue((Number) yVal);
						tracePtVal.getField().setText(yVal.toString());
					}
				}
				catch (Exception e) {
					pt2Trace.getField().setText("error");
					System.out.println(e.getMessage());
				}
			}
		}
		graphOld.repaint();
	}
	
	public void refreshFields(){
		
		
		this.removeAll();
		GridBagConstraints pCon = new GridBagConstraints();
		traceBox = new SubPanel(getTopLevelContainer());
		intBox = new SubPanel(getTopLevelContainer());
		deriveBox = new SubPanel(getTopLevelContainer());
		
		GridBagConstraints bCon = new GridBagConstraints();
		
		bCon.fill = GridBagConstraints.BOTH;
		bCon.gridx = 0;
		bCon.gridy = 0;
		bCon.gridheight = 1;
		bCon.gridwidth = 10;
		bCon.weightx = 1;
		bCon.weighty = 1;
		this.add(traceBox, bCon);
		
		bCon.gridy = 1;
		this.add(intBox, bCon);
		
		bCon.gridy = 2;
		this.add(deriveBox, bCon);
		
		JPanel colorBox = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g) {
				g.setColor(color);
				g.fillRect(0, 0, 30,30);
			}
		};
		colorBox.setPreferredSize(new Dimension(30,30));
		
		pCon.gridx = 0;
		pCon.gridy = 0;
		pCon.gridheight = 1;
		pCon.gridwidth = 1;
		pCon.ipadx = 3;
		pCon.ipady = 3;
		traceBox.add(colorBox, pCon);
		
		indVar = new OCLabel(func.getIndependentVar().getName() + ":", 1, 1, 1, 0, traceBox, mainApp);
		pt2Trace = new OCTextField(getTopLevelContainer(), true, 9, 1, 1, 2, 0, traceBox, mainApp) {
			public void associatedAction() {
				tracePt();
			}
		};
		
		trace = new OCButton("Trace", 1, 1, 3, 0, traceBox, mainApp){
			public void associatedAction() throws ParseException, ValueNotStoredException, EvalException {
				pt2Trace.primaryAction();
			}
		};
		
		depVar = new OCLabel(func.getDependentVar().getName() + ":", 1, 1, 4, 0, traceBox, mainApp);
		tracePtVal = new OCTextField(getTopLevelContainer(), false, 9, 1, 1, 5, 0, traceBox, mainApp);
		
		if(func.getGraphType() == 1){
			start = new OCLabel("start:", 1, 1, 0, 0, intBox, mainApp);
			startInt = new OCTextField(getTopLevelContainer(), true, 4, 1, 1, 1, 0, intBox, mainApp){
				public void associatedAction(){
				}
			};
			end = new OCLabel("end:", 1, 1, 2, 0, intBox, mainApp);
			endInt = new OCTextField(getTopLevelContainer(), true, 4, 1, 1, 3, 0, intBox, mainApp){
				public void associatedAction(){
				}
			};
			
			integrate = new OCButton("Integrate", 1, 1, 4, 0, intBox, mainApp){
				public void associatedAction() throws ParseException, ValueNotStoredException, EvalException{
					startInt.primaryAction();
					endInt.primaryAction();
					integrate();
				}
			};
			
			intApprox = new OCLabel("Int:", 1, 1, 5, 0, intBox, mainApp);
			intVal = new OCTextField(getTopLevelContainer(), false, 9, 1, 1, 6, 0, intBox, mainApp);
			
			
			indVar = new OCLabel(func.getIndependentVar().getName() + ":", 1, 1, 0, 0, deriveBox, mainApp);
			indVarVal = new OCTextField(getTopLevelContainer(), true, 4, 1, 1, 1, 0, deriveBox, mainApp){
				public void associatedAction(){
					try {
						derive();
					} catch (EvalException e) {
						// TODO Auto-generated catch block
						;
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						;
					}
				}
			};
			
			derive = new OCButton("Derive", 1, 1, 2, 0, deriveBox, mainApp){
				public void associatedAction() throws ParseException, ValueNotStoredException, EvalException{
					indVarVal.primaryAction();
				}
			};
			
			slopeApprox = new OCLabel("Slope:", 1, 1, 3, 0, deriveBox, mainApp);
			slopeVal = new OCTextField(getTopLevelContainer(), false, 9, 1, 1, 4, 0, deriveBox, mainApp);
			
			if(func.isTakingIntegral()){
				startInt.getField().setText("" + func.getStartIntegral());
				endInt.getField().setText("" + func.getEndIntegral());
				integrate();
			}
			
			if(func.isDeriving()){
				indVarVal.getField().setText("" + func.getDerivative());
				try {
					derive();
				} catch (EvalException e) {
					// TODO Auto-generated catch block
					;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					;
				}
			}
			
			if(func.isTracingPt()){
				pt2Trace.getField().setText("" + func.getTraceVal());
				tracePt();
			}
		}
		else if(func.getGraphType() == 2){
			//spacer = new OCLabel("Integrate", 1, 1, 5, 0, deriveBox, mainApp);
		}
		else{
			;//do nothing, will add when more graph types supported
		}
		
		this.revalidate();
		this.repaint();
	}

}

