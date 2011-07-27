/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import doc.DocViewerPanel;

public abstract class MathObjectAttribute<K> {
	
	
	public static final String BOOLEAN_ATTRIBUTE = "BooleanAttribute";
	public static final String DOUBLE_ATTRIBUTE = "DoubleAttribute";
	public static final String GRID_POINT_ATTRIBUTE = "GridPointAttribute";
	public static final String INTEGER_ATTRIBUTE = "IntegerAttribute";
	public static final String STRING_ATTRIBUTE = "StringAttribute";
	
	private String name;
	
	private K value;
	
	private boolean studentEditable;
	
	private boolean showingDialog;

	public MathObjectAttribute(String n){
		name = n;
		setStudentEditable(false);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(K value) {
		this.value = value;
	}

	public K getValue() {
		return value;
	}
	
	public abstract void setValueWithString(String s) throws AttributeException;
	
	public abstract String getType();
	
	public JPanel makeAdjustmentPanel(final DocViewerPanel dvp){
		JPanel temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.HORIZONTAL;
		con.weightx = .01;
		con.gridx = 0;
		con.gridy = 0;
		con.insets = new Insets(0, 10, 0, 10);
		temp.add(new JLabel(getName()), con);
		final JTextField field = new JTextField();
		con.weightx = 1;
		con.gridx = 1;
		temp.add(field, con);
		field.setText(getValue().toString());
		field.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				try {
					setValueWithString(field.getText());
					dvp.repaintDoc();
				} catch (AttributeException e) {
					if (!showingDialog){
						JOptionPane.showMessageDialog(null,
							    e.getMessage(),
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						showingDialog = false;
					}
				}
			}
			
		});
		field.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					setValueWithString(field.getText());
					dvp.repaintDoc();
				} catch (AttributeException e) {
					// TODO Auto-generated catch block
					if (!showingDialog){
						showingDialog = true;
						JOptionPane.showMessageDialog(null,
							    e.getMessage(),
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						showingDialog = false;
					}
				}
			}
			
		});
		return temp;
	}

	public void setStudentEditable(boolean studentEditable) {
		this.studentEditable = studentEditable;
	}

	public boolean isStudentEditable() {
		return studentEditable;
	}
}
