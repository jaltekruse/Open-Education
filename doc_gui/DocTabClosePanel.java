/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DocTabClosePanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NotebookPanel notebookPanel;
	private DocViewerPanel viewerPanel;
	private JTextField name;
	private JLabel docName;
	
	public DocTabClosePanel(NotebookPanel n, DocViewerPanel dvp){
		viewerPanel = dvp;
		notebookPanel = n;
		this.setMaximumSize(new Dimension(100, 20));
		this.setMinimumSize(new Dimension(200, 20));
		this.setLayout(new GridBagLayout());
		
		docName = new JLabel(dvp.getDoc().getName());
		name = new JTextField();
		name.setText(dvp.getDoc().getName());
		name.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				viewerPanel.getDoc().setFilename(name.getText());
				notebookPanel.requestFocus();
				notebookPanel.repaint();
			}
			
		});
		
		name.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.focusDocViewer(viewerPanel);
				notebookPanel.repaint();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		GridBagConstraints con = new GridBagConstraints();
		
		con.fill = GridBagConstraints.HORIZONTAL;
		con.weightx = 1;
		con.weighty = 1;
		con.gridx = 0;
		con.gridy = 0;
		con.insets = new Insets(0, 0, 0, 10);
		
		this.add(docName, con);

		
		JButton close = new JButton("x");
		close.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.closeDocViewer(viewerPanel);
			}
			
		});
		
		con.fill = GridBagConstraints.NONE;
		con.gridx = 1;
		con.weightx = 0;
		con.insets = new Insets(3, 0, 0, 0);
		this.add(close, con);
		
		this.setOpaque(false);
	}
	
	public void updateField(){
		docName.setText(viewerPanel.getDoc().getName());
	}

}
