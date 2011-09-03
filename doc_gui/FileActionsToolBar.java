package doc_gui;


import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import doc.Page;
import doc.xml.DocReader;

public class FileActionsToolBar extends JToolBar {
	
	private NotebookPanel notebookPanel;
	
	public FileActionsToolBar(NotebookPanel p){
		notebookPanel = p;
		
		Icon saveIcon = notebookPanel.getIcon("img/save.png");
		
		OCButton saveButton = new OCButton(saveIcon, "Save [ctrl+s]", 1, 1, 2, 0, this){
			
			public void associatedAction(){
				notebookPanel.save();
			}
		};
		
		saveButton.setMnemonic(KeyEvent.VK_S);
		
		ImageIcon openIcon = notebookPanel.getIcon("img/open.png");
		
		OCButton openButton = new OCButton(openIcon, "Open [ctrl+o]", 1, 1, 2, 0, this){
			
			public void associatedAction(){
				notebookPanel.open();
			}
		};
		
		if ( ! notebookPanel.isInStudentMode()){
			ImageIcon addPageIcon = notebookPanel.getIcon("img/newPage.png");
			
			OCButton addPageButton = new OCButton(addPageIcon,
					"Add Page [ctrl+n](before selected, or at the end if none selected)", 1, 1, 2, 0, this){
				
				public void associatedAction(){
					notebookPanel.addPage();
				}
			};
			
			ImageIcon pagePropsIcon = notebookPanel.getIcon("img/pageProperties.png");
			
			OCButton pagePropsButton = new OCButton(pagePropsIcon, "Doc Properties", 1, 1, 2, 0, this){
				
				public void associatedAction(){
					notebookPanel.showDocProperties();
				}
			};
		}
		
		ImageIcon zoomInIcon = notebookPanel.getIcon("img/zoomIn.png");
		
		OCButton zoomIn = new OCButton(zoomInIcon, "Zoom In [ctrl and '+']", 1, 1, 2, 0, this){
			
			public void associatedAction(){
				notebookPanel.zoomIn();
			}
		};

		ImageIcon zoomOutIcon = notebookPanel.getIcon("img/zoomOut.png");
		
		OCButton zoomOut = new OCButton(zoomOutIcon, "Zoom Out [ctrl and '-']", 1, 1, 3, 0, this){
			
			public void associatedAction(){
				notebookPanel.getCurrentDocViewer().zoomOut();
			}
		};
//		if ( ! notebookPanel.isInStudentMode()){
//			ImageIcon undoIcon = notebookPanel.getIcon("img/undo.png");
//			
//			OCButton undoButton = new OCButton(undoIcon, "Undo", 1, 1, 3, 0, this){
//				
//				public void associatedAction(){
//				}
//			};
//			
//			ImageIcon redoIcon = notebookPanel.getIcon("img/redo.png");
//			
//			OCButton redoButton = new OCButton(redoIcon, "Redo", 1, 1, 3, 0, this){
//				
//				public void associatedAction(){
//				}
//			};
//		}
		
		ImageIcon printIcon = notebookPanel.getIcon("img/print.png");
		
		OCButton printButton = new OCButton(printIcon, "Print [ctrl+p]", 1, 1, 3, 0, this){
			
			public void associatedAction(){
				notebookPanel.print();
			}
		};
	}

}
