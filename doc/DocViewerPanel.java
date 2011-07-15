/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc;

import gui.SubPanel;
import gui.TopLevelContainerOld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import doc.mathobjects.MathObject;
import doc.mathobjects.MathObjectAttribute;
import doc.mathobjects.ObjectPropertiesFrame;

public class DocViewerPanel extends JDesktopPane{
	
	private Document doc;
	
	private float zoomLevel;
	
	private static final float zoomRate = 1.1f;
	
	private JScrollPane docScrollPane;
	
	private DocViewerPanel thisPanel;
	
	private ObjectPropertiesFrame toolFrame;
	
	private JInternalFrame docPropsFrame;
	
	/**
	 * The minimum space allowed around any side of the document. Given as an
	 * integer, 25, which much be scaled the same as the page using zoomLevel
	 * to display documents on the screen at different sizes.
	 */
	public static final int DOC_BUFFER_SPACE = 25;
	
	private int currentPage;
	
	private MathObject focusedObject;
	
	private PageGUI pageRenderer;
	
	private JPanel docPanel;
	
	private DocMouseListener docMouse;
	
	//keeps track number of successive scroll events, prevents excessive redrawing when
	//scroll wheel is moved quickly
	private int scrollCounter;
	
	
	public DocViewerPanel(Document d, TopLevelContainerOld t){
		doc = d;
		
		thisPanel = this;

		scrollCounter = 0;
		
		pageRenderer = new PageGUI(this);
		
		zoomLevel = 1;
		currentPage = 1;
		
		docPanel = makeDocPanel();
		resizeViewWindow();
		
		docMouse = new DocMouseListener(this);
		docPanel.addMouseListener(docMouse);
		docPanel.addMouseMotionListener(docMouse);		
		
		
		//this conflicts with the scrolling on the document
//		docPanel.addMouseWheelListener(makeMouseWheelListener());
		
		docScrollPane = new JScrollPane(docPanel);
		docScrollPane.setWheelScrollingEnabled(false);
		
		docScrollPane.addMouseWheelListener(new MouseWheelListener(){

			@Override
			public void mouseWheelMoved(final MouseWheelEvent e) {
				// TODO Auto-generated method stub
				if (e.getWheelRotation() < 0)
				{// the wheel movement was negative
					if (scrollCounter > 0)
					{//the last wheel movement was in the positive direction
						scrollCounter++;
						if (scrollCounter > 3 && docScrollPane.getViewport().getViewPosition().getY() > 0)
						{//there has been three consecutive positive wheel movements
							//and the view is not at the end of the doc
							docScrollPane.getViewport().setViewPosition(new Point(
									(int)docScrollPane.getViewport().getViewPosition().getX(), 
									(int) docScrollPane.getViewport().getViewPosition().getY()
									+ e.getWheelRotation() * 25 * 3));
							scrollCounter = 0;
						}
					}
					else
					{//the last motion was negative, start counting up positive movements
						scrollCounter = 1;
					}
				}
				
				if (e.getWheelRotation() > 0)
				{// the wheel movement was negative
					if (scrollCounter < 0)
					{//the last wheel movement was in the negative direction
						scrollCounter--;
						if (scrollCounter < -3 && docScrollPane.getViewport().getViewPosition().getY()
								+ docScrollPane.getViewport().getHeight() < docPanel.getHeight())
						{//there has been three consecutive positive wheel movements
							//and the view is not at the end of the doc
							docScrollPane.getViewport().setViewPosition(new Point(
									(int)docScrollPane.getViewport().getViewPosition().getX(), 
									(int) docScrollPane.getViewport().getViewPosition().getY()
									+ e.getWheelRotation() * 25 * 3));
							scrollCounter = 0;
						}
					}
					else
					{//the last motion was positive, start counting up negative movements
						scrollCounter = -1;
					}
				}
			}
			
		});
		
		toolFrame = new ObjectPropertiesFrame(this);
		this.add(toolFrame);
		//do not show yet, only appears when MathObject is selected
		
		docPropsFrame = new JInternalFrame("Document",
				true, //resizable
				false, //closable
				false, //maximizable
				false);//iconifiable
		
		docPropsFrame.setBounds(20, 100, 200, 300);
		this.add(docPropsFrame);
		
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
	
	public void setScrollBounds(int w, int h){
		docScrollPane.setBounds(0, 0, w, h);
	}
	
	private JPanel makeDocPanel() {
		// TODO Auto-generated method stub
		return new JPanel(){
			
			
			public void update(Graphics g){
				
			}
			
			public void paint(Graphics g){
			
//				System.out.println("repaint");
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
				Rectangle pageSectionShowing;
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
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					currPageRect = new Rectangle((int) pageOrigin.getX(), (int) pageOrigin.getY(), pageXSize, pageYSize);
					
					if (viewPortRect.intersects(currPageRect))
					{//render page only if it is in the current section of the document showing
						pageSectionShowing = new Rectangle();
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
							pageRenderer.drawPageWithDecorations(g, doc.getPage(i), new Point(
									(int) pageOrigin.getX(), (int) pageOrigin.getY()),
									new Rectangle(xShowing, yShowing, 10, 0), zoomLevel);
						} catch (DocumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				g.dispose();
			}
		};
	}
	
	public void toggleDocPropsFrame(){
		docPropsFrame.getContentPane().add(toolFrame.generatePanel(getDoc(), this));
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
					toolFrame.update();
			   }
		});
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

	public void setFocusedObject(MathObject focusedObject) {
		this.focusedObject = focusedObject;
		if (focusedObject != null){
			toolFrame.generatePanel(focusedObject);
			toolFrame.setVisible(true);
			toolFrame.setBounds(docScrollPane.getWidth() - 230,
					docScrollPane.getHeight()/2 - 150, 200, 350);
		}
		else{
			toolFrame.setVisible(false);
		}
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
		Rectangle currPageRect;
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
}
