/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class OCButton extends JButton {

	/**
	 * An OCButton is the standard object for a button on the interface. The
	 * constructor takes various attributes for using a GridBagConstraints
	 * object. Its action can be changed by overriding the associatedAction
	 * method.
	 */
	private static final long serialVersionUID = 1L;
	private String s;
	private GridBagConstraints bCon;
	private JComponent comp;

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
				associatedAction();
			}
		});

		comp.add(this, bCon);
	}

	public OCButton(Icon bi, String tip, int gridwidth, int gridheight,
			int gridx, int gridy, JComponent comp) {

		super(bi);
		this.comp = comp;
		this.setToolTipText(tip);
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
				associatedAction();
			}
		});

		comp.add(this, bCon);
	}

	public OCButton(boolean hasInsets, String str, int gridwidth,
			int gridheight, int gridx, int gridy, JComponent comp) {

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
		if (hasInsets) {
			bCon.insets = new Insets(2, 2, 2, 2);
		}
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				associatedAction();
			}
		});

		comp.add(this, bCon);
	}

	public OCButton(String str, String tip, int gridwidth, int gridheight,
			int gridx, int gridy, JComponent comp) {

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
				associatedAction();
			}
		});

		this.setToolTipText(tip);

		comp.add(this, bCon);
	}

	public void removeInsets() {
		comp.remove(this);
		bCon.insets = null;
		comp.add(this, bCon);
	}

	public void associatedAction() {
	}

}
