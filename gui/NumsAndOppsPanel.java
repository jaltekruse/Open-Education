/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import tree.EvalException;
import tree.ExpressionParser;
import tree.ParseException;
import tree.ValueNotStoredException;

public class NumsAndOppsPanel extends SubPanel {
	/**
	 * 
	 */

	private boolean trigInv;
	private OCButton sin, cos, tan, trigInverse;
	private MainApplet mainApp;
	private JComboBox angleUnitSelect;
	
	NumsAndOppsPanel(final MainApplet mainApp, TopLevelContainerOld topLevelComp) {
		// SubPanel(Numbuttons, NumTextFields)
		super(topLevelComp);
		this.mainApp = mainApp;
		trigInv = false;
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(100, 100));
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.setSize(mainApp.getSize().width / 2,
				mainApp.getSize().height / 9 * 4);

		// OCButton( string, width, height, x, y, container, NewCalc)

		OCButton carret = new OCButton("^", "exponent", 1, 1, 0, 2, this, mainApp);
		OCButton inv = new OCButton("^-1", "inverse", 1, 1, 0, 3, this, mainApp);
		OCButton dot = new OCButton(".", 1, 1, 3, 6, this, mainApp);
		OCButton plus = new OCButton("+", "addition", 1, 2, 4, 3, this, mainApp);
		OCButton minus = new OCButton("-", 1, 1, 4, 2, this, mainApp);
		OCButton mult = new OCButton("*", 1, 1, 3, 2, this, mainApp);
		OCButton divide = new OCButton("/", 1, 1, 2, 2, this, mainApp);
		OCButton leftParen = new OCButton("(", 1, 1, 3, 1, this, mainApp);
		OCButton rightParen = new OCButton(")", 1, 1, 4, 1, this, mainApp);
		OCButton equals = new OCButton("=", "variable assignment", 1, 1, 7, 0, this, mainApp);
		OCButton natLog = new OCButton("ln(", "natural log", 1, 1, 0, 6, this, mainApp);
		OCButton log = new OCButton("log(", "log base 10", 1, 1, 0, 5, this, mainApp);
		OCButton sqrt = new OCButton("sqrt(", "square root", 1, 1, 0, 4, this, mainApp);
		OCButton sciNote = new OCButton("E", "scientific notation", 1, 1, 2, 1, this, mainApp);
		OCButton zero = new OCButton("0", 2, 1, 1, 6, this, mainApp);
		OCButton one = new OCButton("1", 1, 1, 1, 5, this, mainApp);
		OCButton two = new OCButton("2", 1, 1, 2, 5, this, mainApp);
		OCButton three = new OCButton("3", 1, 1, 3, 5, this, mainApp);
		OCButton four = new OCButton("4", 1, 1, 1, 4, this, mainApp);
		OCButton five = new OCButton("5", 1, 1, 2, 4, this, mainApp);
		OCButton six = new OCButton("6", 1, 1, 3, 4, this, mainApp);
		OCButton seven = new OCButton("7", 1, 1, 1, 3, this, mainApp);
		OCButton eight = new OCButton("8", 1, 1, 2, 3, this, mainApp);
		OCButton nine = new OCButton("9", 1, 1, 3, 3, this, mainApp);
		OCButton tenPow = new OCButton("10^(", 1, 1, 1, 2, this, mainApp);
		OCButton pi = new OCButton("pi", 1, 1, 7, 1, this, mainApp);
		OCButton eulers = new OCButton("e", "eulers number", 1, 1, 7, 2, this, mainApp);
		OCButton eulerPow = new OCButton("e^(", "eulers number to a power", 1, 1, 7, 3, this, mainApp);
		OCButton a = new OCButton("a", "variable for storage", 1, 1, 7, 4, this, mainApp);
		OCButton b = new OCButton("b", "variable for stroage", 1, 1, 7, 5, this, mainApp);
		OCButton ans = new OCButton("ans", "the last result", 1, 1, 7, 6, this, mainApp);
		//OCButton y = new OCButton("y", 1, 1, 7, 4, this, mainApp);
		sin = new OCButton("sin(", 1, 1, 1, 0, this, mainApp);
		cos = new OCButton("cos(", 1, 1, 2, 0, this, mainApp);
		tan = new OCButton("tan(", 1, 1, 3, 0, this, mainApp);
		trigInverse = new OCButton("Inv", "Make trig functions inverse.", 1, 1, 4, 0, this, mainApp){
			public void associatedAction(){
					changeTrigButtons();
			}
		};

