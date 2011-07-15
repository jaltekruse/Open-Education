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
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import tree.EvalException;
import tree.ParseException;
import tree.ValueNotStoredException;

public class OCTextField extends SubPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MainApplet mainApp;
	OCTextField thisField;
	JTextField field;
	JComponent comp;
	
	ArrayList<String> history;

	public OCTextField(TopLevelContainerOld topLevelComp) {
		super(topLevelComp);
		field = new JTextField();
	}

	public OCTextField(TopLevelContainerOld topLevelComp, boolean editable, int length, int gridWidth,
			int gridHeight, int gridx, int gridy, JComponent comp,
			final MainApplet currmainApp) {
		
		super(topLevelComp);
		this.comp = comp;
		field = new JTextField();
		field.setEditable(editable);
		GridBagConstraints tCon = new GridBagConstraints();
		history = new ArrayList<String>();

		thisField = this;

		mainApp = currmainApp;
		field.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				try {
					mainApp.setCurrTextField(thisField);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ValueNotStoredException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (EvalException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if (!field.isVisible()){
					mainApp.getGlassPanel().setHistoryVisible(false);
				}
			}
		});
		
		field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					primaryAction();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ValueNotStoredException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (EvalException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		field.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_DOWN){
					if (!field.getText().equals("")){
						addToHistory(field.getText());
					}
					getTopLevelContainer().getGlassPanel().addFieldHistory();
					getTopLevelContainer().getGlassPanel().refresh();
					getTopLevelContainer().getGlassPanel().setHistoryVisible(true);
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
					getTopLevelContainer().getGlassPanel().setHistoryVisible(false);
					mainApp.getCurrTextField().getField().requestFocus();
				}
				if (e.getKeyCode() == KeyEvent.VK_UP){
					mainApp.getCurrTextField().getField().requestFocus();
					getTopLevelContainer().getGlassPanel().setHistoryVisible(false);
					return;
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			
		});

		tCon.fill = GridBagConstraints.BOTH;
		tCon.weightx = .5;
		tCon.gridheight = 1;
		tCon.gridwidth = 1;
		tCon.gridheight = 1;
		tCon.gridx = 0;
		tCon.gridy = 0;
		this.add(field, tCon);
		this.setBorder(BorderFactory.createLineBorder(Color.gray));
		if (editable){
			OCButton dropDown = new OCButton(true,"v", 1, 1, 1, 0, this, mainApp){
				public void associatedAction() throws ParseException, ValueNotStoredException, EvalException{
					if (getTopLevelContainer().getGlassPanel().historyIsVisible()){
						getTopLevelContainer().getGlassPanel().setHistoryVisible(false);
						if (!mainApp.getCurrTextField().equals(thisField)){
							mainApp.setCurrTextField(thisField);
							if (!field.getText().equals("")){
								addToHistory(field.getText());
							}
							getTopLevelContainer().getGlassPanel().addFieldHistory();
							getTopLevelContainer().getGlassPanel().refresh();
							getTopLevelContainer().getGlassPanel().setHistoryVisible(true);
						}
					}
					else{
						mainApp.setCurrTextField(thisField);
						//dont know why this doesn't work, have to fix it sometime
						if (!field.getText().equals("")){
							addToHistory(field.getText());
						}
						getTopLevelContainer().getGlassPanel().addFieldHistory();
						getTopLevelContainer().getGlassPanel().refresh();
						getTopLevelContainer().getGlassPanel().setHistoryVisible(true);
					}
				}
			};

		}
		
		tCon.fill = GridBagConstraints.HORIZONTAL;
		tCon.weightx = 1;
		tCon.gridheight = 1;
		tCon.gridwidth = gridWidth;
		tCon.gridheight = gridHeight;
		tCon.gridx = gridx;
		tCon.gridy = gridy;
		comp.add(this, tCon);
	}

	/**
	 * Used to add a member to this fields history when it has an action invoked on it, either
	 * from pressing enter, or another event. Do not override this!!
	 * 
	 * @throws EvalException 
	 * @throws ValueNotStoredException 
	 * @throws ParseException 
	 */
	public void primaryAction() throws ParseException, ValueNotStoredException, EvalException{
		if (!field.getText().equals("")){
			addToHistory(field.getText());
		}
		if (mainApp != null && mainApp.getGlassPanel().historyIsVisible()){
			System.out.println("history is visible refreshing");
			mainApp.getGlassPanel().refresh();
		}
		associatedAction();
	}
	
	/**
	 * This method takes the expression in the current field an evaluates it, the result is
	 * placed into the field. Override this to change the functionality of a 
	 * 
	 * @throws ParseException
	 * @throws ValueNotStoredException
	 * @throws EvalException
	 */
	public void associatedAction(){
		
		String currText = field.getText();
		if (!currText.equals(null) && !currText.equals("") && mainApp != null) {
			try{
				field.setText(mainApp.evalCalc(currText).toString());
				mainApp.updateGraph();
			} catch (Exception ex){
				field.setText("error with eval, hit the down arrow to get to history");
			}
		}
	}
	
	/**
	 * This method is used to create another action after the primary action and associated
	 * action. If you want the box to do something after the typical actions.
	 */
	public void auxillaryAction(){
		
	}
	
	private void addToHistory(String s){
		if (history.size() == 0)
		{
			history.add(s);
		}
		else
		{
			if (!history.get(history.size() - 1).equals(s))
			{//don't add if this item is the same as the one at the top of the list
				history.add(s);
			}
		}
		if (history.size() == 11){
			history.remove(0);
		}
	}
	
	public ArrayList<String> getHistory(){
		return history;
	}
	
	public JTextField getField(){
		return field;
	}
	
	public int getXCord(){
		return comp.getX() + thisField.getX();
	}
	public int getYCord(){
		return comp.getY() + thisField.getY();
	}
}
