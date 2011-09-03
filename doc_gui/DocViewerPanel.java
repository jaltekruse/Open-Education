/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc_gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;


import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import doc.Document;
import doc.Page;
import doc.PageGUI;
import doc.PointInDocument;
import doc.mathobjects.MathObject;
import doc.mathobjects.Grouping;
import doc.mathobjects.ProblemObject;
import doc.mathobjects.RectangleObject;

public class DocViewerPanel extends JDesktopPane{
	
	private Document doc;
	
	private float zoomLevel;
	
	private static final float zoomRate = 1.1f;
	
	private JScrollPane docScrollPane;
	
	private DocViewerPanel thisPanel;
	
	private ObjectPropertiesFrame objPropsFrame;
	private JInternalFrame databaseFrame;
	
	private JInternalFrame docPropsFrame;
	
	private Page selectedPage;
	
	private BufferedImage background;
	
	/**
	 * The minimum space allowed around any side of the document. Given as an
	 * integer, 25, which much be scaled the same as the page using zoomLevel
	 * to display documents on the screen at different sizes.
	 */
	public static final int DOC_BUFFER_SPACE = 25;
	
	private int currentPage;
	
	private MathObject focusedObject;
	
	private PageGUI pageGUI;
	
	private JPanel docPanel;
	
	private DocMouseListener docMouse;
	
	private RectangleObject selectionRect;
	
	//used for creating a group while the selection rectangle is being drawn
	private Grouping tempGroup;
	
	private boolean isInStudentMode;
	
	private OpenNotebook notebook;
	
	public DocViewerPanel(Document d, TopLevelContainerOld t, boolean b, OpenNotebook book){
		notebook = book;
		doc = d;
		doc.docPanel = this;
		
		isInStudentMode = b;
		thisPanel = this;
		
		tempGroup = new Grouping(new Page(doc));
		setPageGUI(new PageGUI(this));
		background = new BufferedImage(10,10, 10);
		
		zoomLevel = 1;
		currentPage = 1;
		
		docPanel = makeDocPanel();
		resizeViewWindow();
		
		docMouse = new DocMouseListener(this);
		docPanel.addMouseListener(docMouse);
		docPanel.addMouseMotionListener(docMouse);		
		
		docScrollPane = new JScrollPane(docPanel);
		docScrollPane.setWheelScrollingEnabled(true);
		docScrollPane.getVerticalScrollBar().setUnitIncrement(12);
		docScrollPane.getHorizontalScrollBar().setUnitIncrement(12);
		
		objPropsFrame = new ObjectPropertiesFrame(this);
		objPropsFrame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		objPropsFrame.setBounds(10, 10, 250, 300);
		this.add(objPropsFrame);
		//do not show yet, only appears when MathObject is selected
		
		docPropsFrame = new JInternalFrame("Document",
				true, //resizable
				true, //closable
				false, //maximizable
				false);//iconifiable
		
		
		docPropsFrame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		docPropsFrame.setBounds(20, 20, 200, 300);
		this.add(docPropsFrame);
		
		
		databaseFrame = new JInternalFrame("Document",
				true, //resizable
				true, //closable
				false, //maximizable
				false);//iconifiable
		
		databaseFrame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		databaseFrame.setBounds(20, 20, 300, 250);
		this.add(databaseFrame);
		
		this.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				setScrollBounds(thisPanel.getWidth(), thisPanel.getHeight());
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		this.add(docScrollPane);
	}
	
	public void drawObjectInBackgorund(MathObject o){
		pageGUI.drawObject(o, background.getGraphics(),
				o.getParentPage(), new Point(0,0),
				new Rectangle(),  zoomLevel);
	}
	
	public void setScrollBounds(int w, int h){
		docScrollPane.setBounds(0, 0, w, h);
	}
	
