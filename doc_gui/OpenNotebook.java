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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JApplet;

import javax.swing.JFileChooser;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = -5614406937717777249L;
	private static OpenNotebook notebook;
	private boolean inStudentMode;
	private static JFileChooser fileChooser;
	private static JFrame application;
	private DatabaseOfGroupedObjects database;
	private static NotebookPanel notebookPanel;
	public static final int ALIGN_DOCS_LEFT = 1, ALIGN_DOCS_RIGHT = 2, ALIGN_DOCS_CENTER = 3;
	private static String ourNodeName = "/doc_gui";
	private static Preferences prefs = Preferences.userRoot().node(ourNodeName);;
	private static final String DATABASE_PATH = "databasePath";
	private static final String BACKING_STORE_AVAIL = "BackingStoreAvail";

	private int docAlignment = ALIGN_DOCS_RIGHT;

	public OpenNotebook(){
		//create background resources here
		//GUI elements will be created in NotebookInterface
		//notebook will be created first and then passed into the GUI

		//a similar structure for file creation and then actual gui representation will likely
		//be used, so users will enter data like subjects covered, author, and modification date into a dialog
		//this data can be stored on the back-end and then the back-end data can be passed into the classes to
		//actually render the document
		//		addContents(this.getContentPane(), this);
		database = new DatabaseOfGroupedObjects();
	}

	public static void makeErrorDialog(String s){
		JOptionPane.showMessageDialog(null,
				s,
				"Error",
				JOptionPane.ERROR_MESSAGE);
	}

	private static void createAndShowGUI() {

		notebook = new OpenNotebook();
		application = new JFrame("OpenNotebook - Teacher");
		Dimension frameDim = new Dimension(950, 740);
		application.setPreferredSize(frameDim);

		application.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		application.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				quit();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		notebook.setInStudentMode(false);

		application.pack();
		application.setVisible(true);

		if ( ! preferencesDirectorySet()){
			setPreferencesDirectory();
		}
	}

	public static void quit(){
		Object[] options = {"Quit", "Cancel"};
		int n = JOptionPane.showOptionDialog(application,
				"If you have any unsaved changes they will be lost.\n" +
						"Are you sure you want to quit?",
						"Data May Be Lost",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null,
						options,
						options[1]);

		if (n == 0){ 
			notebookPanel.disposeOfSampledialog();
			application.dispose();
		}
		else{} // user clicked cancel, don't do anything
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

	public static boolean preferencesDirectorySet(){
		String path = prefs.get(DATABASE_PATH, null);
		if ( path == null){
			return false;
		}
		return true;
	}

	private static boolean backingStoreAvailable() {
		try {
			boolean oldValue = prefs.getBoolean(BACKING_STORE_AVAIL, false);
			prefs.putBoolean(BACKING_STORE_AVAIL, !oldValue);
			prefs.flush();
		} catch(BackingStoreException e) {
			return false;
		}
		return true;
	}

	public static void setPreferencesDirectory(){
		if ( ! backingStoreAvailable()){
			JOptionPane.showMessageDialog(null, "An error occured finding preferences,\n" +
					"your application will run with default settings.",
					"Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(null, "Open Notebook requires a directory to store your\n" +
				"preferences, please select one in the next dialog.",
				"Set Preferences Directory", JOptionPane.WARNING_MESSAGE);
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int success = fileChooser.showOpenDialog(application);
		if ( success == JFileChooser.APPROVE_OPTION){
			String newPath = fileChooser.getCurrentDirectory().getPath();
			prefs.put(DATABASE_PATH, newPath);
		}

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public int getDocAlignment() {
		return docAlignment;
	}

	public void setDocAlignment(int docAlignment) {
		if ( docAlignment == ALIGN_DOCS_LEFT || 
				docAlignment == ALIGN_DOCS_RIGHT || 
				docAlignment == ALIGN_DOCS_CENTER){
			this.docAlignment = docAlignment;
		}

	}
}
