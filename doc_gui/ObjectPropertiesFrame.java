/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


import doc.Document;
import doc.mathobjects.AttributeException;
import doc.mathobjects.GraphObject;
import doc.mathobjects.MathObject;
import doc.mathobjects.PolygonObject;
import doc.mathobjects.TriangleObject;
import doc_gui.attributes.BooleanAttribute;
import doc_gui.attributes.MathObjectAttribute;
import doc_gui.attributes.StringAttribute;
import doc_gui.attributes.ColorAttribute;
import doc_gui.mathobject_gui.MathObjectGUI;

public class ObjectPropertiesFrame extends JInternalFrame {
	
	private JPanel panel;
	private DocViewerPanel docPanel; 
	private Vector<AdjustmentPanel> adjusters;
	private MathObject object;
	private ObjectPropertiesFrame thisFrame;
	private MathObjectGUI objectGUI;
	JScrollPane scrollPane;
	
	public ObjectPropertiesFrame(DocViewerPanel dvp){
		super("tools",
				true, //resizable
				true, //closable
				false, //maximizable
				false);//iconifiable
		docPanel = dvp;
		panel = new JPanel();
		adjusters = new Vector<AdjustmentPanel>();
		scrollPane = new JScrollPane(panel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.getContentPane().add(scrollPane);
		thisFrame = this;
	}
	
	public JScrollPane getScrollPane(){
		return scrollPane;
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
				if ( docPanel.isInStudentMode() && mAtt.isStudentEditable() ||
						! docPanel.isInStudentMode())
				{// only show editing dialog if in teacher mode (not student)
					//or if the attribute has been left student editable
					if (mAtt instanceof BooleanAttribute){
						panel.add(new BooleanAdjustmentPanel((BooleanAttribute)mAtt, docPanel, panel), con);
					}
					else if (mAtt instanceof StringAttribute){
						panel.add(new StringAdjustmentPanel((StringAttribute)mAtt, docPanel, panel), con);
					}
					else if (mAtt instanceof ColorAttribute){
						panel.add(new ColorAdjustmentPanel((ColorAttribute)mAtt, docPanel, panel), con);
					}
					else{
						panel.add(new GenericAdjustmentPanel(mAtt, docPanel, panel), con);
					}
					con.gridy++;
				}
			}
		}
		return panel;
	}
	
	public void generatePanel(MathObject o){
		if (o == null){
			return;
		}
		object = o;
		panel.removeAll();
		adjusters.removeAllElements();
		this.setTitle(o.getClass().getSimpleName());
		JPanel actionPics = new JPanel();
		actionPics.setLayout(new GridLayout(0,3));
		JPanel otherActions = new JPanel();
		otherActions.setLayout(new GridLayout(0,1));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
		con.weightx = 1;
		con.weighty = 1;
		con.gridx = 0;
		con.gridy = 0;
		
		panel.add(actionPics, con);
		
		con.gridy = 1;
		panel.add(otherActions, con);
		
		ImageIcon pic;
		JButton button;
		if ( ! docPanel.isInStudentMode()){
			for (final String s : o.getActions()){
				pic = getIconForAction(s);
				if (pic != null){
					button = new JButton(pic);
					button.setToolTipText(s);
					actionPics.add(button);
				}
				else{
					button = new JButton(s);
					otherActions.add(button);
				}
				button.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						object.performAction(s);
						thisFrame.update();
						docPanel.repaint();
					}
					
				});
			}
		}
		con.gridy = 2;
		for (final String s : o.getStudentActions()){
			button = new JButton(s);
			panel.add(button, con);
			button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					object.performSpecialObjectAction(s);
					thisFrame.update();
					docPanel.repaint();
				}
				
			});
			con.gridy++;
		}
		for (MathObjectAttribute mAtt : o.getAttributes()){
			if ( ( docPanel.isInStudentMode() && mAtt.isStudentEditable() ) ||
					( ! docPanel.isInStudentMode() && mAtt.isUserEditable()) )
			{// only show editing dialog if in teacher mode (not student)
				//or if the attribute has been left student editable
				if (mAtt instanceof BooleanAttribute){
					adjusters.add(new BooleanAdjustmentPanel((BooleanAttribute)mAtt, docPanel, panel));
				}
				else if (mAtt instanceof StringAttribute){
					adjusters.add(new StringAdjustmentPanel(mAtt, docPanel, panel ));
				}
				else if (mAtt instanceof ColorAttribute){
					adjusters.add(new ColorAdjustmentPanel((ColorAttribute)mAtt, docPanel, panel ));
				}
				else{
					adjusters.add(new GenericAdjustmentPanel(mAtt, docPanel, panel));
				}
				panel.add(adjusters.get(adjusters.size() - 1), con);
				con.gridy++;
			}
		}
		panel.revalidate();
	}
	
	
	public ImageIcon getIconForAction(String actionName){
		String filename = null;
		if (actionName.equals(MathObject.MAKE_SQUARE)){
			filename = "img/makeSquare.png";
		}
		else if (actionName.equals(TriangleObject.MAKE_ISOSCELES_TRIANGLE)){
			filename = "img/makeIsosceles.png";
		}
		else if (actionName.equals(TriangleObject.MAKE_RIGHT_TRIANGLE)){
			filename = "img/makeRightTriangle.png";
		}
		else if (actionName.equals(PolygonObject.FLIP_HORIZONTALLY)){
			filename = "img/flipHorizontally.png";
		}
		else if (actionName.equals(PolygonObject.FLIP_VERTICALLY)){
			filename = "img/flipVertically.png";
		}
		else if (actionName.equals(GraphObject.ZOOM_IN)){
			filename = "img/smallZoomIn.png";
		}
		else if (actionName.equals(GraphObject.ZOOM_OUT)){
			filename = "img/smallZoomOut.png";
		}
		else{
			return null;
		}
		
		try {
			BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
			return new ImageIcon(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}
	
	private abstract class AdjustmentPanel extends JPanel{
		
		protected MathObjectAttribute mAtt;
		protected DocViewerPanel docPanel;
		protected JPanel parentPanel;
		
		protected boolean showingDialog;
		
		public AdjustmentPanel(final MathObjectAttribute mAtt, DocViewerPanel dvp, JPanel p){
			this.mAtt = mAtt;
			docPanel = dvp;
			parentPanel = p;
			addPanelContent();
		}
		
		public abstract void addPanelContent();
		
		public abstract void updateData();
	}
	
	private class GenericAdjustmentPanel extends AdjustmentPanel{

		private JTextField field;
		
		public GenericAdjustmentPanel(MathObjectAttribute mAtt,
				DocViewerPanel dvp, JPanel p) {
			super(mAtt, dvp, p);
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
			con.weightx = .1;
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
						if (mAtt.getParentObject() != null){
							mAtt.getParentObject().setAttributeValueWithString(
									mAtt.getName(), field.getText());
						}
						else{
							mAtt.setValueWithString(field.getText());
						}
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
		private JScrollPane scrollPane;
		
		public StringAdjustmentPanel(MathObjectAttribute mAtt,
				DocViewerPanel dvp, JPanel p) {
			super(mAtt, dvp, p);
		}

		@Override
		public void addPanelContent() {
			textArea = new JTextArea(3, 10);
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
							mAtt.getParentObject().setAttributeValue(mAtt.getName(), textArea.getText());
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
						String s = textArea.getText();
						s = s.substring(0,
								textArea.getCaretPosition() - 1) + s.substring(textArea.getCaretPosition());
						textArea.setText(s);
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
			con.weightx = .5;
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
			scrollPane = new JScrollPane(textArea);
			scrollPane.setWheelScrollingEnabled(false);
			scrollPane.addMouseWheelListener(new MouseWheelListener(){

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					// TODO Auto-generated method stub
					Point componentPoint = SwingUtilities.convertPoint(
                            scrollPane,
                            e.getPoint(),
                            parentPanel);
					parentPanel.dispatchEvent(new MouseWheelEvent(parentPanel,
                            e.getID(),
                            e.getWhen(),
                            e.getModifiers(),
                            componentPoint.x,
                            componentPoint.y,
                            e.getClickCount(),
                            e.isPopupTrigger(),
                            e.getScrollType(),
                            e.getScrollAmount(),
                            e.getWheelRotation()));
				}
				
			});
			add(scrollPane, con);
			
		}

		@Override
		public void updateData() {
			System.out.println(mAtt.getName());
			textArea.setText(mAtt.getValue().toString());
		}
		
	}
	
	private class BooleanAdjustmentPanel extends AdjustmentPanel{
		
		private JCheckBox checkbox;
		
		public BooleanAdjustmentPanel(BooleanAttribute mAtt,
				DocViewerPanel dvp, JPanel p) {
			super(mAtt, dvp, p);
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
	
	private class ColorAdjustmentPanel extends AdjustmentPanel{
		
		private JLabel colorLabel;
		private JCheckBox checkbox;
		private boolean justChangedColor;
		private JColorChooser colorChooser;
		
		public ColorAdjustmentPanel(ColorAttribute mAtt,
				DocViewerPanel dvp, JPanel p) {
			super(mAtt, dvp, p);
			justChangedColor = false;
			colorChooser = new JColorChooser();
		}

		@Override
		public void updateData() {
			colorLabel.setBackground(((ColorAttribute)mAtt).getValue());
			if (((ColorAttribute)mAtt).getValue() != null){
				checkbox.setSelected(true);
			}
			else{
				checkbox.setSelected(false);
			}
		}

		@Override
		public void addPanelContent() {
			setLayout(new GridBagLayout());
			
			checkbox = new JCheckBox("fill");
			
			if (((ColorAttribute)mAtt).getValue() != null){
				checkbox.setSelected(true);
			}
			else{
				checkbox.setSelected(false);
			}
			
			checkbox.addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (e.getStateChange() == ItemEvent.SELECTED){
						System.out.println(justChangedColor);
						if (justChangedColor){
							return;
						}
						mAtt.setValue(Color.WHITE);
						colorLabel.setBackground(((ColorAttribute)mAtt).getValue());
						ColorAdjustmentPanel.this.repaint();
						docPanel.repaintDoc();
						
					}
					else{
						mAtt.setValue(null);
						colorLabel.setBackground(((ColorAttribute)mAtt).getValue());
						ColorAdjustmentPanel.this.repaint();
						docPanel.repaintDoc();
					}
				}
				
			});
			colorLabel = new JLabel("    ");
			colorLabel.setBorder(new LineBorder(Color.BLACK));
			colorLabel.setOpaque(true);
			colorLabel.setBackground(((ColorAttribute)mAtt).getValue());
			JButton setColor = new JButton("set color");
			setColor.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					Color c = JColorChooser.showDialog(
		                     ColorAdjustmentPanel.this,
		                     "Choose color",
		                     ((ColorAttribute)mAtt).getValue());
					((ColorAttribute)mAtt).setValue(c);
					colorLabel.setBackground(((ColorAttribute)mAtt).getValue());
					if (((ColorAttribute)mAtt).getValue() != null){
						System.out.println(c);
						justChangedColor = true;
						checkbox.setSelected(true);
					}
					else{
						checkbox.setSelected(false);
					}
					docPanel.repaint();
				}
				
			});
			
			GridBagConstraints con = new GridBagConstraints();
			con.fill = GridBagConstraints.HORIZONTAL;
			con.weightx = 1;
			con.gridx = 0;
			con.gridy = 0;
			con.insets = new Insets(0, 3, 0, 3);
			add(checkbox, con);
			con.gridy = 0;
			con.gridx = 1;
			con.gridheight = 1;
			add(colorLabel, con);
			con.gridx = 2;
			add(setColor, con);
		}
		
	}
	
	public void update(){
		for (AdjustmentPanel a : adjusters){
			a.updateData();
		}
		this.repaint();
	}
	
	

}
