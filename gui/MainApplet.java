/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;


import gui.graph.GraphPanel;
import gui.graph.GraphWindow;
import gui.graph.GridPropsPanel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tree.EvalException;
import tree.ExpressionParser;
import tree.ParseException;
import tree.Expression;
import tree.ValueNotStoredException;

import java.io.*;
import java.net.*;


/**
 * A NewCalc object represents the entire user interface. It uses the standard
 * GridBag Layout manager. 
 * @author jason
 *
 */
public class MainApplet extends JApplet implements TopLevelContainerOld{

	private static final long serialVersionUID = 1L;
	private static OCTextField textWithFocus;
	
	private static CalcPanel text;
	private ValStoragePanel varPanel, constPanel;
	private JScrollPane varScrollPane, constScrollPane;
	
	private ExpressionParser parser;
	private JTabbedPane calcTabs, mathFunc, graphTabs;
	private FunctionsPane graphFunctions;
	private GridPropsPanel gridProps;
	private GraphOld g;
	private static int textWithFocusCaretPos;
	private static JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem help;
	private OCFrame tutorialFrame, licenseFrame, calcFrame, drawFrame, graphFrame;
	static GlassPane glassPane;
	private Container contentPane;
	private GraphPanel graphPanel;
	private OCclipboard clipboard;
	private static ProblemPanel render;
	
	/**
	 * @throws ValueNotStoredException 
	 * @throws ParseException 
	 * @throws EvalException 
	 * 
	 */

	public MainApplet() throws ParseException, ValueNotStoredException, EvalException {

		UIManager.put("swing.boldMetal", Boolean.FALSE);
		contentPane = this.getContentPane();
		contentPane.setLayout(new GridBagLayout());
		parser = new ExpressionParser(this);
		textWithFocus = new OCTextField(this);
		glassPane = new GlassPane(this, this);
		this.setGlassPane(glassPane);
		menuBar = new JMenuBar();
		help = new JMenu("Help");
		menuBar.add(help);
		clipboard = new OCclipboard();

		JMenuItem tutorial = new JMenuItem("Tutorial");
		tutorial.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(tutorialFrame == null){
					tutorialFrame = makeTextViewer("README.txt");
				}
				else{
					tutorialFrame.setVisible(true);
				}
			}
		});

		JMenuItem license = new JMenuItem("License");
		license.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(licenseFrame == null){
					licenseFrame = makeTextViewer("COPYING.txt");
				}
				else{
					licenseFrame.setVisible(true);
				}
			}
		});

		help.add(tutorial);
		help.add(license);

		this.setJMenuBar(menuBar);
		
		graphTabs = new JTabbedPane(JTabbedPane.TOP);

		


		graphPanel = new GraphPanel(this, this, 200, 200);
		graphTabs.add(graphPanel, "Graph");
		
		graphTabs.add(new DrawPad(this, this, 300, 300), "Draw");
		
		render = new ProblemPanel(this, this);
		graphTabs.add(render,"render");
		
		//have been working a lot with the rendering system, so it is set
		//to be the default tab
		graphTabs.setSelectedIndex(3);

		graphTabs.addChangeListener(graphTabsListener());

//		pCon.fill = GridBagConstraints.BOTH;
//		pCon.insets = new Insets(2, 2, 2, 2);
//		pCon.weightx = 1;
//		pCon.weighty = 1;
//		pCon.gridheight = 1;
//		pCon.gridwidth = 1;
//		pCon.weightx = .01;
//		pCon.weighty = 1;
//		pCon.gridx = 0;
//		pCon.gridy = 0;
//		contentPane.add(text, pCon);
		
		GridBagConstraints pCon = new GridBagConstraints();
		
		pCon.fill = GridBagConstraints.BOTH;
		pCon.insets = new Insets(2, 2, 2, 2);
		pCon.weightx = 1;
		pCon.weighty = 1;
		pCon.gridheight = 2;
		pCon.gridwidth = 1;
		pCon.weightx = 1;
		pCon.weighty = 1;
		pCon.gridx = 1;
		pCon.gridy = 0;
		contentPane.add(graphTabs, pCon);

