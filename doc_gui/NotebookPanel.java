/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import doc.Document;
import doc.Page;
import doc.attributes.AttributeException;
import doc.mathobjects.GraphObject;
import doc.mathobjects.Grouping;
import doc.mathobjects.MathObject;
import doc.mathobjects.GeneratedProblem;
import doc.mathobjects.OvalObject;
import doc.mathobjects.ProblemGenerator;
import doc.mathobjects.ProblemObject;
import doc.mathobjects.RectangleObject;
import doc.mathobjects.TextObject;
import doc.mathobjects.TriangleObject;
import doc.xml.DocReader;

/**
 * Used to display the main panel of the interface. Major components include a
 * toolbar at the top, a set of tabs for open documents, a document view window
 * and possibly a side panel for MathObjectGUI utilities.
 * 
 * @author jason
 * 
 */
public class NotebookPanel extends SubPanel {

	private JFileChooser fileChooser;
	private OpenNotebook openNotebook;
	private Vector<DocViewerPanel> openDocs;
	private JTabbedPane docTabs;
	private Vector<DocTabClosePanel> tabLabels;
	private NotebookPanel thisNotebookPanel;
	private DocReader reader;

	// flag to track if the last action that was performed was closing a
	// tab, if the user closes the tab just before the "+" (add new doc)
	// tab the "+" tab gains focus, without this flag, that would prompt
	// a new document to be opened immediately after closing one
	private boolean justClosedTab;
	private MathObject clipBoardContents;
	JDialog sampleDialog;
	public static final String UNTITLED_DOC = "Untitled Doc",
			VIEW_PROBLEM_FORUMLA_MESSAGE = "This document was created for viewing a problem "
					+ "formula from one of your other documents. "
					+ "You can modify the formula here and then copy it back onto your other document to generate "
					+ "new problems. If you wish to move objects around, or add something new to the formula, "
					+ "you can create a text object to temporarily store its script data, as it is destroyed "
					+ "when you use the \"Remove problem\" function to ungroup the objects in your problem. "
					+ "Keep in mind that if you just update the problem on this document the changes will not "
					+ "be applied to your generated problems. You must delete the old ones from your other "
					+ "document(s) and copy your modified formula over to generate new ones.";

	public NotebookPanel(OpenNotebook openbook, Document doc) {
		super(null);
		openNotebook = openbook;

		this.setLayout(new GridBagLayout());

		setFileChooser(new JFileChooser());

		JTabbedPane docTabs = new JTabbedPane();
		GridBagConstraints bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = 1;
		bCon.weighty = 1;
		this.add(docTabs, bCon);

		tabLabels = new Vector<DocTabClosePanel>();
		openDocs = new Vector<DocViewerPanel>();
		openDocs.add(new DocViewerPanel(doc, getTopLevelContainer(),
				openNotebook.isInStudentMode(), openNotebook));
		docTabs.add(openDocs.get(0), openDocs.get(0).getDoc().getName());

	}

