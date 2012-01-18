package doc_gui.object_panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Formatter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import doc.attributes.AttributeException;
import doc.attributes.MathObjectAttribute;
import doc_gui.DocViewerPanel;

public class GenericAdjustmentPanel extends AdjustmentPanel{

	private JTextField field;
	
	private static Formatter formatter;

	public GenericAdjustmentPanel(MathObjectAttribute mAtt,
			DocViewerPanel dvp, JPanel p) {
		super(mAtt, dvp, p);
		formatter = new Formatter();
	}

	@Override
	public void updateData() {
		if ( mAtt.getValue() instanceof Double){
			if (mAtt.getValue().toString().length() > 6){
				field.setText(String.format("%.6G", mAtt.getValue()));
			}
			else{
				field.setText(mAtt.getValue().toString());
			}
		}
		else{
			field.setText(mAtt.getValue().toString());
		}
		field.setCaretPosition(0);
	}

	@Override
	public void addPanelContent() {
		setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.HORIZONTAL;
		con.weightx = .1;
		con.gridx = 0;
		con.gridy = 0;
		con.insets = new Insets(0, 10, 0, 0);
		add(new JLabel(mAtt.getName()), con);
		field = new JTextField();
		con.weightx = 1;
		con.gridx = 1;
		con.insets = new Insets(0, 10, 0, 5);
		add(field, con);
		if ( mAtt.getValue() instanceof Double){
			int len = mAtt.getValue().toString().length();
			if (len > 5){
				formatter = new Formatter();
				field.setText(String.format("%.5G", mAtt.getValue()));
			}
		}
		else{
			field.setText(mAtt.getValue().toString());
		}
		field.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusLost(FocusEvent arg0) {
				try {
					mAtt.setValueWithString(field.getText());
					docPanel.repaintDoc();
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
					if (mAtt.getParentObject() != null){
						mAtt.getParentObject().setAttributeValueWithString(
								mAtt.getName(), field.getText());
					}
					else{
						mAtt.setValueWithString(field.getText());
					}
					docPanel.repaintDoc();
					docPanel.updateObjectToolFrame();
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
	}

	@Override
	public void applyPanelValueToObject() {
		// TODO Auto-generated method stub
		try {
			mAtt.setValueWithString(field.getText());
			if ( mAtt.getValue() instanceof Double){
				if (mAtt.getValue().toString().length() > 5){
					field.setText(String.format("%.5G", mAtt.getValue()));
				}
			}
			else{
				field.setText(mAtt.getValue().toString());
			}
			docPanel.repaintDoc();
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

	@Override
	public void focusAttributField() {
		// TODO Auto-generated method stub
		field.requestFocus();
	}
}