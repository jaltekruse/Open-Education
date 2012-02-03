package doc_gui.attribute_panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import doc.attributes.ColorAttribute;
import doc_gui.DocViewerPanel;


public class ColorAdjustmentPanel extends AdjustmentPanel<ColorAttribute>{

	private JLabel colorLabel;
	private JCheckBox checkbox;
	private boolean justChangedColor;
	private JColorChooser colorChooser;
	private JButton setColor;

	public ColorAdjustmentPanel(ColorAttribute mAtt,
			DocViewerPanel dvp, JPanel p) {
		super(mAtt, dvp, p);
		justChangedColor = false;
		colorChooser = new JColorChooser();
	}

	@Override
	public void updateData() {
		colorLabel.setBackground(mAtt.getValue());
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

		if (mAtt.getValue() != null){
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
					if (justChangedColor){
						return;
					}
					mAtt.setValue(Color.WHITE);
					colorLabel.setBackground(mAtt.getValue());
					ColorAdjustmentPanel.this.repaint();
					docPanel.addUndoState();
					docPanel.repaintDoc();
					docPanel.updateObjectToolFrame();

				}
				else{
					mAtt.setValue(null);
					colorLabel.setBackground(mAtt.getValue());
					ColorAdjustmentPanel.this.repaint();
					docPanel.addUndoState();
					docPanel.repaintDoc();
					docPanel.updateObjectToolFrame();
				}
			}

		});
		colorLabel = new JLabel("    ");
		colorLabel.setBorder(new LineBorder(Color.BLACK));
		colorLabel.setOpaque(true);
		colorLabel.setBackground(mAtt.getValue());
		setColor = new JButton("set color");
		setColor.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Color c = JColorChooser.showDialog(
						ColorAdjustmentPanel.this,
						"Choose color",
						((ColorAttribute)mAtt).getValue());
				((ColorAttribute)mAtt).setValue(c);
				colorLabel.setBackground(mAtt.getValue());
				if (((ColorAttribute)mAtt).getValue() != null){
					justChangedColor = true;
					checkbox.setSelected(true);
				}
				else{
					checkbox.setSelected(false);
				}
				docPanel.addUndoState();
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

	@Override
	public void applyPanelValueToObject() {
		// this is not applicable for this class, as it is set with a popup
	}

	@Override
	public void focusAttributField() {
		// TODO Auto-generated method stub
		setColor.requestFocus();
	}

}

