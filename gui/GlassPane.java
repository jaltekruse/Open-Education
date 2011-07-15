/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class GlassPane extends JComponent{

	MainApplet mainApp;
	GlassPane thisPane;
	int width, height, x, y, fontHeight, selectStart, selectEnd;
	ArrayList<String> history;
	JTextArea historyPane;
	private JScrollPane histScrollPane;
	TopLevelContainerOld topLevelContainerOld;

	public GlassPane(final MainApplet mainApp, TopLevelContainerOld c){
		this.mainApp = mainApp;
		topLevelContainerOld = c;
		thisPane = this;
		this.setVisible(true);
	}

	public void moveIndex(int num){
		
		String text = historyPane.getText();
		int caret = historyPane.getCaretPosition();
		
		if (num < 0)
		{
			for (int i = 1; caret - i > 0; i++)
			{
				if (text.charAt(caret - i) == '\n')
				{
					selectStart = caret - i + 1;
					
					for (int j = 1; selectStart + j < text.length(); j++)
					{
						if (text.charAt(selectStart+ j) == '\n')
						{
							selectEnd = selectStart + j;
							return;
						}
					}
				}
			}
			
			//you are on the first line currently
			
			selectStart = 0;
			for (int i = 1; i < text.length(); i++)
			{
				if (text.charAt(i) == '\n')
				{
					selectEnd = i;
					return;
				}
			}
		}
		
		if (num > 0){
			for (int i = 1; caret - i > 0; i++)
			{
				if (text.charAt(caret - i) == '\n')
				{
					selectStart = caret - i + 1;
					break;
				}
			}
			
			for (int i = 1; selectStart + i < text.length(); i++)
			{
				if (text.charAt(selectStart + i) == '\n')
				{
					selectEnd = selectStart + i;
					return;
				}
			}
			
			//you are on the last line currently
			
			selectEnd = text.length();
			for (int i = selectEnd - 2; i > 0; i--){
				if (text.charAt(i) == '\n'){
					selectStart = i + 1;
					return;
				}
			}
		}
		refresh();
	}
	
	public void refresh(){
//		if (mainApp.getCurrTextField().getHistory().size() == 0)
//		{//requested to add a history box to a field without a history
//			return;
//		}
		if (!mainApp.getCurrTextField().isShowing()){
			setHistoryVisible(false);
			return;
		}
		if (!history.isEmpty()){
			Scanner s = new Scanner(historyPane.getText());
			historyPane.requestFocus();
			historyPane.setSelectionStart(selectStart);
			historyPane.setSelectionEnd(selectEnd);
			this.setVisible(true);
			this.repaint();
		}
	}

	public void addFieldHistory(){
//		if (mainApp.getCurrTextField().getHistory().size() == 0)
//		{//requested to add a history box to a field without a history
//			return;
//		}
		if (!mainApp.getCurrTextField().isShowing()){
			setHistoryVisible(false);
			return;
		}
		this.removeAll();
		historyPane = new JTextArea(5,5);
		history = mainApp.getCurrTextField().getHistory();
		System.out.println(mainApp.getCurrTextField());
		historyPane.setEditable(true);
		
		histScrollPane = new JScrollPane(historyPane);
		histScrollPane.setWheelScrollingEnabled(true);
		
		historyPane.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					mainApp.getCurrTextField().getField().setText(historyPane.getSelectedText());
					mainApp.getCurrTextField().getField().requestFocus();
					setHistoryVisible(false);
				}
				
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
					setHistoryVisible(false);
					mainApp.getCurrTextField().getField().requestFocus();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_UP){
					if (selectStart == 0){
						mainApp.getCurrTextField().getField().requestFocus();
						setHistoryVisible(false);
						return;
					}
					moveIndex(-1);
					refresh();
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					moveIndex(1);
					refresh();
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					;
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			
		});
		
		historyPane.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				//historyPane.setVisible(false);
				//the above line doesn't work, need to find some way to
				//get rid of history when the interface changes and the
				//current field isn't being displayed
			}
			
		});
		
		historyPane.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				selectCurrLine();
				refresh();
				mainApp.getCurrTextField().getField().setText(historyPane.getSelectedText());
				mainApp.getCurrTextField().getField().requestFocus();
				setHistoryVisible(false);
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		height = history.size() * 18;
		
		if (history.size() == 0){
			height = 16;
			historyPane.append("empty history");
		}
		else{
	
			int i = history.size() - 1;
			historyPane.append(history.get(i));
			for (i--; i >= 0; i--){
				historyPane.append('\n' + history.get(i));
			}
		}
		
		x = mainApp.getCurrTextField().getField().getLocationOnScreen().x
			- topLevelContainerOld.getContentPane().getLocationOnScreen().x;
		y = mainApp.getCurrTextField().getField().getLocationOnScreen().y
			- topLevelContainerOld.getContentPane().getLocationOnScreen().y 
			+ mainApp.getCurrTextField().getField().getHeight();
		width = 200;
		
		if (y + mainApp.getCurrTextField().getField().getHeight() + height > topLevelContainerOld.getHeight())
		{//if the history is going to hang out of the window
			//make it instead above the field
			y = mainApp.getCurrTextField().getField().getLocationOnScreen().y
			- topLevelContainerOld.getContentPane().getLocationOnScreen().y - height;
		}
		if (topLevelContainerOld.getJMenuBar() != null){
			y += topLevelContainerOld.getJMenuBar().getHeight();
		}

		historyPane.setBorder(new LineBorder(Color.BLACK, 1));
		historyPane.setBounds(x, y, width, height);
		historyPane.setOpaque(true);
		
		histScrollPane.setBorder(new LineBorder(Color.BLACK, 1));
		histScrollPane.setBounds(x, y, width, height);
		histScrollPane.setOpaque(true);
		
		this.add(histScrollPane);
		historyPane.setVisible(true);
		
		selectStart = 0;
		historyPane.setCaretPosition(1);
		
		selectCurrLine();
		this.setVisible(true);
		refresh();
	}

	private void selectCurrLine(){
		
		String text = historyPane.getText();
		int caret = historyPane.getCaretPosition();
		
		if (caret == 0){
			selectStart = 0;
			for (int j = 1; selectStart + j < text.length(); j++)
			{
				if (text.charAt(selectStart+ j) == '\n')
				{
					selectEnd = selectStart + j;
					return;
				}
			}
		}
		
		for (int i = 0; i + caret < text.length() + 1; i++)
		{
			if (i + caret == text.length())
			{//you are on the last line currently
				selectEnd = text.length();
				for (int j = selectEnd - 2; j > 0; j--){
					if (text.charAt(j) == '\n'){
						selectStart = j + 1;
						return;
					}
				}
			}
			else if (text.charAt(caret + i) == '\n')
			{
				break;
			}
		}
		for (int i = 1; caret - i > -1; i++)
		{
			if (caret - i == 0 || text.charAt(caret - i) == '\n')
			{
				selectStart = caret - i + 1;
				
				if (caret - i == 0){
					selectStart--;
				}
				for (int j = 1; selectStart + j < text.length(); j++)
				{
					if (text.charAt(selectStart+ j) == '\n')
					{
						selectEnd = selectStart + j;
						return;
					}
				}
			}
		}
	}
	
	public void setHistoryVisible(boolean b){
		if (histScrollPane != null)
		{
			if (b == true){
				JScrollBar tempScroll = histScrollPane.getVerticalScrollBar();
				tempScroll.setValue(0);
				historyPane.setCaretPosition(0);
				selectCurrLine();
				refresh();
				histScrollPane.setVisible(b);
			}
			else{
				histScrollPane.setVisible(b);
			}
		}
	}
	
	public boolean historyIsVisible(){
		if (histScrollPane != null){
			return histScrollPane.isVisible();
		}
		return false;
	}
}
