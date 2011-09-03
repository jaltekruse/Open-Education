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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import doc.Document;
import doc.Page;
import doc.mathobjects.AnswerBoxObject;
import doc.mathobjects.CubeObject;
import doc.mathobjects.ExpressionObject;
import doc.mathobjects.GraphObject;
import doc.mathobjects.Grouping;
import doc.mathobjects.HexagonObject;
import doc.mathobjects.MathObject;
import doc.mathobjects.NumberLineObject;
import doc.mathobjects.OvalObject;
import doc.mathobjects.ParallelogramObject;
import doc.mathobjects.RectangleObject;
import doc.mathobjects.TextObject;
import doc.mathobjects.TrapezoidObject;
import doc.mathobjects.TriangleObject;
import doc.xml.DocReader;

/**
 * Used to display the main panel of the interface. Major components include a
 * toolbar at the top, a set of tabs for open documents, a document view
 * window and possibly a side panel for MathObjectGUI utilities.
 * 
 * @author jason
 *
 */
public class NotebookPanel extends SubPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private JFileChooser fileChooser;
	
	private OpenNotebook openNotebook;
	
	private Vector<DocViewerPanel> openDocs;
	
	private JTabbedPane docTabs;
	
	private Vector<DocTabClosePanel> tabLabels;
	
	private NotebookPanel thisNotebookPanel;
	
	private boolean justClosedTab;
	
	private MathObject clipBoardContents;
	
	public NotebookPanel (OpenNotebook openbook, Document doc){
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
		//create individual GUI elements here
		super(null);
		openNotebook = openbook;
		thisNotebookPanel = this;
		
		justClosedTab = false;
		
		setFileChooser(new JFileChooser());
		
		this.setLayout(new BorderLayout());
		
		this.addKeyboardShortcuts();
		
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

		if ( ! openNotebook.isInStudentMode()){
			JToolBar objectActions = new ObjectActionsToolBar(this);
			topToolBars.add(objectActions);
			
			JToolBar objectToolbar = new ObjectToolBar(this);
			add(objectToolbar, BorderLayout.SOUTH);
		}
		
		tabLabels = new Vector<DocTabClosePanel>();
		openDocs = new Vector<DocViewerPanel>();
		Document newDoc = new Document("Untitled Document");
		newDoc.addBlankPage();
		openDocs.add(new DocViewerPanel(newDoc, getTopLevelContainer(),
				openNotebook.isInStudentMode(), openNotebook));
		
		//change the value passed in to randomly add objects onto the screen
		randomlyAddObjects(0);
		
		int tabIndex = 0;
		for (DocViewerPanel d : openDocs){
			docTabs.addTab(d.getDoc().getName(), d);
			DocTabClosePanel temp = new DocTabClosePanel(this, d);
			tabLabels.add(temp);
			docTabs.setTabComponentAt(tabIndex, temp);
			tabIndex++;
		}
		
		docTabs.add(new JPanel(), "+");
		
		docTabs.addChangeListener( new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				int selected = 	docTabs.getSelectedIndex();
				String nameSelected = docTabs.getTitleAt(selected);
				if (nameSelected.equals("+"))
				{//add a new untitled document
					if (justClosedTab == false){
						Document tempDoc = new Document("Untitled Document");
						tempDoc.addBlankPage();
						addDoc(tempDoc);
					}
					else
					{// the last tab in the list was closed, set selected tab to new last tab
						docTabs.setSelectedIndex(docTabs.getTabCount() - 2);
					}
				}
			}
		});
		
	}

	public void cut(){
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null){
			setClipBoardContents(mObj);
			mObj.getParentPage().removeObject(mObj);
			getCurrentDocViewer().setFocusedObject(null);
			if (mObj == getCurrentDocViewer().getTempGroup()){
				getCurrentDocViewer().removeTempGroup();
			}
			getCurrentDocViewer().repaintDoc();
		}
	}
	
	public void copy(){
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null){
			setClipBoardContents(mObj.clone());
		}
	}
	
	public void paste(){
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (getClipBoardContents() == null){
			JOptionPane.showMessageDialog(null,
					"Clipboard is empty.",
				    "Empty Clipboard",
				    JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (mObj != null){
			MathObject newObj = getClipBoardContents().clone();
			newObj.setxPos(mObj.getxPos());
			newObj.setyPos(mObj.getyPos());
			newObj.setParentPage(mObj.getParentPage());
			mObj.getParentPage().addObject(newObj);
			getCurrentDocViewer().setFocusedObject(newObj);
		}
		else if (getCurrentDocViewer().getSelectedPage() != null){
			MathObject newObj = getClipBoardContents().clone();
			newObj.setParentPage(getCurrentDocViewer().getSelectedPage());
			getCurrentDocViewer().getSelectedPage().addObject(newObj);
			getCurrentDocViewer().setFocusedObject(newObj);
		}
		else{
			JOptionPane.showMessageDialog(null,
					"Please select a page, or an object on the desired page first.",
				    "Select Location for Paste",
				    JOptionPane.WARNING_MESSAGE);
			return;
		}
		getCurrentDocViewer().repaintDoc();
	}
	
	public void delete(){
		if (getCurrentDocViewer().getFocusedObject() != null){
			MathObject mObj = getCurrentDocViewer().getFocusedObject();
			if (mObj == getCurrentDocViewer().getTempGroup()){
				getCurrentDocViewer().removeTempGroup();
			}
			mObj.getParentPage().removeObject(mObj);
			getCurrentDocViewer().setFocusedObject(null);
			getCurrentDocViewer().repaintDoc();
		}
		else if ( getCurrentDocViewer().getSelectedPage() != null){
			getCurrentDocViewer().getDoc().removePage(
					getCurrentDocViewer().getSelectedPage());
			getCurrentDocViewer().resizeViewWindow();
		}
		else{
			JOptionPane.showMessageDialog(this,
				    "Please select a page or object first.",
				    "Error",
				    JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	}
	
	public void bringToFront(){
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj!= null){
			mObj.getParentPage().bringObjectToFront(mObj);
			getCurrentDocViewer().repaintDoc();
		}
	}
	
	public void bringToBack(){
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj!= null){
			mObj.getParentPage().bringObjectToBack(mObj);
			getCurrentDocViewer().repaintDoc();
		}
	}
	
	public void sendForward(){
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj!= null){
			mObj.getParentPage().sendObjectForward(mObj);
			getCurrentDocViewer().repaintDoc();
		}
	}
	
	public void sendBackward(){
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null){
			mObj.getParentPage().sendObjectBackward(mObj);
			getCurrentDocViewer().repaintDoc();
		}
	}
	
	public void group(){
		if (getCurrentDocViewer().getFocusedObject() != null){
			MathObject mObj = getCurrentDocViewer().getFocusedObject();
			if (mObj == getCurrentDocViewer().getTempGroup()){
				getCurrentDocViewer().removeTempGroup();
				mObj.getParentPage().addObject(mObj);
				getCurrentDocViewer().repaintDoc();
			}

		}
	}
	
	public void ungroup(){
		MathObject mObj = getCurrentDocViewer().getFocusedObject();
		if (mObj != null){
			if (mObj instanceof Grouping){
				if (mObj == getCurrentDocViewer().getTempGroup()){
					getCurrentDocViewer().resetTempGroup();
					getCurrentDocViewer().setFocusedObject(null);
					getCurrentDocViewer().repaintDoc();
					return;
				}
				else{
					((Grouping)mObj).unGroup();
					mObj.getParentPage().removeObject(mObj);
					getCurrentDocViewer().setFocusedObject(null);
					getCurrentDocViewer().repaintDoc();
				}
			}

		}
	}
	
	public void save(){
		BufferedWriter f;
		try {
			int value = getFileChooser().showSaveDialog(this);
			if (value == JFileChooser.APPROVE_OPTION){
				java.io.File file = getFileChooser().getSelectedFile();
				getCurrentDocViewer().getDoc().setFilename(file.getName());
				refreshDocNameTabs();
				f = new BufferedWriter(new FileWriter(file));
				f.write(getCurrentDocViewer().getDoc().exportToXML());
				f.flush();
				f.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					 "Error saving file",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void open(){
		DocReader reader = new DocReader();
		try {
			int value = getFileChooser().showOpenDialog(this);
			if (value == JFileChooser.APPROVE_OPTION){
				addDoc(reader.readFile(
						getFileChooser().getSelectedFile()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
				    "Error opening file, please send it to the lead developer at " +
				    "altekruse@wisc.edu to help with debugging",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void addPage(){
		Document doc = getCurrentDocViewer().getDoc();
		Page p = getCurrentDocViewer().getSelectedPage();
		if (p != null){
			int index = 0;
			try {
				index = doc.getPageIndex(p);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			doc.getPages().add(index, new Page(doc));
		}
		else{
			getCurrentDocViewer().getDoc().addBlankPage();
		}
		getCurrentDocViewer().resizeViewWindow();
	}
	
	public void deletePage(){
		if (getCurrentDocViewer().getSelectedPage() == null){
			JOptionPane.showMessageDialog(this,
				    "Please select a page first.",
				    "Error",
				    JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		getCurrentDocViewer().getDoc().removePage(
				getCurrentDocViewer().getSelectedPage());
		getCurrentDocViewer().resizeViewWindow();
	}
	
	public void showDocProperties(){
		getCurrentDocViewer().toggleDocPropsFrame();
	}
	
	public void print(){
		DocPrinter docPrinter = new DocPrinter();
		
		docPrinter.setDoc(getCurrentDocViewer().getDoc());
		PrinterJob job = PrinterJob.getPrinterJob();
        
		Paper paper = new Paper();
        paper.setImageableArea(Page.DEFAULT_MARGIN, Page.DEFAULT_MARGIN,
        		Page.DEFAULT_PAGE_WIDTH - 2 * Page.DEFAULT_MARGIN
        		, Page.DEFAULT_PAGE_HEIGHT - 2 * Page.DEFAULT_MARGIN);
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
	
	public void zoomIn(){
		getCurrentDocViewer().zoomIn();
	}
	
	public void zoomOut(){
		getCurrentDocViewer().zoomOut();
	}
	
	public void undo(){
		
	}
	
	public void redo(){
		
	}
	
	public void addKeyboardShortcuts(){
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE")
				, "delete");
		this.getActionMap().put("delete", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				delete();
			}
		});     
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S")
				, "save");
		this.getActionMap().put("save", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				save();
			}
		});  
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control O")
				, "open");
		this.getActionMap().put("open", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				open();
			}
		});  
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control P")
				, "print");
		this.getActionMap().put("print", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				print();
			}
		});
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control C")
				, "copy");
		this.getActionMap().put("copy", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				copy();
			}
		});  
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control X")
				, "cut");
		this.getActionMap().put("cut", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cut();
			}
		});
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V")
				, "paste");
		this.getActionMap().put("paste", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				paste();
			}
		});
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control N")
				, "add page");
		this.getActionMap().put("add page", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				addPage();
			}
		});
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z")
				, "undo");
		this.getActionMap().put("undo", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				undo();
			}
		});
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y")
				, "redo");
		this.getActionMap().put("redo", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				redo();
			}
		});
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control")
				, "controlPressed");
		this.getActionMap().put("controlPressed", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("ctrl pressed");
			}
		});
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control EQUALS")
				, "zoomIn");
		this.getActionMap().put("zoomIn", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				zoomIn();
			}
		});
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control MINUS")
				, "zoomOut");
		this.getActionMap().put("zoomOut", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				zoomOut();
			}
		});
	}
	
	public void refreshDocNameTabs(){
		for (DocTabClosePanel p : tabLabels){
			p.updateField();
		}
	}
	
	public boolean isInStudentMode(){
		return openNotebook.isInStudentMode();
	}
	
	public String getWebPage(String user, String pass){

		String page= "";
		try {
			// Construct data
			String data = URLEncoder.encode("user_session[login]", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
			data += "&" + URLEncoder.encode("user_session[password]", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");

			// Send data
			URL url = new URL("http://24.131.169.33:3000/create");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				page += line;
			}
			wr.close();
			rd.close();
		} catch (Exception e) {

		}
		return page;
	}
	
	public void addDoc(Document doc){
		int numTabs = docTabs.getTabCount();
		openDocs.add(new DocViewerPanel(doc, getTopLevelContainer(),
				openNotebook.isInStudentMode(), openNotebook));
		
		//remove the old adding new doc tab
		docTabs.remove(numTabs - 1);
		
		//add new doc
		docTabs.add(openDocs.get(openDocs.size() - 1),
				openDocs.get(openDocs.size()-1).getDoc().getName());
		DocTabClosePanel temp = new DocTabClosePanel(thisNotebookPanel,
				openDocs.get(openDocs.size() - 1));
		tabLabels.add(temp);
		docTabs.setTabComponentAt(openDocs.size()-1, temp);
		
		//add back creation tab
		docTabs.add(new JPanel(), "+");
		docTabs.setSelectedIndex(docTabs.getTabCount() - 2);
	}
	
	public DocViewerPanel getCurrentDocViewer(){
		return openDocs.get(docTabs.getSelectedIndex());
	}
	
	public ImageIcon getIcon(String filename){
		try {
			BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
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
	public void closeDocViewer(DocViewerPanel docPanel){
		if (openDocs.size() == 1){
			return;
		}
		openDocs.remove(docPanel);
		justClosedTab = true;
		docTabs.remove(docPanel);
		justClosedTab = false;
	}
	
	public void focusDocViewer(DocViewerPanel docPanel){
		docTabs.setSelectedComponent(docPanel);
	}
	
	public void randomlyAddObjects(int n){
		
		try {
			
			//used to store temporary random type of object to add
			int objType = 0;
			int xPos, yPos, xSize, ySize, thickness, doc, page;
			Random rand = new Random();
			MathObject objectToAdd;
			
			//message to show in each textObject
			String text = "hello there";
			
			//loop to randomly generate objects on the open documents
			for (int i = 0; i < n; i++){
				
				objType = rand.nextInt(5);
				xPos = rand.nextInt(300) + 5;
				yPos = rand.nextInt(550) + 5;
				xSize = rand.nextInt(200) + 100;
				ySize = rand.nextInt(200) + 100;
				thickness = rand.nextInt(10) + 1;
				doc = rand.nextInt(openDocs.size());
				page = rand.nextInt(openDocs.get(doc).getDoc().getNumPages() - 1) + 1;
				
				if (objType == 0){
					objectToAdd = new RectangleObject(openDocs.get(doc).getDoc().getPage(page),
							xPos, yPos, xSize, ySize, thickness);
				}
				else if (objType == 1){
					objectToAdd = new OvalObject(openDocs.get(doc).getDoc().getPage(page),
							xPos, yPos, xSize, ySize, thickness);
				}
				else if (objType == 2){
					objectToAdd = new TextObject(openDocs.get(doc).getDoc().getPage(page),
							xPos, yPos, xSize, ySize, thickness, text);
				}
				else if (objType == 3){
					objectToAdd = new TriangleObject(openDocs.get(doc).getDoc().getPage(page),
							xPos, yPos, xSize, ySize, thickness);
				}
				else{
					objectToAdd = new GraphObject(openDocs.get(doc).getDoc().getPage(page),
							xPos, yPos, xSize, ySize);
				}
				if ( ! openDocs.get(doc).getDoc().getPage(page).addObject(objectToAdd))
				{// attempt to add was not successful
					i--;
				}
			}
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
