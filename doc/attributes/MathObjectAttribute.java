/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.attributes;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.UUID;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import doc.GridPoint;
import doc.mathobjects.MathObject;
import doc_gui.DocViewerPanel;

public abstract class MathObjectAttribute<K> {


	public static final String BOOLEAN_ATTRIBUTE = "boolean", DOUBLE_ATTRIBUTE = "double",
			GRID_POINT_ATTRIBUTE = "point", INTEGER_ATTRIBUTE = "int",
			STRING_ATTRIBUTE = "string", COLOR_ATTRIBUTE = "color", DATE = "date",
			EMAIL_ATTRIBUTE = "email", UUID_ATTRIBUTE = "uuid", VAR_NAME_ATTRIBUTE = "varName",
			ENUMERATED_ATTRIUBTE = "enumeratedValue", VAR_VAL_GENERATOR = "varValGenerator";

	public static final String[] attTypes = {	BOOLEAN_ATTRIBUTE,		DOUBLE_ATTRIBUTE,
		GRID_POINT_ATTRIBUTE,				INTEGER_ATTRIBUTE,		STRING_ATTRIBUTE,
		COLOR_ATTRIBUTE, DATE,				EMAIL_ATTRIBUTE,		UUID_ATTRIBUTE,
		VAR_NAME_ATTRIBUTE,					ENUMERATED_ATTRIUBTE,	VAR_VAL_GENERATOR};

	public static final String NAME = "name", VALUE = "value";

	private String name;
	protected K value;
	private boolean userEditable, studentEditable;
	private MathObject parentObject;

	public MathObjectAttribute(String n){
		name = n;
		setStudentEditable(false);
		setUserEditable(true);
		resetValue();
	}

	public MathObjectAttribute(String n, boolean userEditable){
		name = n;
		setStudentEditable(false);
		setUserEditable(userEditable);
		resetValue();
	}

	public MathObjectAttribute(String n, boolean userEditable, boolean studentEditable){
		name = n;
		setStudentEditable(studentEditable);
		setUserEditable(userEditable);
		resetValue();
	}

	public MathObjectAttribute(String n, K val){
		name = n;
		setValue(val);
		setStudentEditable(false);
		setUserEditable(true);
	}

	public MathObjectAttribute(String n, K val, boolean userEditable){
		name = n;
		setValue(val);
		setStudentEditable(false);
		setUserEditable(userEditable);
	}

	public MathObjectAttribute(String n, K val, boolean userEditable, boolean studentEditable){
		name = n;
		setValue(val);
		setStudentEditable(studentEditable);
		setUserEditable(userEditable);
	}

	public static boolean isAttributeType(String type){
		for (String s : attTypes){
			if (type.equals(s)){
				return true;
			}
		}
		return false;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract K readValueFromString(String s) throws AttributeException;

	public void setValue(K value){
		this.value = value;
	}

	public K getValue() {
		return value;
	}

	public String exportToXML(){
		return "<" + getType() + " " + NAME + "=\"" + getName()
				+ "\" " + VALUE + "=\"" + formatFoXML(getValue().toString()) + "\"/>\n";
	}

	public abstract void resetValue();

	public static String formatFoXML(String aText){
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(aText);
		char character =  iterator.current();
		while (character != CharacterIterator.DONE ){
			if (character == '<') {
				result.append("&lt;");
			}
			else if (character == '>') {
				result.append("&gt;");
			}
			else if (character == '\"') {
				result.append("&quot;");
			}
			else if (character == '\'') {
				result.append("&#039;");
			}
			else if (character == '&') {
				result.append("&amp;");
			}
			else {
				//the char is not a special one
				//add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	@Override
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
		else if (this instanceof VariableNameAttribute){
			a = new VariableNameAttribute( new String (this.getName() ),
					((VariableNameAttribute) this).getValue());
		}
		else if (this instanceof VariableNameAttribute){
			a = new EnumeratedAttribute( new String (this.getName() ),
					((EnumeratedAttribute) this).getValue(),
					((EnumeratedAttribute)this).getPossibleValues().clone());
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
		else if ( this instanceof DateAttribute){
			a = new DateAttribute(this.getName());
			if ( this.getValue() != null){
				Date date = new Date();
				try{
					date.setMonth( ( (DateAttribute)this).getValue().getMonth());
					date.setDay( ( (DateAttribute)this).getValue().getDay());
					date.setYear( ( (DateAttribute)this).getValue().getYear());
				} catch (Exception ex){
					// impossible error, values are taken out of a date, so they will
					// be valid
				}
				a.setValue(date);
			}
		}
		else if ( this instanceof EmailAttribute){
			a = new EmailAttribute( new String(this.getName()));
			a.setValue(new String( ( (String)this.getValue()) ) );
		}
		else if (this instanceof UUIDAttribute){
			a = new UUIDAttribute(new String(this.getName()));
			if (getValue() != null){
				a.setValue(new UUID( ((UUID)getValue()).getLeastSignificantBits(), 
						((UUID)getValue()).getMostSignificantBits()));
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
