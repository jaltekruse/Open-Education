/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui.attributes;

import java.awt.Color;
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

import doc.GridPoint;
import doc.mathobjects.AttributeException;
import doc.mathobjects.MathObject;
import doc_gui.DocViewerPanel;

public abstract class MathObjectAttribute<K> {
	
	
	public static final String BOOLEAN_ATTRIBUTE = "boolean";
	public static final String DOUBLE_ATTRIBUTE = "double";
	public static final String GRID_POINT_ATTRIBUTE = "point";
	public static final String INTEGER_ATTRIBUTE = "int";
	public static final String STRING_ATTRIBUTE = "string";
	public static final String COLOR_ATTRIBUTE = "color";
	
	private String name;
	
	private K value;
	
	private boolean userEditable;
	
	private boolean studentEditable;
	
	private boolean showingDialog;
	
	private MathObject parentObject;

	public MathObjectAttribute(String n){
		name = n;
		setStudentEditable(false);
		setUserEditable(true);
	}
	
	public MathObjectAttribute(String n, K val){
		name = n;
		setValue(val);
		setStudentEditable(false);
		setUserEditable(true);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public abstract K readValueFromString(String s) throws AttributeException;

	public void setValue(K value) {
		this.value = value;
	}

	public K getValue() {
		return value;
	}
	
	public String export(){
		return "<" + getType() + " name=\"" + getName()
		+ "\" value=\"" + getValue().toString() + "\"/>\n";
	}
	
	public MathObjectAttribute clone(){
		MathObjectAttribute a = null;
		if (this instanceof BooleanAttribute){
			a = new BooleanAttribute( new String (this.getName() ), 
					new Boolean( ((BooleanAttribute) this).getValue() ));
		}
		else if( this instanceof IntegerAttribute){
			a = new IntegerAttribute( new String (this.getName() ), 
					new Integer( ((IntegerAttribute) this).getValue()) ,
					new Integer( ((IntegerAttribute) this).getMinimum()) ,
					new Integer( ((IntegerAttribute) this).getMaximum()) );
		}
		else if (this instanceof StringAttribute){
			a = new StringAttribute( new String (this.getName() ),
					((StringAttribute) this).getValue());
		}
		else if (this instanceof DoubleAttribute){
			a = new DoubleAttribute( new String (this.getName() ), 
					new Double( ((DoubleAttribute) this).getValue()) ,
					new Double( ((DoubleAttribute) this).getMinimum()) ,
					new Double( ((DoubleAttribute) this).getMaximum()) );
		}
		else if (this instanceof GridPointAttribute){
			a = new GridPointAttribute( new String (this.getName() ), 
					new GridPoint( ((GridPointAttribute)this).getValue().getx(), 
					((GridPointAttribute)this).getValue().gety() ) );
		}
		else if( this instanceof ColorAttribute){
			if (this.getValue() == null){
				a = new ColorAttribute( new String (this.getName() ) );
			}
			else{
				a = new ColorAttribute( this.getName(), new Color( 
						((ColorAttribute) this).getValue().getRed() ,
						((ColorAttribute) this).getValue().getGreen() ,
						((ColorAttribute) this).getValue().getBlue() ) );
			}
		}
		if (a != null){
			a.setStudentEditable(isStudentEditable());
			a.setUserEditable(isUserEditable());
			return a;
		}
		return null;
	}
	
	public void setValueWithString(String s) throws AttributeException{
		setValue(readValueFromString(s));
	}
	
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

	public void setUserEditable(boolean userEditable) {
		this.userEditable = userEditable;
	}

	public boolean isUserEditable() {
		return userEditable;
	}

	public void setParentObject(MathObject parentObject) {
		this.parentObject = parentObject;
	}

	public MathObject getParentObject() {
		return parentObject;
	}
}
