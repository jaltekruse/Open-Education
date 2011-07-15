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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import doc.DocViewerPanel;
import doc.Document;

public class ObjectPropertiesFrame extends JInternalFrame {
	
	private JPanel panel;
	private DocViewerPanel docPanel;
	private Vector<AdjustmentPanel> adjusters;
	private MathObject object;
	private ObjectPropertiesFrame thisFrame;
	
	public ObjectPropertiesFrame(DocViewerPanel dvp){
		super("tools",
				true, //resizable
				false, //closable
				false, //maximizable
				false);//iconifiable
		docPanel = dvp;
		panel = new JPanel();
		adjusters = new Vector<AdjustmentPanel>();
		this.getContentPane().add(new JScrollPane(panel));
		thisFrame = this;
	}
	
	public JPanel generatePanel(Document doc, DocViewerPanel docPanel) {
		JPanel panel = new JPanel();
		panel.removeAll();
		if (doc != null){
			panel.setLayout(new GridBagLayout());
			GridBagConstraints con = new GridBagConstraints();
			con.fill = GridBagConstraints.BOTH;
			con.weightx = 1;
			con.weighty = 1;
			con.gridx = 0;
			con.gridy = 0;
			for (MathObjectAttribute mAtt : doc.getAttributes()){
				if (mAtt instanceof BooleanAttribute){
					panel.add(new BooleanAdjustmentPanel((BooleanAttribute)mAtt, docPanel), con);
				}
				else if (mAtt instanceof StringAttribute){
					panel.add(new StringAdjustmentPanel((StringAttribute)mAtt, docPanel), con);
				}
				else{
					panel.add(new GenericAdjustmentPanel(mAtt, docPanel), con);
				}
				con.gridy++;
			}
		}
		return panel;
	}
	
	public void generatePanel(MathObject o){
		object = o;
		panel.removeAll();
		this.setTitle(o.getClass().getSimpleName());
		if (o != null){
			panel.setLayout(new GridBagLayout());
			GridBagConstraints con = new GridBagConstraints();
			con.fill = GridBagConstraints.BOTH;
			con.weightx = 1;
			con.weighty = 1;
			con.gridx = 0;
			con.gridy = 0;
			for (final String s : o.getActions()){
				JButton button = new JButton(s);
				panel.add(button, con);
				button.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						object.performAction(s);
						thisFrame.update();
						docPanel.repaint();
					}
					
				});
				con.gridy++;
			}
			for (MathObjectAttribute mAtt : o.getAttributes()){
				if (mAtt instanceof BooleanAttribute){
					adjusters.add(new BooleanAdjustmentPanel((BooleanAttribute)mAtt, docPanel));
				}
				else if (mAtt instanceof StringAttribute){
					adjusters.add(new StringAdjustmentPanel(mAtt, docPanel));
				}
				else{
					adjusters.add(new GenericAdjustmentPanel(mAtt, docPanel));
				}
				panel.add(adjusters.get(adjusters.size() - 1), con);
				con.gridy++;
			}
		}
	}
	
	private abstract class AdjustmentPanel extends JPanel{
		
		protected MathObjectAttribute mAtt;
		protected DocViewerPanel docPanel;
		
		protected boolean showingDialog;
		
		public AdjustmentPanel(final MathObjectAttribute mAtt, DocViewerPanel dvp){
			this.mAtt = mAtt;
			docPanel = dvp;
			addPanelContent();
		}
		
		public abstract void addPanelContent();
		
		public abstract void updateData();
	}
	
	private class GenericAdjustmentPanel extends AdjustmentPanel{

		private JTextField field;
		
		public GenericAdjustmentPanel(MathObjectAttribute mAtt,
				DocViewerPanel dvp) {
			super(mAtt, dvp);
		}

		@Override
		public void updateData() {
			field.setText(mAtt.getValue().toString());
		}

		@Override
		public void addPanelContent() {
			setLayout(new GridBagLayout());
			GridBagConstraints con = new GridBagConstraints();
			con.fill = GridBagConstraints.HORIZONTAL;
			con.weightx = .01;
			con.gridx = 0;
			con.gridy = 0;
			con.insets = new Insets(0, 10, 0, 10);
			add(new JLabel(mAtt.getName()), con);
			field = new JTextField();
			con.weightx = 1;
			con.gridx = 1;
			add(field, con);
			field.setText(mAtt.getValue().toString());
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
						mAtt.setValueWithString(field.getText());
						docPanel.repaintDoc();
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
	}
	
	private class StringAdjustmentPanel extends AdjustmentPanel{

		private JTextArea textArea;
		
		public StringAdjustmentPanel(MathObjectAttribute mAtt,
				DocViewerPanel dvp) {
			super(mAtt, dvp);
		}

		@Override
		public void addPanelContent() {
			textArea = new JTextArea(2, 2);
			textArea.setEditable(true);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setText(((StringAttribute)mAtt).getValue());
			textArea.addKeyListener(new KeyListener(){

				@Override
				public void keyPressed(KeyEvent arg0) {
					// TODO Auto-generated method stub
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
						try {
							mAtt.setValueWithString(textArea.getText());
							docPanel.repaintDoc();
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
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
						textArea.setCaretPosition(textArea.getCaretPosition());
					}
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					// TODO Auto-generated method stub
				}
				
			});
			setLayout(new GridBagLayout());
			GridBagConstraints con = new GridBagConstraints();
			con.fill = GridBagConstraints.HORIZONTAL;
			con.weightx = .01;
			con.gridx = 0;
			con.gridwidth = 1;
			con.gridy = 0;
			con.insets = new Insets(0, 10, 0, 10);
			add(new JLabel(mAtt.getName()), con);
			con.fill = GridBagConstraints.BOTH;
			con.weightx = 1;
			con.weighty = 1;
			con.gridy = 1;
			con.gridx = 0;
			con.gridheight = 2;
			add(new JScrollPane(textArea), con);
			
		}

		@Override
		public void updateData() {
			textArea.setText(mAtt.getValue().toString());
		}
		
	}
	
	private class BooleanAdjustmentPanel extends AdjustmentPanel{
		
		private JCheckBox checkbox;
		
		public BooleanAdjustmentPanel(BooleanAttribute mAtt,
				DocViewerPanel dvp) {
			super(mAtt, dvp);
		}

		@Override
		public void updateData() {
			checkbox.setSelected(((BooleanAttribute)mAtt).getValue());
		}

		@Override
		public void addPanelContent() {
			setLayout(new GridLayout(0,1));
			checkbox = new JCheckBox(mAtt.getName());
			
			checkbox.setSelected(((BooleanAttribute)mAtt).getValue());
			checkbox.addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (e.getStateChange() == ItemEvent.SELECTED){
						mAtt.setValue(true);
						docPanel.repaintDoc();
					}
					else{
						mAtt.setValue(false);
						docPanel.repaintDoc();
					}
				}
				
			});
			add(checkbox);
			
		}
		
	}
	
	public void update(){
		for (AdjustmentPanel a : adjusters){
			a.updateData();
		}
	}
	
	

}
