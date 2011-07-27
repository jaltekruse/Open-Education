/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import doc.GridPoint;
import doc.Page;

public class HexagonObject extends PolygonObject {
	
	public HexagonObject(Page p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h, t);
		addInitialPoints();
	}
	
	public HexagonObject(Page p){
		super(p);
		addInitialPoints();

	}
	
	@Override
	public void addDefaultAttributes() {
		
	}

	@Override
	public void addInitialPoints() {
		for (int i = 0 ; i < 6 ; i++){
			
		}
		addVertex(new GridPoint(.25, 0));
		addVertex(new GridPoint(.75, 0));
		addVertex(new GridPoint(1, .5));
		addVertex(new GridPoint(.75, 1));
		addVertex(new GridPoint(.25, 1));
		addVertex(new GridPoint(0, .5));
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return HEXAGON_OBJECT;
	}
	

}