//		pCon.fill = GridBagConstraints.HORIZONTAL;
//
//		pCon.insets = new Insets(2, 2, 2, 2);
//		pCon.gridheight = 1;
//		pCon.gridwidth = 1;
//		pCon.weightx = .01;
//		pCon.weighty = .1;
//		pCon.gridx = 0;
//		pCon.gridy = 1;
//		contentPane.add(mathFunc, pCon);
		
		this.repaint();
	}
	
	public void copyToClipboard(String s){
		clipboard.CopyToClipboard(s);
	}
	
	public BufferedImage getImage(String fileName){
		BufferedImage tempImage = null;
		try {
			//System.out.println("Version2_FILENAME!!!!!:  " + fileName);
			tempImage =  ImageIO.read(getClass().getClassLoader().getResourceAsStream("img/" + fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempImage;
	}
	
	public void showCalc(){
		if (!calcFrame.isShowing()){
			calcFrame.setVisible(true);
		}
	}
	
	public void makeErrorDialog(String s){
		JOptionPane.showMessageDialog(frame,
			    s,
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
	}

	public void setGlassVisible(boolean b){
		glassPane.setVisible(b);
	}
	
	public Container getContentPane2(){
		return contentPane;
	}
	
	protected OCFrame makeTextViewer(String string) {
		OCFrame newFrame = new OCFrame(string);
		newFrame.setPreferredSize(new Dimension(640, 400));
		JTextArea terminal = new JTextArea(14, 20);

		Font terminalFont = new Font("newFont", 1, 12);
		terminal.setFont(terminalFont);
		terminal.setEditable(false);
		JScrollPane termScrollPane = new JScrollPane(terminal);
		termScrollPane.setWheelScrollingEnabled(true);

		newFrame.add(termScrollPane);
		newFrame.pack();

		newFrame.setVisible(true);
		terminal.append(readTextDoc(string));
		termScrollPane.revalidate();
		JScrollBar tempScroll = termScrollPane.getVerticalScrollBar();
		tempScroll.setValue(0);
		newFrame.pack();
		newFrame.repaint();
		return newFrame;
	}

	private ChangeListener graphTabsListener() {
		return new ChangeListener(){

			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				int selected = 	graphTabs.getSelectedIndex();
				String nameSelected = graphTabs.getTitleAt(selected);
				if (nameSelected.equals("Graph"))
				{//hack to make the graph update when you hit the tab, this should be done better
					try {
						if (! getCurrTextField().equals(text.getEntryLine()))
						{
							getCurrTextField().primaryAction();
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ValueNotStoredException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (EvalException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (nameSelected.equals("newGraph")){
					graphPanel.repaint();
				}
				if (nameSelected.equals("render")){
					try {
						setCurrTextField(render.getEntryLine());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ValueNotStoredException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (EvalException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
	}

	public int getFocusedComp() {
		int currPos = calcTabs.getSelectedIndex();
		return currPos;
	}

	public ExpressionParser getParser(){
		return parser;
	}
	
	public JMenuBar getJMenuBar(){
		return menuBar;
	}

	/**
	 * This is not currently being used, but it is code to make an HTML request. So
	 * we could scan in a database of constants, functions or other stuff from our
	 * server.
	 * 
	 * @param args
	 * @return
	 */
	public String getWebPage(String[] args){

		String page= "";
		try {
			// Construct data
			String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
			data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");

			// Send data
			URL url = new URL("http://paigeinvaders.sourceforge.net/index.html");
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

	/**
	 * Reads a specified text document.
	 * 
	 * @param docName - the filename
	 * @return a String representation of the document
	 */
	public String readTextDoc(String docName){
		String line;
		try{
			InputStream in = getClass().getClassLoader().getResourceAsStream("txt/" + docName);
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			StringBuffer strBuff = new StringBuffer();
			while((line = bf.readLine()) != null){
				strBuff.append(line + "\n");
			}
			getClass().getClassLoader().getResourceAsStream("/txt/" + docName);
			return strBuff.toString();
		}
		catch(IOException e){
			System.out.println("error");
			e.printStackTrace();
		}
		return null;
	}

	public void updateGraph() {
		graphPanel.repaint();
	}

	public void updateGraph(String func) {
		g.repaint();
	}

	public OCTextField getCurrTextField() {
		return textWithFocus;
	}

	public ValStoragePanel getVarsPanel(){
		return varPanel;
	}

	public static void setCurrTextField(OCTextField focused) throws ParseException, ValueNotStoredException, EvalException {
		if (!textWithFocus.equals(focused)) {
//			if (textWithFocus != text.getEntryLine())
//			{//if the focus changes to a textfield other than the calculators entryline
//				//perform the associatedAction of the last field
//				textWithFocus.primaryAction();
//			}
			glassPane.setHistoryVisible(false);
			textWithFocus = focused;
		}
		textWithFocus.getField().requestFocus();
	}
	
	public CalcPanel getCalcPanel(){
		return text;
	}

	public void updateCaretPos() {
		textWithFocus.getField().setCaretPosition(textWithFocusCaretPos);
	}

	public GraphOld getGraphObj() {
		return g;
	}

	public GridPropsPanel getGridProps(){
		return gridProps;
	}

	public Expression evalCalc(String eqtn) throws EvalException, ParseException {
		return parser.ParseExpression(eqtn).eval();
	}

	private static void createAndShowGUI() throws ParseException, ValueNotStoredException, EvalException {

		frame = new JFrame("OpenCalc");
		Dimension frameDim = new Dimension(1000, 725);
		frame.setPreferredSize(frameDim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(false);

		MainApplet currCalc = new MainApplet();
		frame.add(currCalc);
		
		frame.pack();
		frame.setVisible(true);
		
		
		//make frame for calculator
		
		OCFrame calcFrame = new CalcFrame(currCalc);
		calcFrame.setPreferredSize(new Dimension(460, 700));
		calcFrame.pack();
		
		
		
		
//		setCurrTextField(text.getEntryLine());
//		text.getEntryLine().getField().selectAll();
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGUI();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ValueNotStoredException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EvalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public GlassPane getGlassPanel() {
		// TODO Auto-generated method stub
		return glassPane;
	}
}
