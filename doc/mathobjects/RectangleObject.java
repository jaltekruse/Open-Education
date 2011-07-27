/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package doc.mathobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import doc.GridPoint;
import doc.Page;

/**
 * A basic MathObject that represents a rectangular space on the screen.
 * 
 * @author jason altekruse
 *
 */
public class RectangleObject extends PolygonObject {
	
	public RectangleObject(Page p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h, t);
		addInitialPoints();
	}
	
	public RectangleObject(Page p){
		super(p);
		addInitialPoints();
	}

	@Override
	public void addInitialPoints() {
		addVertex(new GridPoint(0, 0));
		addVertex(new GridPoint(1, 0));
		addVertex(new GridPoint(1, 1));
		addVertex(new GridPoint(0, 1));
	}
	
	@Override
	public void addDefaultAttributes() {
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return RECTANGLE_OBJECT;
	}
}
