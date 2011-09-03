/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui;


import java.awt.Container;
import java.awt.Dimension;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JApplet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import doc.DatabaseOfGroupedObjects;


/**
 * An OpenNotebook object keeps track of all of the background data needed for
 * the entire user interface. Actual display is handled by the classes created
 * inside of the associated NotebookFrame.
 * @author jason
 *
 */

public class OpenNotebook extends JApplet{
	
	private boolean inStudentMode;
	private static JFrame application;
	private DatabaseOfGroupedObjects database;
	private NotebookPanel notebookPanel;
	
	public OpenNotebook(){
		//create background resources here
		//GUI elements will be created in NotebookInterface
		//notebook will be created first and then passed into the GUI
		
		//a similar structure for file creation and then actual gui representation will likely
		//be used, so users will enter data like subjects covered, author, and modification date into a dialog
		//this data can be stored on the back-end and then the back-end data can be passed into the classes to
		//actually render the document
		addContents(this.getContentPane(), this);
		database = new DatabaseOfGroupedObjects();
	}
	
	public static void makeErrorDialog(String s){
		JOptionPane.showMessageDialog(null,
			    s,
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	private static void createAndShowGUI() {

		OpenNotebook notebook = new OpenNotebook();
		application = new JFrame("OpenNotebook - Teacher");
		application.setJMenuBar(new NotebookMenuBar(notebook));
		Dimension frameDim = new Dimension(1000, 600);
		application.setPreferredSize(frameDim);
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Object[] options = {"Student", "Teacher"};
		int n = JOptionPane.showOptionDialog(application,
		    "Which mode would you like to run?",
		    "Mode Seletion",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[1]);

		if (n == 0){ 
			notebook.setInStudentMode(true);
		}
		else{
			notebook.setInStudentMode(false);
		}
		
		application.pack();
		application.setVisible(true);
	}
	
	private void addContents(Container c, OpenNotebook book){
		notebookPanel = new NotebookPanel(book);
		c.removeAll();
		
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

	public void setInStudentMode(boolean b) {
		inStudentMode = b;
		if (application == null){
			this.getContentPane().removeAll();
			this.addContents(this.getContentPane(), this);
			this.setJMenuBar(new NotebookMenuBar(this));
		}
		else{
			if (b)
				application.setTitle("OpenNotebook - Student");
			else
				application.setTitle("OpenNotebook - Teacher");
			application.getContentPane().removeAll();
			this.addContents(application.getContentPane(), this);
			application.setJMenuBar(new NotebookMenuBar(this));
		}
	}

	public boolean isInStudentMode() {
		return inStudentMode;
	}
	
	public DatabaseOfGroupedObjects getDatabase(){
		return database;
	}

	public void setNotebookPanel(NotebookPanel notebookPanel) {
		this.notebookPanel = notebookPanel;
	}

	public NotebookPanel getNotebookPanel() {
		return notebookPanel;
	}
}
