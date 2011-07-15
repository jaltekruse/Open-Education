/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import doc.mathobjects.CubeObject;
import doc.mathobjects.ExpressionObject;
import doc.mathobjects.GraphObject;
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

import gui.InvisiblePanel;
import gui.OCButton;
import gui.SubPanel;
import gui.TopLevelContainerOld;

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
	
	private NotebookPanel thisNotebookPanel;
	
	private boolean justClosedTab;
	
	public NotebookPanel (OpenNotebook openbook, Document doc){
		super(null);
		openNotebook = openbook;
		
		this.setLayout(new GridBagLayout());
		
		fileChooser = new JFileChooser();
		
		JTabbedPane docTabs = new JTabbedPane();
		GridBagConstraints bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = 1;
		bCon.weighty = 1;
		this.add(docTabs, bCon);
		
		openDocs = new Vector<DocViewerPanel>();
		openDocs.add(new DocViewerPanel(doc, getTopLevelContainer()));
		docTabs.add(openDocs.get(0), openDocs.get(0).getDoc().getName());
		
	}
	
	public NotebookPanel(OpenNotebook openbook) {
		//create individual GUI elements here
		super(null);
		openNotebook = openbook;
		thisNotebookPanel = this;
		
		justClosedTab = false;
		
		fileChooser = new JFileChooser();
		
		this.setLayout(new BorderLayout());
		
		docTabs = new JTabbedPane();
		docTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		add(docTabs);
		
		JToolBar fileActions = new JToolBar("file actions");
		JToolBar objectActions = new JToolBar("object actions");
		JToolBar objectToolbar = new JToolBar("objects");
		
		JPanel topToolBars = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		topToolBars.setLayout(layout);
		topToolBars.add(fileActions);
		topToolBars.add(objectActions);
		
		add(topToolBars, BorderLayout.NORTH);
		add(objectToolbar, BorderLayout.SOUTH);
		
		Icon saveIcon = getIcon("img/save.png");
		
		OCButton saveButton = new OCButton(saveIcon, "Save", 1, 1, 2, 0, fileActions, null){
			
			public void associatedAction(){
				
				BufferedWriter f;
				try {
					int value = fileChooser.showSaveDialog(this);
					if (value == JFileChooser.APPROVE_OPTION){
						java.io.File file = fileChooser.getSelectedFile();
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
		};
		
		ImageIcon openIcon = getIcon("img/open.png");
		
		OCButton openButton = new OCButton(openIcon, "Open", 1, 1, 2, 0, fileActions, null){
			
			public void associatedAction(){
				DocReader reader = new DocReader();
				try {
					int value = fileChooser.showOpenDialog(this);
					if (value == JFileChooser.APPROVE_OPTION){
						addDoc(reader.readFile(fileChooser.getSelectedFile()));
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
		};
		
		ImageIcon newPageIcon = getIcon("img/newPage.png");
		
		OCButton newPageButton = new OCButton(newPageIcon, "Add Page", 1, 1, 2, 0, fileActions, null){
			
			public void associatedAction(){
				getCurrentDocViewer().getDoc().addBlankPage();
				getCurrentDocViewer().resizeViewWindow();
			}
		};
		
		ImageIcon pagePropsIcon = getIcon("img/pageProperties.png");
		
		OCButton pagePropsButton = new OCButton(pagePropsIcon, "Doc Properties", 1, 1, 2, 0, fileActions, null){
			
			public void associatedAction(){
				getCurrentDocViewer().toggleDocPropsFrame();
			}
		};
		
		ImageIcon zoomInIcon = getIcon("img/zoomIn.png");
		
		OCButton zoomIn = new OCButton(zoomInIcon, "Zoom In", 1, 1, 2, 0, fileActions, null){
			
			public void associatedAction(){
				openDocs.get(docTabs.getSelectedIndex()).zoomIn();
			}
		};

		ImageIcon zoomOutIcon = getIcon("img/zoomOut.png");
		
		OCButton zoomOut = new OCButton(zoomOutIcon, "Zoom Out", 1, 1, 3, 0, fileActions, null){
			
			public void associatedAction(){
				openDocs.get(docTabs.getSelectedIndex()).zoomOut();
			}
		};
		
		ImageIcon undoIcon = getIcon("img/undo.png");
		
		OCButton undoButton = new OCButton(undoIcon, "Undo", 1, 1, 3, 0, fileActions, null){
			
			public void associatedAction(){
			}
		};
		
		ImageIcon redoIcon = getIcon("img/redo.png");
		
		OCButton redoButton = new OCButton(redoIcon, "Redo", 1, 1, 3, 0, fileActions, null){
			
			public void associatedAction(){
			}
		};
		
		ImageIcon printIcon = getIcon("img/print.png");
		
		OCButton printButton = new OCButton(printIcon, "Print", 1, 1, 3, 0, fileActions, null){
			
			public void associatedAction(){
				DocPrinter docPrinter = new DocPrinter();
					
				docPrinter.setDoc(openDocs.get(docTabs.getSelectedIndex()).getDoc());
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
		};
		
		
		ImageIcon deleteIcon = getIcon("img/delete.png");
		
		OCButton deleteButton = new OCButton(deleteIcon, "Delete", 1, 1, 3, 0, objectActions, null){
			
			//allow this button to also delete page, need to add code to allow page to be
			//selected and have visual feedback (a border) to indicate selection
			public void associatedAction(){
				if (getCurrentDocViewer().getFocusedObject() != null){
					MathObject mObj = openDocs.get(docTabs.getSelectedIndex()).getFocusedObject();
					mObj.getParentPage().removeObject(mObj);
					openDocs.get(docTabs.getSelectedIndex()).setFocusedObject(null);
					openDocs.get(docTabs.getSelectedIndex()).repaintDoc();
				}
			}
		};
		
		ImageIcon bringToFrontIcon = getIcon("img/bringToFront.png");
		
		OCButton bringToFrontButton = new OCButton(bringToFrontIcon, "Bring to Front", 1, 1, 3, 0, objectActions, null){
			
			public void associatedAction(){
				MathObject mObj = getCurrentDocViewer().getFocusedObject();
				if (mObj!= null){
					mObj.getParentPage().bringObjectToFront(mObj);
					getCurrentDocViewer().repaintDoc();
				}
			}
		};
		
		ImageIcon sendForwardIcon = getIcon("img/sendForward.png");
		
		OCButton sendForwardButton = new OCButton(sendForwardIcon, "Send Forward", 1, 1, 3, 0, objectActions, null){
			
			public void associatedAction(){
				MathObject mObj = getCurrentDocViewer().getFocusedObject();
				if (mObj!= null){
					mObj.getParentPage().sendObjectForward(mObj);
					getCurrentDocViewer().repaintDoc();
				}
			}
		};
		ImageIcon bringToBackIcon = getIcon("img/bringToBack.png");
		
		OCButton bringToBackButton = new OCButton(bringToBackIcon, "Bring to Back", 1, 1, 3, 0, objectActions, null){

			public void associatedAction(){
				MathObject mObj = getCurrentDocViewer().getFocusedObject();
				if (mObj!= null){
					mObj.getParentPage().bringObjectToBack(mObj);
					openDocs.get(docTabs.getSelectedIndex()).repaintDoc();
				}
			}
		};
		ImageIcon sendBackwardIcon = getIcon("img/sendBackward.png");
		
		OCButton sendBackwardButton = new OCButton(sendBackwardIcon, "Send Backward", 1, 1, 3, 0, objectActions, null){
			
			//allow this button to also delete page, need to add code to allow page to be
			//selected and have visual feedback (a border) to indicagte selection
			public void associatedAction(){
				MathObject mObj = getCurrentDocViewer().getFocusedObject();
				if (mObj!= null){
					mObj.getParentPage().sendObjectBackward(mObj);
					getCurrentDocViewer().repaintDoc();
				}
			}
		};

		ImageIcon rectIcon = getIcon("img/rectangle.png");
		
		OCButton rectButton = new OCButton(rectIcon, "Add Rectangle", 1, 1, 0, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				getCurrentDocViewer().createMathObject(new RectangleObject(new Page(new Document("temp"))));
			}
		};
		
		ImageIcon ovalIcon = getIcon("img/oval.png");
		
		OCButton ovalButton = new OCButton(ovalIcon, "Add Oval", 1, 1, 1, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj

				getCurrentDocViewer().createMathObject(new OvalObject(new Page(new Document("temp"))));
			}
		};

		ImageIcon triangleIcon = getIcon("img/triangle.png");
		
		OCButton triangleButton = new OCButton(triangleIcon, "Add Triangle", 1, 1, 1, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				getCurrentDocViewer().createMathObject(new TriangleObject(new Page(new Document("temp"))));
			}
		};
		
		ImageIcon hexagonIcon = getIcon("img/hexagon.png");
		
		OCButton hexagonButton = new OCButton(hexagonIcon, "Add Hexagon", 1, 1, 1, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				getCurrentDocViewer().createMathObject(new HexagonObject(new Page(new Document("temp"))));
			}
		};
		
		ImageIcon trapezoidIcon = getIcon("img/trapezoid.png");
		
		OCButton trapezoidButton = new OCButton(trapezoidIcon, "Add Trapezoid", 1, 1, 1, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				getCurrentDocViewer().createMathObject(new TrapezoidObject(new Page(new Document("temp"))));
			}
		};
		
		
		ImageIcon parallelogramIcon = getIcon("img/parallelogram.png");
		
		OCButton parallelogramButton = new OCButton(parallelogramIcon, "Add Parallelogram", 1, 1, 1, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				getCurrentDocViewer().createMathObject(new ParallelogramObject(new Page(new Document("temp"))));
			}
		};
		
		ImageIcon cubeIcon = getIcon("img/cube.png");
		
		OCButton cubeButton = new OCButton(cubeIcon, "Add Cube", 1, 1, 1, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				getCurrentDocViewer().createMathObject(new CubeObject(new Page(new Document("temp"))));
			}
		};
		
		ImageIcon gridIcon = getIcon("img/grid.png");
		
		OCButton gridButton = new OCButton(gridIcon, "Add Graph", 1, 1, 2, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				getCurrentDocViewer().createMathObject(new GraphObject(new Page(new Document("temp"))));
			}
		};
		
		ImageIcon numberLineIcon = getIcon("img/numberLine.png");
		
		OCButton numberLineButton = new OCButton(numberLineIcon, "Add Number Line", 1, 1, 2, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				getCurrentDocViewer().createMathObject(new NumberLineObject(new Page(new Document("temp"))));
			}
		};
		
		ImageIcon textIcon = getIcon("img/text.png");
		
		OCButton textButton = new OCButton(textIcon, "Add Text", 1, 1, 6, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				getCurrentDocViewer().createMathObject(new TextObject(new Page(new Document("temp"))));
			}
		};
		
		ImageIcon expressionIcon = getIcon("img/expression.png");
		
		OCButton expressionButton = new OCButton(expressionIcon, "Add Expression", 1, 1, 6, 0, objectToolbar, null){
			
			public void associatedAction(){
				//pass even down to current doc window for placing a mathObj
				
				getCurrentDocViewer().createMathObject(new ExpressionObject(new Page(new Document("temp"))));
			}
		};
		
		openDocs = new Vector<DocViewerPanel>();
		Document newDoc = new Document("Untitled Document");
		newDoc.addBlankPage();
		openDocs.add(new DocViewerPanel(newDoc, getTopLevelContainer()));
		
		//change the value passed in to randomly add objects onto the screen
		randomlyAddObjects(0);
		
		int tabIndex = 0;
		for (DocViewerPanel d : openDocs){
			docTabs.addTab(d.getDoc().getName(), d);
			docTabs.setTabComponentAt(tabIndex, new DocTabClosePanel(this, d));
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
	
	public void addDoc(Document doc){
		int numTabs = docTabs.getTabCount();
		openDocs.add(new DocViewerPanel(doc, getTopLevelContainer()));
		
		//remove the old adding new doc tab
		docTabs.remove(numTabs - 1);
		
		//add new doc
		docTabs.add(openDocs.get(openDocs.size() - 1),
				openDocs.get(openDocs.size()-1).getDoc().getName());
		docTabs.setTabComponentAt(openDocs.size()-1,
				new DocTabClosePanel(thisNotebookPanel,
						openDocs.get(openDocs.size() - 1)));
		
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
				if ( ! openDocs.get(doc).getDoc().getPage(page).addMathObject(objectToAdd))
				{// attempt to add was not successful
					i--;
				}
			}
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
