/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */


package doc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import gui.OCFrame;
import gui.OCclipboard;
import gui.TopLevelContainerOld;

/**
 * The top level frame for the OpenNotebook interface. This class is mostly
 * for managing the menubar.
 * 
 * @author jason
 *
 */
public class NotebookFrame extends OCFrame implements TopLevelContainerOld{

	JMenuBar menuBar;
	JMenu help;
	
	public NotebookFrame(String s) {
		super(s);
		menuBar = new JMenuBar();
		help = new JMenu("Help");
		menuBar.add(help);
		UIManager.put("swing.boldMetal", Boolean.FALSE);
//		clipboard = new OCclipboard();

		JMenuItem tutorial = new JMenuItem("Tutorial");
		tutorial.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {}
		});

		JMenuItem license = new JMenuItem("License");
		license.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {}
		});

		help.add(tutorial);
		help.add(license);

		this.setJMenuBar(menuBar);
	}

}
