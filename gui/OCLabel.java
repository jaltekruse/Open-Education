/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OCLabel extends JPanel {
	
	String dispText;
	MainApplet mainApp;
	
	public OCLabel(String str, int gridwidth, int gridheight, int gridx,
			int gridy, double weightX, double weightY, JComponent comp, final MainApplet currmainApp) {
		dispText = str;
		mainApp = currmainApp;
		GridBagConstraints bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = .04;
		bCon.weighty = weightY;
		bCon.gridheight = gridheight;
		bCon.gridwidth = gridwidth;
		bCon.gridx = gridx;
		bCon.gridy = gridy;
		bCon.insets = new Insets(3, 6, 0, 0);
		this.repaint();
		comp.add(new JLabel(str), bCon);
	}
	
	public OCLabel(String str, int gridwidth, int gridheight, int gridx,
			int gridy, JComponent comp, final MainApplet currmainApp) {
		this(str, gridwidth, gridheight, gridx, gridy, 1, .2, comp, currmainApp);
	}
	
	public void paint(Graphics g){
		this.setPreferredSize(new Dimension(g.getFontMetrics().stringWidth(dispText), 20));
		int y = getSize().height;
		int x = getSize().width;
		g.setColor(Color.BLACK);
		g.drawString(dispText, 5, y/2);
	}
	
}
