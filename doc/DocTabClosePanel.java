/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class DocTabClosePanel extends JPanel{
	
	private NotebookPanel notebookPanel;
	private JPanel thisPanel;
	private DocViewerPanel viewerPanel;
	private JTextField name;
	
	public DocTabClosePanel(NotebookPanel n, DocViewerPanel dvp){
		viewerPanel = dvp;
		notebookPanel = n;
		thisPanel = this;
		this.setMaximumSize(new Dimension(100, 20));
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints con = new GridBagConstraints();
		
		con.fill = GridBagConstraints.HORIZONTAL;
		con.weightx = 1;
		con.gridx = 0;
		name = new JTextField();
		name.setText(dvp.getDoc().getName());
		name.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				viewerPanel.getDoc().setFilename(name.getText());
				notebookPanel.requestFocus();
				notebookPanel.revalidate();
			}
			
		});
		
		name.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				notebookPanel.focusDocViewer(viewerPanel);
				notebookPanel.revalidate();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		this.add(name);

		
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
		this.add(close);
		
		this.setOpaque(false);
	}
	
	public void updateField(){
		
	}

}
