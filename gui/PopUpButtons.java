/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.Dimension;

public class PopUpButtons extends SubPanel {

	MainApplet mainApp;
	
	public PopUpButtons(TopLevelContainerOld topLevelComp, MainApplet mainApp) {
		super(topLevelComp);
		this.mainApp = mainApp;
		
		OCButton advanced = new OCButton("Calculator", 1, 1, 10, 0, this, mainApp){
			public void associatedAction() {
				
			}
		};
	}

}
