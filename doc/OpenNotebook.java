/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tree.EvalException;
import tree.ParseException;
import tree.ValueNotStoredException;

import gui.MainApplet;
import gui.OCFrame;
import gui.TopLevelContainer;
import gui.TopLevelContainerOld;

/**
 * An OpenNotebook object keeps track of all of the background data needed for
 * the entire user interface. Actual display is handled by the classes created
 * inside of the associated NotebookFrame.
 * @author jason
 *
 */

public class OpenNotebook extends JApplet{
	
	public OpenNotebook(){
		//create background resources here
		//GUI elements will be created in NotebookInterface
		//notebook will be created first and then passed into the GUI
		
		//a similar structure for file creation and then actual gui representation will likely
		//be used, so users will enter data like subjects covered, author, and modification date into a dialog
		//this data can be stored on the back-end and then the back-end data can be passed into the classes to
		//actually render the document
		addContents(this.getContentPane(), this);
		
	}
	
	public static void makeErrorDialog(String s){
		JOptionPane.showMessageDialog(null,
			    s,
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	private static void createAndShowGUI() {

		OCFrame application = new NotebookFrame("OpenNotebook");
		Dimension frameDim = new Dimension(1000, 600);
		application.setPreferredSize(frameDim);
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		OpenNotebook notebook = new OpenNotebook();
		addContents(application.getContentPane(), notebook);
		
		application.pack();
		application.setVisible(true);
	}
	
	private static void addContents(Container c, OpenNotebook book){
		NotebookPanel notebookPanel = new NotebookPanel(book);
		
		c.setLayout(new GridBagLayout());
		GridBagConstraints bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = 1;
		bCon.weighty = 1;
		c.add(notebookPanel, bCon);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