	public NotebookPanel(OpenNotebook openbook) {
		// create individual GUI elements here
		super(null);
		openNotebook = openbook;
		thisNotebookPanel = this;

		justClosedTab = false;

		setFileChooser(new JFileChooser());
		reader = new DocReader();
		createSampleDialog();

		this.setLayout(new BorderLayout());

		KeyboardShortcuts.addKeyboardShortcuts(this);

		docTabs = new JTabbedPane();
		docTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		add(docTabs, BorderLayout.CENTER);

		JPanel topToolBars = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		topToolBars.setLayout(layout);

		JToolBar fileActions = new FileActionsToolBar(this);
		topToolBars.add(fileActions);

		add(topToolBars, BorderLayout.NORTH);
		this.addKeyListener(new NotebookKeyboardListener(this));

		if (!openNotebook.isInStudentMode()) {
			JToolBar objectActions = new ObjectActionsToolBar(this);
			topToolBars.add(objectActions);

			JToolBar objectToolbar = new ObjectToolBar(this);
			add(objectToolbar, BorderLayout.SOUTH);
		}

		tabLabels = new Vector<DocTabClosePanel>();
		openDocs = new Vector<DocViewerPanel>();
		Document newDoc = new Document(UNTITLED_DOC);
		newDoc.addBlankPage();
		openDocs.add(new DocViewerPanel(newDoc, getTopLevelContainer(),
				openNotebook.isInStudentMode(), openNotebook));

		int tabIndex = 0;
		for (DocViewerPanel d : openDocs) {
			docTabs.addTab(d.getDoc().getName(), d);
			DocTabClosePanel temp = new DocTabClosePanel(this, d);
			tabLabels.add(temp);
			docTabs.setTabComponentAt(tabIndex, temp);
			tabIndex++;
		}

		docTabs.add(new JPanel(), "+");

		docTabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (openDocs.size() == 0) {
					return;
				}
				int selected = docTabs.getSelectedIndex();
				String nameSelected = docTabs.getTitleAt(selected);
				if (nameSelected.equals("+")) {// add a new untitled document
					if (justClosedTab == false) {
						Document tempDoc = new Document(UNTITLED_DOC);
						tempDoc.addBlankPage();
						addDoc(tempDoc);
					} else {// the last tab in the list was closed, set selected
							// tab to new last tab
						docTabs.setSelectedIndex(docTabs.getTabCount() - 2);
					}
				}
			}
		});

	}

	public void setDocAlignment(int alignment) {
		openNotebook.setDocAlignment(alignment);
		getCurrentDocViewer().repaintDoc();
	}

	public void setPreferencesDirectory() {
		openNotebook.setPreferencesDirectory();
	}

	public void cut() {
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null) {
			setClipBoardContents(mObj);
			mObj.getParentContainer().removeObject(mObj);
			getCurrentDocViewer().setFocusedObject(null);
			if (mObj == getCurrentDocViewer().getTempGroup()) {
				getCurrentDocViewer().removeTempGroup();
			}
			getCurrentDocViewer().repaintDoc();
		}
	}

	public void copy() {
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null) {
			setClipBoardContents(mObj.clone());
		}
	}

	public void paste() {
		MathObject focusedObj = getCurrentDocViewer().getFocusedObject();
		if (getClipBoardContents() == null) {
			JOptionPane.showMessageDialog(null, "Clipboard is empty.",
					"Empty Clipboard", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (focusedObj != null) {
			MathObject newObj = getClipBoardContents().clone();
			newObj.setxPos(focusedObj.getxPos());
			newObj.setyPos(focusedObj.getyPos());
			newObj.setParentContainer(focusedObj.getParentContainer());
			focusedObj.getParentContainer().addObject(newObj);
			getCurrentDocViewer().setFocusedObject(newObj);
		} else if (getCurrentDocViewer().getSelectedPage() != null) {
			MathObject newObj = getClipBoardContents().clone();
			newObj.setParentContainer(getCurrentDocViewer().getSelectedPage());
			getCurrentDocViewer().getSelectedPage().addObject(newObj);
			getCurrentDocViewer().setFocusedObject(newObj);
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							"Please select a page, or an object on the desired page first.",
							"Select Location for Paste",
							JOptionPane.WARNING_MESSAGE);
			return;
		}
		getCurrentDocViewer().repaintDoc();
	}

	public void quit() {
		OpenNotebook.quit();
	}

	public void delete() {
		if (getCurrentDocViewer().getFocusedObject() != null) {
			MathObject mObj = getCurrentDocViewer().getFocusedObject();
			if (mObj == getCurrentDocViewer().getTempGroup()) {
				getCurrentDocViewer().removeTempGroup();
			}
			mObj.getParentContainer().removeObject(mObj);
			mObj.setParentContainer(null);
			mObj.setJustDeleted(true);
			getCurrentDocViewer().setFocusedObject(null);
			getCurrentDocViewer().repaintDoc();
		} else if (getCurrentDocViewer().getSelectedPage() != null) {
			deletePage();
		} else {
			JOptionPane.showMessageDialog(this,
					"Please select a page or object first.", "Error",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	}

	public void bringToFront() {
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null) {
			mObj.getParentPage().bringObjectToFront(mObj);
			getCurrentDocViewer().repaintDoc();
		}
	}

	public void bringToBack() {
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null) {
			mObj.getParentPage().bringObjectToBack(mObj);
			getCurrentDocViewer().repaintDoc();
		}
	}

	public void sendForward() {
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null) {
			mObj.getParentPage().sendObjectForward(mObj);
			getCurrentDocViewer().repaintDoc();
		}
	}

	public void sendBackward() {
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null) {
			mObj.getParentPage().sendObjectBackward(mObj);
			getCurrentDocViewer().repaintDoc();
		}
	}

	public void generateWorksheet() {
		Vector<Page> docPages = getCurrentDocViewer().getDoc().getPages();
		Vector<MathObject> pageObjects;
		Page p;
		MathObject mObj;
		int oldSize;
		boolean generatedProblem = false;

		for (int i = 0; i < docPages.size(); i++) {
			pageObjects = docPages.get(i).getObjects();
			oldSize = pageObjects.size();
			for (int j = 0; j < oldSize; j++) {
				mObj = pageObjects.get(j);
				if (mObj instanceof GeneratedProblem) {
					((GeneratedProblem) mObj).generateNewProblem();
					generatedProblem = true;
					j--;
					oldSize--;
				}
			}
		}
		getCurrentDocViewer().repaintDoc();
		if (!generatedProblem) {
			JOptionPane
					.showMessageDialog(
							null,
							"No generated problems were found to replace. To see some\ngenerated problems try opening a sample document.",
							"No Problems Generated",
							JOptionPane.WARNING_MESSAGE);
		}
	}

	public void group() {
		if (getCurrentDocViewer().getFocusedObject() != null) {
			MathObject mObj = getCurrentDocViewer().getFocusedObject();
			if (mObj == getCurrentDocViewer().getTempGroup()) {
				getCurrentDocViewer().getTempGroup().getParentContainer()
						.removeObject(mObj);
				MathObject newGroup = getCurrentDocViewer().getTempGroup()
						.clone();
				mObj.getParentContainer().addObject(newGroup);
				getCurrentDocViewer().resetTempGroup();
				// make sure this comes after the call to resetTempGroup, when a
				// new focused object is set
				// it places the objects in the temp group back on the page,
				// which is not what is needed here
				getCurrentDocViewer().setFocusedObject(newGroup);
				getCurrentDocViewer().repaintDoc();
			}

		}
	}

	public void viewProblemGnerator(ProblemGenerator probGen) {
		Document newDoc = new Document("Problem Generator");
		newDoc.addBlankPage();
		TextObject textObj = new TextObject(newDoc.getPage(0), 5 + newDoc
				.getPage(0).getyMargin(), 5 + newDoc.getPage(0).getxMargin(),
				newDoc.getPage(0).getWidth() - 2
						* newDoc.getPage(0).getxMargin(), 100, 12,
				VIEW_PROBLEM_FORUMLA_MESSAGE);
		try {
			textObj.setAttributeValue(TextObject.SHOW_BOX, false);
		} catch (AttributeException e) {
			// TODO Auto-generated catch block
			// should not happen
		}
		newDoc.getPage(0).addObject(textObj);
		MathObject newProb = ((MathObject) probGen).clone();
		newProb.setParentContainer(newDoc.getPage(0));
		newProb.setyPos(200);
		newProb.setxPos(80 + newDoc.getPage(0).getxMargin());
		newDoc.getPage(0).addObject(newProb);
		this.addDoc(newDoc);
	}

	public void ungroup() {
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null) {
			if (mObj instanceof Grouping) {
				if (mObj == getCurrentDocViewer().getTempGroup()) {
					getCurrentDocViewer().ungroupTempGroup();
					getCurrentDocViewer().setFocusedObject(null);
					getCurrentDocViewer().repaintDoc();
					return;
				} else {
					((Grouping) mObj).unGroup();
					mObj.getParentContainer().removeObject(mObj);
					getCurrentDocViewer().setFocusedObject(null);
					getCurrentDocViewer().repaintDoc();
				}
			}

		}
	}

	public void save() {
		BufferedWriter f = null;
		try {
			getFileChooser().setSelectedFile(
					new File(getCurrentDocViewer().getDoc().getName()));
			int value = getFileChooser().showSaveDialog(this);
			if (value == JFileChooser.APPROVE_OPTION) {
				java.io.File file = getFileChooser().getSelectedFile();
				getCurrentDocViewer().getDoc().setFilename(file.getName());
				refreshDocNameTabs();
				f = new BufferedWriter(new FileWriter(file));
				f.write(getCurrentDocViewer().getDoc().exportToXML());
				f.flush();
				f.close();
			}
		} catch (Exception e) {
			try {
				f.flush();
				f.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JOptionPane.showMessageDialog(null, "Error saving file", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void open() {
		try {
			int value = getFileChooser().showOpenDialog(this);
			if (value == JFileChooser.APPROVE_OPTION) {
				FileReader fileReader = new FileReader(getFileChooser()
						.getSelectedFile());
				addDoc(reader.readFile(fileReader, getFileChooser()
						.getSelectedFile().getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error opening file, please send it to the lead developer at\n"
							+ "dev@open-math.com to help with debugging",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void createSampleDialog() {
		sampleDialog = new JDialog();
		sampleDialog.add(new SampleListPanel(this));
		sampleDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		sampleDialog.pack();
	}

	public void setSampleDialogVisible(boolean b) {
		sampleDialog.setVisible(b);
		if (b) {
			sampleDialog.setBounds(this.getX() + 350, this.getY() + 200, 300,
					300);
		}
	}

	public void disposeOfSampledialog() {
		sampleDialog.dispose();
	}

	public void open(String docName) {

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream("samples/" + docName);
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		try {
			Document tempDoc = reader.readFile(inputStreamReader, docName);
			tempDoc.setFilename(docName);
			addDoc(tempDoc);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error opening file, please send it to the lead developer at\n"
							+ "dev@open-math.com to help with debugging",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void addPage() {
		Document doc = getCurrentDocViewer().getDoc();
		Page p = getCurrentDocViewer().getSelectedPage();

		if (doc.getNumPages() > 0 && p != null) {
			int index = 0;
			index = doc.getPageIndex(p);
			doc.getPages().add(index, new Page(doc));
		} else {
			getCurrentDocViewer().getDoc().addBlankPage();
		}
		getCurrentDocViewer().resizeViewWindow();
	}

	public void deletePage() {
		if (getCurrentDocViewer().getSelectedPage() == null) {
			JOptionPane.showMessageDialog(this, "Please select a page first.",
					"Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		getCurrentDocViewer().getDoc().removePage(
				getCurrentDocViewer().getSelectedPage());
		getCurrentDocViewer().setSelectedPage(null);
		getCurrentDocViewer().resizeViewWindow();
	}

	public void showDocProperties() {
		getCurrentDocViewer().toggleDocPropsFrame();
	}

	public void print() {
		DocPrinter docPrinter = new DocPrinter();

		docPrinter.setDoc(getCurrentDocViewer().getDoc());
		PrinterJob job = PrinterJob.getPrinterJob();

		Paper paper = new Paper();
		paper.setImageableArea(Page.DEFAULT_MARGIN, Page.DEFAULT_MARGIN,
				Page.DEFAULT_PAGE_WIDTH - 2 * Page.DEFAULT_MARGIN,
				Page.DEFAULT_PAGE_HEIGHT - 2 * Page.DEFAULT_MARGIN);
		paper.setSize(Page.DEFAULT_PAGE_WIDTH, Page.DEFAULT_PAGE_HEIGHT);

		PageFormat pf = job.defaultPage();

		pf.setPaper(paper);

		job.setPrintable(docPrinter, pf);
		boolean ok = job.printDialog();
		if (ok) {
			try {
				job.print();
			} catch (PrinterException ex) {
				/* The job did not successfully complete */
			}
		}
	}

	public void zoomIn() {
		getCurrentDocViewer().zoomIn();
	}

	public void defaultZoom() {
		getCurrentDocViewer().defaultZoom();
	}

	public void zoomOut() {
		getCurrentDocViewer().zoomOut();
	}

	public void undo() {

	}

	public void redo() {

	}

	public void refreshDocNameTabs() {
		for (DocTabClosePanel p : tabLabels) {
			p.updateField();
		}
	}

	public boolean isInStudentMode() {
		return openNotebook.isInStudentMode();
	}

	public void addDoc(Document doc) {
		int numTabs = docTabs.getTabCount();
		openDocs.add(new DocViewerPanel(doc, getTopLevelContainer(),
				openNotebook.isInStudentMode(), openNotebook));

		// remove the old adding new doc tab
		docTabs.remove(numTabs - 1);

		// add new doc
		docTabs.add(openDocs.get(openDocs.size() - 1),
				openDocs.get(openDocs.size() - 1).getDoc().getName());
		DocTabClosePanel temp = new DocTabClosePanel(thisNotebookPanel,
				openDocs.get(openDocs.size() - 1));
		tabLabels.add(temp);
		docTabs.setTabComponentAt(openDocs.size() - 1, temp);

		// add back creation tab
		docTabs.add(new JPanel(), "+");
		docTabs.setSelectedIndex(docTabs.getTabCount() - 2);
	}

	public DocViewerPanel getCurrentDocViewer() {
		return openDocs.get(docTabs.getSelectedIndex());
	}

	public ImageIcon getIcon(String filename) {
		try {
			BufferedImage image = ImageIO.read(getClass().getClassLoader()
					.getResourceAsStream(filename));
			return new ImageIcon(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("cannot find image " + filename);
		}
		return null;
	}

	/**
	 * Used to remove an editor from the tabs, should be modified to ask if user
	 * wants to save first.
	 */
	public void closeDocViewer(DocViewerPanel docPanel) {
		if (openDocs.size() == 1) {
			return;
		}

		// open popup to see ask if user wants to save recent changes
		Object[] options = { "Close", "Cancel" };
		int n = JOptionPane.showOptionDialog(this,
				"If you have any unsaved changes they will be lost.\n"
						+ "Would you like to continue closing this tab?",
				"Close Tab", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null, options, options[1]);

		if (n == 1) {// the user clicked cancel, do not close the tab
			return;
		}

		openDocs.remove(docPanel);
		justClosedTab = true;
		docTabs.remove(docPanel);
		justClosedTab = false;
	}

	public void focusDocViewer(DocViewerPanel docPanel) {
		docTabs.setSelectedComponent(docPanel);
	}

	public void setFileChooser(JFileChooser fileChooser) {
		this.fileChooser = fileChooser;
	}

	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public void setClipBoardContents(MathObject clipBoardContents) {
		this.clipBoardContents = clipBoardContents;
	}

	public MathObject getClipBoardContents() {
		return clipBoardContents;
	}
}