	private JPanel makeDocPanel() {
		// TODO Auto-generated method stub
		return new JPanel(){
			
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void update(Graphics g){
				
			}
			
			public void paint(Graphics g){
			
//				System.out.println("repaint");\
				
				//set the graphics object to render text and shapes with smooth lines
				Graphics2D g2d = (Graphics2D)g;
//				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				//pixels needed to render a page, and the minimum border space around it
				int pageXSize = (int) (Page.DEFAULT_PAGE_WIDTH * zoomLevel);
				int pageYSize = (int) (Page.DEFAULT_PAGE_HEIGHT * zoomLevel);
				
				Rectangle viewPortRect = new Rectangle( (int) docScrollPane.getViewport().getViewPosition().getX(),
						(int) docScrollPane.getViewport().getViewPosition().getY(),
						(int) docScrollPane.getViewport().getWidth(),
						(int) docScrollPane.getViewport().getHeight());
				
				//fill background with gray
				g.setColor(Color.GRAY.brighter());
				((Graphics2D) g).fill(viewPortRect);
				
//				System.out.println(viewPortRect.toString());
				Rectangle currPageRect;
				Point pageOrigin = null;
				
				//used to store origin of the part of the page that can be seen
				//and the sections overall width and height, both are in the user space at 72 dpi
				int xShowing, yShowing, xSizeShowing, ySizeShowing;
				
				for (int i = 1; i < doc.getNumPages(); i++){
					
					//need to modify rectangle to properly calculate the portion of the page currently displayed
					//which will be given in the user space starting with the origin of the printable area at 0,0
					//and at 72 dpi
					
					try {
						pageOrigin = getPageOrigin(i);
					} catch (DocumentException e1) {
						e1.printStackTrace();
					}
					currPageRect = new Rectangle((int) pageOrigin.getX(), (int) pageOrigin.getY(), pageXSize, pageYSize);
					
					if (viewPortRect.intersects(currPageRect))
					{//render page only if it is in the current section of the document showing
						if (viewPortRect.getX() < currPageRect.getX()){
							xShowing = 0;
						}
						else{
							xShowing = (int) ((viewPortRect.getX() - currPageRect.getX()) / zoomLevel);
						}
						
						if (viewPortRect.getY() < currPageRect.getY()){
							yShowing = 0;
							
						}
						else{
							yShowing = (int) ((viewPortRect.getY() - currPageRect.getY()) / zoomLevel);
						}
						
						try {
							pageGUI.drawPageWithDecorations(g, doc.getPage(i), new Point(
									(int) pageOrigin.getX(), (int) pageOrigin.getY()),
									new Rectangle(xShowing, yShowing, 10, 0), zoomLevel);
						} catch (DocumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				if (selectionRect != null){
					try {
						pageGUI.polygonGUI.drawMathObject(selectionRect, g2d,
								getPageOrigin(selectionRect.getParentPage()), zoomLevel);
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				g.dispose();
			}
		};
	}
	
	public void toggleDocPropsFrame(){
		docPropsFrame.getContentPane().add(objPropsFrame.generatePanel(getDoc(), this));
		docPropsFrame.setVisible( ! docPropsFrame.isVisible());
	}
	
	public void resizeViewWindow(){
		
		//pixels needed to render a page
		int pageXSize = (int) (Page.DEFAULT_PAGE_WIDTH * zoomLevel);
		int pageYSize = (int) (Page.DEFAULT_PAGE_HEIGHT * zoomLevel);
		int adjustedBufferSpace = (int) (DOC_BUFFER_SPACE * zoomLevel);
		
		//space needed including gray boarders around pages
		int widthNeeded = pageXSize + 2 * adjustedBufferSpace;
		int heightNeeded = adjustedBufferSpace;
		
		//add up space needed for all pages
		for (int i = 1; i < doc.getNumPages(); i++){
			heightNeeded += pageYSize + adjustedBufferSpace;
		}
		
		//set a new size for the panel to render the pages, and revalidate to force
		//scroll bar to re-adjust
		docPanel.setPreferredSize(new Dimension( widthNeeded, heightNeeded));
		EventQueue.invokeLater(new Runnable() {
			   public void run() {
					docPanel.revalidate();
			   }
		});
	}
	
	public void repaintDoc(){
		EventQueue.invokeLater(new Runnable() {
			   public void run() {
					docPanel.repaint();
					docPanel.revalidate();
			   }
		});
	}
	
	public void updateObjectToolFrame(){
		objPropsFrame.update();
	}
	
	public Document getDoc(){
		return doc;
	}
	
	public void zoomIn(){
		if (zoomLevel > 2){
			return;
		}
		zoomLevel *= zoomRate;
		
		EventQueue.invokeLater(new Runnable() {
			   public void run() {
					int oldVertPos = docScrollPane.getVerticalScrollBar().getValue();
					docScrollPane.getVerticalScrollBar().setValue((int) (oldVertPos * zoomRate));
					
					int oldHorizontalPos = docScrollPane.getHorizontalScrollBar().getValue();
					docScrollPane.getHorizontalScrollBar().setValue((int) (oldHorizontalPos * zoomRate));
					resizeViewWindow();
			   }
		});
	}
	
	public void zoomOut(){
		if (zoomLevel < .4){
			return;
		}
		zoomLevel *= (1/zoomRate);
		
		EventQueue.invokeLater(new Runnable() {
			   public void run() {
				   int oldVertPos = docScrollPane.getVerticalScrollBar().getValue();
					docScrollPane.getVerticalScrollBar().setValue((int) (oldVertPos /zoomRate));
					
					int oldHorizontalPos = docScrollPane.getHorizontalScrollBar().getValue();
					docScrollPane.getHorizontalScrollBar().setValue((int) (oldHorizontalPos /zoomRate));
					resizeViewWindow();
			   }
		});
	}

	public Page getCurrentPage() throws DocumentException {
		// TODO Auto-generated method stub
		return doc.getPage(currentPage);
	}
	
	//uses the integer codes declared as public ints above
	public void createMathObject(MathObject mObj){
		docMouse.setPlacingObject(true);
		docMouse.setObjToPlace(mObj);
	}
	
	public void setSelectedPage(int i){
		try {
			setSelectedPage(doc.getPage(i));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSelectedPage(Page p){
		if (doc.getPages().contains(p) && ! isInStudentMode()){
			selectedPage = p;
			setFocusedObject(null);
			if (tempGroup != null){
				resetTempGroup();
			}
		}
		else{
			if (p == null)
			{//to indicate that no page is currently selected
				selectedPage = null;
			}
			else{
				System.out.println("page is not in the document associated with this DocViewerPanel");
			}
		}
	}

	public Page getSelectedPage(){
		return selectedPage;
	}
	
	public void setFocusedObject(MathObject focusedObject) {
		System.out.println("set focused object");
		if (focusedObject != null){
			if ( ! isInStudentMode() || (isInStudentMode() && focusedObject.isStudentSelectable())){
				this.focusedObject = focusedObject;
				objPropsFrame.generatePanel(focusedObject);
				objPropsFrame.setVisible(true);
				setSelectedPage(null);
				if (tempGroup != null && focusedObject != tempGroup && focusedObject != selectionRect){
					resetTempGroup();
				}
			}
		}
		else{
			this.focusedObject = focusedObject;
			objPropsFrame.setVisible(false);
		}
	}
	
	public void resetTempGroup(){
		tempGroup.unGroup();
		tempGroup.getParentPage().removeObject(tempGroup);
		tempGroup = new Grouping(new Page(new Document("temp")));
	}
	
	public void removeTempGroup(){
		tempGroup.getParentPage().removeObject(tempGroup);
		tempGroup = new Grouping(new Page(new Document("temp")));
	}

	public MathObject getFocusedObject() {
		return focusedObject;
	}
	
	public PointInDocument panelPt2DocPt(int x, int y){
		//pixels needed to render a page, and the minimum border space around it
		int pageXSize = (int) (Page.DEFAULT_PAGE_WIDTH * zoomLevel);
		int pageYSize = (int) (Page.DEFAULT_PAGE_HEIGHT * zoomLevel);
		int adjustedBufferSpace = (int) (DOC_BUFFER_SPACE * zoomLevel);
		
		//draw the pages
		int pagexOrigin = (docPanel.getWidth() - pageXSize)/2;
		int pageyOrigin = adjustedBufferSpace;
		PointInDocument ptInDoc = new PointInDocument();
		ptInDoc.setOutSidePage(true);
		
		//go through all of the pages to look for collisions
		for (int i = 1; i < doc.getNumPages(); i++){
			
			if (y > pageyOrigin && y < pageyOrigin + pageYSize){
				
				ptInDoc.setyPos((int) ((y - pageyOrigin) / zoomLevel));
				ptInDoc.setPage(i);
				ptInDoc.setOutSidePage(false);
				break;
			}
			else if ( y < pageyOrigin)
			{//the click was above this page, but did not hit any previous pages, it must be in the buffer
				//space between pages
				ptInDoc.setPage(i);
				break;
			}
			pageyOrigin += pageYSize + adjustedBufferSpace;
		}
		
		if ( y > pageyOrigin + pageYSize)
		{// the click was below the last page
			ptInDoc.setBelowPage(true);
			ptInDoc.setPage(doc.lastPageIndex());
		}
		
		if( x < pagexOrigin || x > pagexOrigin + pageXSize ){
			ptInDoc.setOutSidePage(true);
		}
		else{
			ptInDoc.setxPos((int) ((x - pagexOrigin) / zoomLevel));
		}
		
		return ptInDoc;
	}
	
	public Point getPageOrigin(int pageIndex) throws DocumentException{
		//check to make sure the page number is valid
		//will possibly throw page not found exception
		doc.getPage(pageIndex);
		
		//pixels needed to render a page, and the minimum border space around it
		int pageXSize = (int) (Page.DEFAULT_PAGE_WIDTH * zoomLevel);
		int pageYSize = (int) (Page.DEFAULT_PAGE_HEIGHT * zoomLevel);
		int adjustedBufferSpace = (int) (DOC_BUFFER_SPACE * zoomLevel);
		
		return new Point((docPanel.getWidth() - pageXSize)/2,
				adjustedBufferSpace + (pageIndex - 1) * (pageYSize + adjustedBufferSpace));
	}
	
	public Point getObjectPos(MathObject mObj) throws DocumentException{
		Point pageOrigin = getPageOrigin(mObj.getParentPage());
		return new Point( (int) (pageOrigin.getX() + mObj.getxPos() * zoomLevel)
				, (int) (pageOrigin.getY() + mObj.getyPos() * zoomLevel));
	}
	
	public boolean generateProblems(ProblemObject probelm){
		
		return true;
	}
	
	public void showDatabase(){
		databaseFrame.getContentPane().removeAll();
		databaseFrame.getContentPane().add(new DatabasePanel(notebook.getDatabase(), this));
		databaseFrame.pack();
		databaseFrame.setVisible(true);
	}
	
	public Point getPageOrigin(Page p) throws DocumentException{
		return getPageOrigin(doc.getPageIndex(p) + 1);
	}
	
	public int getPagexSize(){
		return (int) (Page.DEFAULT_PAGE_WIDTH * zoomLevel);
	}

	public int getPageySize(){
		return  (int) (Page.DEFAULT_PAGE_HEIGHT * zoomLevel);
	}
	
	public float getZoomLevel(){
		return zoomLevel;
	}

	public void setInStudentMode(boolean isInStudentMode) {
		this.isInStudentMode = isInStudentMode;
	}

	public boolean isInStudentMode() {
		return isInStudentMode;
	}

	public void setPageGUI(PageGUI pageRenderer) {
		this.pageGUI = pageRenderer;
	}

	public PageGUI getPageGUI() {
		return pageGUI;
	}

	public void setSelectionRect(RectangleObject selectionRect) {
		this.selectionRect = selectionRect;
	}

	public RectangleObject getSelectionRect() {
		return selectionRect;
	}

	public void setTempGroup(Grouping tempGroup) {
		this.tempGroup = tempGroup;
	}

	public Grouping getTempGroup() {
		return tempGroup;
	}

	public void setNotebook(OpenNotebook notebook) {
		this.notebook = notebook;
	}

	public OpenNotebook getNotebook() {
		return notebook;
	}
}