		OCButton back = new OCButton("<-", "backspace", 1, 1, 1, 1, this, mainApp) {
			public void associatedAction() {
				JTextField currField = mainApp.getCurrTextField().getField();
				if (currField != null) {
					String currText = currField.getText();
					int caretPos = currField.getCaretPosition();

					if (currText.equals("")) {
						// do nothing
					}

					if (caretPos > 0) {
						if (currField.getSelectionStart() == currField
								.getSelectionEnd()) {
							System.out.println("backspace");
							currText = currField.getText();
							String tempText = currText.substring(0,
									caretPos - 1);
							tempText += currText.substring(caretPos, currText
									.length());
							currField.setText(tempText);
							currField.requestFocus();
							currField.setCaretPosition(caretPos - 1);
						} else {
							int selectStart = currField.getSelectionStart();
							int selectEnd = currField.getSelectionEnd();
							String tempText = currText
									.substring(0, selectStart);
							tempText += currText.substring(selectEnd, currText
									.length());
							currField.setText(tempText);
							currField.requestFocus();
							currField.setCaretPosition(selectStart);
						}
					}
				}
			}
		};

		String[] angleUnits = {"rad", "deg", "grad"};
		angleUnitSelect = new JComboBox(angleUnits);
		
		angleUnitSelect.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e){
				int units = angleUnitSelect.getSelectedIndex() + 1;
				
				
				if (units == 1){
					mainApp.getParser().setAngleUnits(ExpressionParser.RAD);
				}
				
				else if (units == 2){
					mainApp.getParser().setAngleUnits(ExpressionParser.DEG);
				}
				
				else if (units == 3){
					mainApp.getParser().setAngleUnits(ExpressionParser.GRAD);
				}
			}
		});
		
		GridBagConstraints bCon = new GridBagConstraints();
		bCon.gridx = 0;
		bCon.gridy = 0;
		
		this.add(angleUnitSelect, bCon);
		
		OCButton clear = new OCButton("CL", "Clear all in the current field.", 1, 1, 0, 1, this, mainApp) {
			public void associatedAction() {
				JTextField currField = mainApp.getCurrTextField().getField();
				if (currField != null) {
					currField.setText("");
					currField.requestFocus();
				}
			}
		};
		
		OCButton enter = new OCButton("entr", "Evaluate the current field.", 1, 2, 4, 5, this, mainApp){
			public void associatedAction() throws ParseException, ValueNotStoredException, EvalException{
				mainApp.getCurrTextField().primaryAction();
			}
		}; 
	}
	
	private void changeTrigButtons(){
		trigInv = !trigInv;
		if (trigInv){
			this.remove(sin);
			this.remove(cos);
			this.remove(tan);
			this.remove(trigInverse);
			sin = new OCButton("sin-1(", "inverse sine", 1, 1, 1, 0, this, mainApp);
			cos = new OCButton("cos-1(", "inverse cosine", 1, 1, 2, 0, this, mainApp);
			tan = new OCButton("tan-1(", "inverse tangent", 1, 1, 3, 0, this, mainApp);
			trigInverse = new OCButton("Reg", "Return to regular trig functions.", 1, 1, 4, 0, this, mainApp){
				public void associatedAction(){
					changeTrigButtons();
				}
			};
			this.revalidate();
			this.repaint();
		}
		else{
			this.remove(sin);
			this.remove(cos);
			this.remove(tan);
			this.remove(trigInverse);
			sin = new OCButton("sin(", 1, 1, 1, 0, this, mainApp);
			cos = new OCButton("cos(", 1, 1, 2, 0, this, mainApp);
			tan = new OCButton("tan(", 1, 1, 3, 0, this, mainApp);
			trigInverse = new OCButton("Inv", "Make trig functions inverse.", 1, 1, 4, 0, this, mainApp){
				public void associatedAction(){
					changeTrigButtons();
				}
			};
			this.revalidate();
			this.repaint();
		}
	}
}
