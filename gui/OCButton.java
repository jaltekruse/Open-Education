/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

import tree.Operator;

import tree.EvalException;
import tree.ParseException;
import tree.ValueNotStoredException;

public class OCButton extends JButton {

	/**
	 * An OCButton is the standard object for a button on the interface.
	 * The constructor takes various attributes for using a GridBagConstraints
	 * object. The default action associated with an OCButton is to append
	 * the text on the button to the currently selected OCTextfield. This can
	 * be changed by overriding the associatedAction method. 
	 */
	private static final long serialVersionUID = 1L;
	private String s;
	private MainApplet mainApp;
	private GridBagConstraints bCon;
	private JComponent comp;

	public OCButton(String str, int gridwidth, int gridheight, int gridx,
			int gridy, JComponent comp, final MainApplet currmainApp) {

		super(str);
		this.comp = comp;
		s = str;
		mainApp = currmainApp;
		bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = .1;
		bCon.weighty = .2;
		bCon.gridheight = gridheight;
		bCon.gridwidth = gridwidth;
		bCon.gridx = gridx;
		bCon.gridy = gridy;
		bCon.insets = new Insets(2, 2, 2, 2);
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					associatedAction();
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

		comp.add(this, bCon);
	}
	
	public OCButton(String str, int gridwidth, int gridheight, int gridx,
			int gridy, JComponent comp) {

		super(str);
		this.comp = comp;
		s = str;
		bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = .1;
		bCon.weighty = .2;
		bCon.gridheight = gridheight;
		bCon.gridwidth = gridwidth;
		bCon.gridx = gridx;
		bCon.gridy = gridy;
		bCon.insets = new Insets(2, 2, 2, 2);
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					associatedAction();
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

		comp.add(this, bCon);
	}
	
	public OCButton(Icon bi, String tip, int gridwidth, int gridheight, int gridx,
			int gridy, JComponent comp, final MainApplet currmainApp) {

		super(bi);
		this.comp = comp;
		this.setToolTipText(tip);
		mainApp = currmainApp;
		bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = .1;
		bCon.weighty = .2;
		bCon.gridheight = gridheight;
		bCon.gridwidth = gridwidth;
		bCon.gridx = gridx;
		bCon.gridy = gridy;
		bCon.insets = new Insets(2, 2, 2, 2);
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					associatedAction();
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

		comp.add(this, bCon);
	}

	public OCButton(boolean hasInsets, String str, int gridwidth, int gridheight, int gridx,
			int gridy, JComponent comp, final MainApplet currmainApp) {

		super(str);
		this.comp = comp;
		s = str;
		mainApp = currmainApp;
		bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = .1;
		bCon.weighty = .2;
		bCon.gridheight = gridheight;
		bCon.gridwidth = gridwidth;
		bCon.gridx = gridx;
		bCon.gridy = gridy;
		if (hasInsets)
		{
			bCon.insets = new Insets(2, 2, 2, 2);
		}
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					associatedAction();
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

		comp.add(this, bCon);
	}
	
	public OCButton(String str, String tip, int gridwidth, int gridheight, int gridx,
			int gridy, JComponent comp, final MainApplet currmainApp) {
		
		super(str);
		this.comp = comp;
		s = str;
		mainApp = currmainApp;
		bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = .1;
		bCon.weighty = .2;
		bCon.gridheight = gridheight;
		bCon.gridwidth = gridwidth;
		bCon.gridx = gridx;
		bCon.gridy = gridy;
		bCon.insets = new Insets(2, 2, 2, 2);
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					associatedAction();
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
		
		this.setToolTipText(tip);

		comp.add(this, bCon);
	}

	public void removeInsets(){
		comp.remove(this);
		bCon.insets = null;
		comp.add(this, bCon);
	}
	public void associatedAction() throws ParseException, ValueNotStoredException, EvalException {
		JTextField currField = mainApp.getCurrTextField().getField();
		int caretPos = 0;
		if (currField != null) {
			String currText = currField.getText();
			if (currField.getCaretPosition() == 0 && 
					Operator.requiresPrevious(s)){
						currField.setText("ans" + s);
						currField.requestFocus();
						currField.setCaretPosition(3 + s.length());
			}
			else if (currField.getSelectionStart() == currField.getSelectionEnd()) {
				currText = currField.getText();
				caretPos = currField.getCaretPosition();
				String tempText = currText.substring(0, caretPos);
				tempText += s;
				tempText += currText.substring(caretPos, currText.length());
				currField.setText(tempText);
				currField.requestFocus();
				currField.setCaretPosition(caretPos + s.length());
			}
			else {
				int selectStart = currField.getSelectionStart();
				int selectEnd = currField.getSelectionEnd();
				String tempText = currText.substring(0, selectStart);
				tempText += s;
				tempText += currText.substring(selectEnd, currText.length());
				currField.setText(tempText);
				currField.requestFocus();
				currField.setCaretPosition(selectStart + s.length());
			}
		}
	}

}
