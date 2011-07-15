/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import javax.swing.JFrame;

public class OCFrame extends JFrame implements TopLevelContainerOld{

	private GlassPane invisiblePanel;
	
	public OCFrame(String s){
		invisiblePanel = new GlassPane(null, this);
		this.setTitle(s);
		this.setGlassPane(invisiblePanel);
	}
	
	//as opencalc is fully replaced by openNotebook this should be removed
	
	public OCFrame(String s, MainApplet mainApp){
		invisiblePanel = new GlassPane(mainApp, this);
		this.setTitle(s);
		this.setGlassPane(invisiblePanel);
	}
	
	@Override
	public GlassPane getGlassPanel() {
		// TODO Auto-generated method stub
		return invisiblePanel;
	}
}
